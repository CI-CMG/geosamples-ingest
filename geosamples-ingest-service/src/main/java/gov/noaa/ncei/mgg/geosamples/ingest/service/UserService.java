package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DescriptorView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ReadOnlySimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends
    SearchServiceBase<GeosamplesUserEntity, String, UserSearchParameters, UserView, GeosamplesUserRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(UserView.class);

  private final GeosamplesUserRepository geosamplesUserRepository;
  private final GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  public UserService(GeosamplesUserRepository geosamplesUserRepository,
      GeosamplesAuthorityRepository geosamplesAuthorityRepository) {
    this.geosamplesUserRepository = geosamplesUserRepository;
    this.geosamplesAuthorityRepository = geosamplesAuthorityRepository;
  }

  @Override
  protected String normalizeId(String id) {
    return id.trim().toLowerCase(Locale.ENGLISH);
  }

  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected List<Specification<GeosamplesUserEntity>> getSpecs(UserSearchParameters searchParameters) {
    List<Specification<GeosamplesUserEntity>> specs = new ArrayList<>();

    List<String> userNameContains = searchParameters.getUserNameContains();
    List<String> userNameEquals = searchParameters.getUserNameEquals();
    List<String> displayNameContains = searchParameters.getDisplayNameContains();

    if (!userNameContains.isEmpty()) {
      specs.add(SearchUtils.contains(userNameContains, GeosamplesUserEntity_.USER_NAME));
    }

    if (!userNameContains.isEmpty()) {
      specs.add(SearchUtils.equal(userNameEquals, GeosamplesUserEntity_.USER_NAME));
    }

    if (!displayNameContains.isEmpty()) {
      specs.add(SearchUtils.contains(displayNameContains, GeosamplesUserEntity_.DISPLAY_NAME));
    }

    return specs;
  }

  @Override
  protected GeosamplesUserRepository getRepository() {
    return geosamplesUserRepository;
  }

  @Override
  protected UserView toView(GeosamplesUserEntity entity) {
    UserView view = new UserView();
    view.setUserName(entity.getUserName());
    view.setDisplayName(entity.getDisplayName());
    List<String> authorities = new ArrayList<>();
    authorities.add(Authorities.ROLE_AUTHENTICATED_USER.toString());
    authorities.addAll(entity.getUserAuthorities().stream().map(GeosamplesUserAuthorityEntity::getAuthorityName).collect(Collectors.toList()));
    Collections.sort(authorities);
    view.setAuthorities(authorities);

    List<String> tokenAliases = new ArrayList<>();
    tokenAliases.addAll(entity.getTokens().stream().map(GeosamplesTokenEntity::getAlias).collect(Collectors.toList()));
    Collections.sort(tokenAliases);
    view.setTokenAliases(tokenAliases);

    return view;
  }

  private GeosamplesUserAuthorityEntity getUserAuthority(String authority) {
    GeosamplesUserAuthorityEntity entity = new GeosamplesUserAuthorityEntity();
    entity.setAuthority(getAuthority(authority));
    return entity;
  }

  private GeosamplesAuthorityEntity getAuthority(String authority) {
    return geosamplesAuthorityRepository.findById(authority)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find authority: " + authority).build()));
  }

  @Override
  protected GeosamplesUserEntity newEntityWithDefaultValues(UserView view) {
    GeosamplesUserEntity entity = new GeosamplesUserEntity();
    entity.setVersion(0);
    entity.setUserName(view.getUserName().trim().toLowerCase(Locale.ENGLISH));
    entity.setDisplayName(view.getDisplayName().trim());
    for (String authority : view.getAuthorities()) {
      entity.addUserAuthority(getUserAuthority(authority));
    }
    return entity;
  }

  @Override
  protected void updateEntity(GeosamplesUserEntity entity, UserView view) {
    entity.setDisplayName(view.getDisplayName());

    Set<String> viewAuthorities = new HashSet<>(view.getAuthorities());

    Set<String> existing = new HashSet<>();
    for(GeosamplesUserAuthorityEntity userAuthorityEntity : entity.getUserAuthorities()) {
      existing.add(userAuthorityEntity.getAuthorityName());
    }

    Set<String> toRemove = new HashSet<>();
    for (String existingAuthority : existing) {
      if(!viewAuthorities.contains(existingAuthority)) {
        toRemove.add(existingAuthority);
      }
    }

    Iterator<GeosamplesUserAuthorityEntity> it = entity.getUserAuthorities().iterator();
    while (it.hasNext()) {
      GeosamplesUserAuthorityEntity userAuthorityEntity = it.next();
      if(toRemove.contains(userAuthorityEntity.getAuthorityName())) {
        it.remove();
      }
    }

    for (String viewAuthority : viewAuthorities) {
      if(!existing.contains(viewAuthority)) {
        entity.addUserAuthority(getUserAuthority(viewAuthority));
      }
    }

  }

  public ReadOnlySimpleItemsView<DescriptorView> getAllAuthorities() {
    ReadOnlySimpleItemsView<DescriptorView> result = new ReadOnlySimpleItemsView<>();
    result.setItems(geosamplesAuthorityRepository.findAll().stream()
        .map(GeosamplesAuthorityEntity::getAuthorityName)
        .sorted()
        .collect(Collectors.toList())
        .stream().map(a -> new DescriptorView(a, a))
        .collect(Collectors.toList())
    );
    return result;
  }

}
