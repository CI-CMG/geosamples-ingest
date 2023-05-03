package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AttachedFacilityVIew;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DescriptorView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ReadOnlySimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
  private final GeosamplesRoleRepository geosamplesRoleRepository;
  private final GeosamplesAuthorityRepository geosamplesAuthorityRepository;
  private final CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  public UserService(GeosamplesUserRepository geosamplesUserRepository, GeosamplesRoleRepository geosamplesRoleRepository,
      GeosamplesAuthorityRepository geosamplesAuthorityRepository,
      CuratorsFacilityRepository curatorsFacilityRepository) {
    this.geosamplesUserRepository = geosamplesUserRepository;
    this.geosamplesRoleRepository = geosamplesRoleRepository;
    this.geosamplesAuthorityRepository = geosamplesAuthorityRepository;
    this.curatorsFacilityRepository = curatorsFacilityRepository;
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
    List<String> facilityCodeEquals = searchParameters.getFacilityCode();

    if (!userNameContains.isEmpty()) {
      specs.add(SearchUtils.contains(userNameContains, GeosamplesUserEntity_.USER_NAME));
    }

    if (!userNameEquals.isEmpty()) {
      specs.add(SearchUtils.equal(userNameEquals, GeosamplesUserEntity_.USER_NAME));
    }

    if (!displayNameContains.isEmpty()) {
      specs.add(SearchUtils.contains(displayNameContains, GeosamplesUserEntity_.DISPLAY_NAME));
    }

    if (!facilityCodeEquals.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCodeEquals, e ->
          e.join(GeosamplesUserEntity_.FACILITY).get(CuratorsFacilityEntity_.FACILITY_CODE)
      ));
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
    view.setRole(entity.getUserRole().getRoleName());

    List<String> tokenAliases = new ArrayList<>();
    tokenAliases.addAll(entity.getTokens().stream().map(GeosamplesTokenEntity::getAlias).collect(Collectors.toList()));
    Collections.sort(tokenAliases);
    view.setTokenAliases(tokenAliases);

    if (entity.getFacility() != null) {
      AttachedFacilityVIew facilityView = new AttachedFacilityVIew();
      facilityView.setId(entity.getFacility().getId());
      facilityView.setFacilityCode(entity.getFacility().getFacilityCode());
      view.setFacility(facilityView);
    }

    return view;
  }

  private GeosamplesRoleEntity getUserRole(String roleName) {
    return geosamplesRoleRepository.getByRoleName(roleName)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find role: " + roleName).build()));
  }

  private CuratorsFacilityEntity getFacility(Long facilityId) {
    return curatorsFacilityRepository.findById(facilityId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find facility: " + facilityId).build()));
  }

  @Override
  protected GeosamplesUserEntity newEntityWithDefaultValues(UserView view) {
    GeosamplesUserEntity entity = new GeosamplesUserEntity();
    entity.setVersion(0);
    entity.setUserName(view.getUserName().trim().toLowerCase(Locale.ENGLISH));
    entity.setDisplayName(view.getDisplayName().trim());
    entity.setUserRole(getUserRole(view.getRole()));
    if (view.getFacility() != null) {
      CuratorsFacilityEntity facility = getFacility(view.getFacility().getId());
      entity.setFacility(facility);
    }
    return entity;
  }

  @Override
  protected void updateEntity(GeosamplesUserEntity entity, UserView view) {
    entity.setDisplayName(view.getDisplayName());
    if (view.getRole() != null) {
      entity.setUserRole(getUserRole(view.getRole()));
    }
    if (view.getFacility() != null) {
      entity.setFacility(getFacility(view.getFacility().getId()));
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

  public ReadOnlySimpleItemsView<DescriptorView> getAllRoles() {
    ReadOnlySimpleItemsView<DescriptorView> result = new ReadOnlySimpleItemsView<>();
    result.setItems(geosamplesRoleRepository.findAll().stream()
        .map(GeosamplesRoleEntity::getRoleName)
        .sorted()
        .collect(Collectors.toList())
        .stream().map(a -> new DescriptorView(a, a))
        .collect(Collectors.toList())
    );
    return result;
  }

  public List<String> getUserAuthorities(String userName) {
    GeosamplesRoleEntity roleAuthority = geosamplesUserRepository.findById(userName)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find user: " + userName).build()))
        .getUserRole();
    if (roleAuthority == null) {
      return Collections.singletonList(Authorities.ROLE_AUTHENTICATED_USER.toString());
    }
    List<String> authorities = roleAuthority.getRoleAuthorities().stream()
          .map(GeosamplesRoleAuthorityEntity::getAuthority)
          .map(GeosamplesAuthorityEntity::getAuthorityName)
          .collect(Collectors.toList());

    authorities.add(Authorities.ROLE_AUTHENTICATED_USER.toString());
    return authorities;
  }
}
