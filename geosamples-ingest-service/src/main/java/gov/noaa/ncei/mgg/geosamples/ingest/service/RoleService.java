package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService extends SearchServiceBase<GeosamplesRoleEntity, Long, RoleSearchParameters, RoleView, GeosamplesRoleRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(RoleView.class);

  private final GeosamplesRoleRepository geosamplesRoleRepository;
  private final GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  public RoleService(GeosamplesRoleRepository geosamplesRoleRepository,
      GeosamplesAuthorityRepository geosamplesAuthorityRepository) {
    this.geosamplesRoleRepository = geosamplesRoleRepository;
    this.geosamplesAuthorityRepository = geosamplesAuthorityRepository;
  }

  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected List<Specification<GeosamplesRoleEntity>> getSpecs(RoleSearchParameters searchParameters) {
    List<Specification<GeosamplesRoleEntity>> specifications = new ArrayList<>();

    List<String> roleNameContains = searchParameters.getRoleNameContains();

    if (roleNameContains != null && !roleNameContains.isEmpty()) {
      specifications.add(SearchUtils.contains(roleNameContains, GeosamplesRoleEntity_.ROLE_NAME));
    }

    return specifications;
  }

  @Override
  protected GeosamplesRoleRepository getRepository() {
    return geosamplesRoleRepository;
  }

  @Override
  protected RoleView toView(GeosamplesRoleEntity entity) {
    RoleView view = new RoleView();
    view.setId(entity.getId());
    view.setVersion(entity.getVersion());
    view.setRoleName(entity.getRoleName());
    view.setAuthorities(
        entity.getRoleAuthorities().stream()
            .map(roleAuthority -> roleAuthority.getAuthority().getAuthorityName())
            .collect(java.util.stream.Collectors.toList())
    );
    return view;
  }

  @Override
  protected GeosamplesRoleEntity newEntityWithDefaultValues(RoleView view) {
    GeosamplesRoleEntity entity = new GeosamplesRoleEntity();
    entity.setRoleName(view.getRoleName());
    entity = geosamplesRoleRepository.save(entity);
    for (String a : view.getAuthorities()) {
      GeosamplesRoleAuthorityEntity roleAuthority = new GeosamplesRoleAuthorityEntity();
      roleAuthority.setAuthority(
          geosamplesAuthorityRepository.findById(a)
              .orElseThrow(() -> new ApiException(
                  HttpStatus.NOT_FOUND,
                  ApiError.builder().error(String.format("Authority not found: %s",  a)).build()
              ))
      );
      entity.addRoleAuthority(roleAuthority);
    }

    return entity;
  }

  @Override
  protected void updateEntity(GeosamplesRoleEntity entity, RoleView view) {
    entity.setRoleName(view.getRoleName());
    entity.clearRoleAuthorities();
    for (String a : view.getAuthorities()) {
      GeosamplesRoleAuthorityEntity roleAuthority = new GeosamplesRoleAuthorityEntity();
      roleAuthority.setAuthority(
          geosamplesAuthorityRepository.findById(a)
              .orElseThrow(() -> new ApiException(
                  HttpStatus.NOT_FOUND,
                  ApiError.builder().error(String.format("Authority not found: %s",  a)).build()
              ))
      );
      entity.addRoleAuthority(roleAuthority);
    }
    geosamplesRoleRepository.save(entity);
  }
}
