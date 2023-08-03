package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlatformService extends
    ApprovalResourceServiceBase<Long, PlatformMasterEntity, PlatformSearchParameters, PlatformView, PlatformMasterRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {

    Map<String, String> map = new HashMap<>();
    map.put("platform", PlatformMasterEntity_.PLATFORM);
    map.put("masterId", PlatformMasterEntity_.MASTER_ID);
    map.put("icesCode", PlatformMasterEntity_.ICES_CODE);
    map.put("approvalState", String.format("%s.%s", PlatformMasterEntity_.APPROVAL, GeosamplesApprovalEntity_.APPROVAL_STATE));
    viewToEntitySortMapping = Collections.unmodifiableMap(map);

  }

  private final PlatformMasterRepository platformMasterRepository;
  private final GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  public PlatformService(PlatformMasterRepository platformMasterRepository,
      GeosamplesUserRepository geosamplesUserRepository) {
    this.platformMasterRepository = platformMasterRepository;
    this.geosamplesUserRepository = geosamplesUserRepository;
  }

  @Override
  protected List<Specification<PlatformMasterEntity>> getSpecs(PlatformSearchParameters searchParameters) {
    List<Specification<PlatformMasterEntity>> specs = new ArrayList<>();

    List<Long> id = searchParameters.getId();
    List<String> platform = searchParameters.getPlatform();
    List<Integer> masterId = searchParameters.getMasterId();
    List<String> icesCode = searchParameters.getIcesCode();
    List<String> createdBy = searchParameters.getCreatedBy();
    List<ApprovalState> approvalState = searchParameters.getApprovalState();

    if (!id.isEmpty()){
      specs.add(SearchUtils.equal(id, PlatformMasterEntity_.ID));
    }
    if (!platform.isEmpty()) {
      specs.add(SearchUtils.contains(platform, PlatformMasterEntity_.PLATFORM));
    }
    if (!masterId.isEmpty()) {
      specs.add(SearchUtils.equal(masterId, PlatformMasterEntity_.MASTER_ID));
    }
    if (!icesCode.isEmpty()) {
      specs.add(SearchUtils.equal(icesCode, PlatformMasterEntity_.ICES_CODE));
    }

    if (!createdBy.isEmpty()) {
      Specification<PlatformMasterEntity> createdBySpec = SearchUtils.equal(
          createdBy.stream().filter(Objects::nonNull).collect(Collectors.toList()),
          (e) -> e.get(PlatformMasterEntity_.CREATED_BY).get(GeosamplesUserEntity_.USER_NAME)
      );
      if (createdBy.contains(null)) {
        Specification<PlatformMasterEntity> nullSpec = (e, cq, cb) -> e.get(PlatformMasterEntity_.CREATED_BY).isNull();
        specs.add(createdBySpec.or(nullSpec));
      } else {
        specs.add(createdBySpec);
      }
    }

    if (!approvalState.isEmpty()) {
      specs.add(SearchUtils.equal(
          approvalState.stream().map(ApprovalState::name).collect(Collectors.toList()),
          (e) -> e.get(CuratorsCruiseEntity_.APPROVAL).get(GeosamplesApprovalEntity_.APPROVAL_STATE)
      ));
    }

    return specs;
  }

  @Override
  protected void validateParentResourceApproval(PlatformMasterEntity entity) {
    // platforms have no parents
  }

  @Override
  protected void revokeChildResourceApproval(PlatformMasterEntity entity) {
    // platforms approval cannot be revoked
  }

  @Override
  protected void updateEntityApproval(PlatformMasterEntity entity, ApprovalView view) {
    boolean publish = entity.isPublish();
    if (entity.getApproval() != null && entity.getApproval().getApprovalState().equals(ApprovalState.APPROVED) && !view.getApprovalState().equals(ApprovalState.APPROVED)) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder().error("Cannot revoke approval for platform").build()
      );
    }
    super.updateEntityApproval(entity, view);
    entity.setPublish(publish);
    if (view.getApprovalState().equals(ApprovalState.APPROVED)) {
      entity.setCreatedBy(null); // User no longer owns this platform
    }
  }

  @Override
  protected PlatformView toView(PlatformMasterEntity entity) {
    PlatformView view = new PlatformView();
    view.setId(entity.getId());
    view.setPlatform(entity.getPlatform());
    view.setMasterId(entity.getMasterId());
    view.setPrefix(entity.getPrefix());
    view.setIcesCode(entity.getIcesCode());
    view.setSourceUri(entity.getSourceUri());
    view.setApprovalState(entity.getApproval() != null ? entity.getApproval().getApprovalState() : null);
    view.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getUserName() : null);
    return view;
  }

  @Override
  protected PlatformMasterEntity newEntityWithDefaultValues(PlatformView view) {
    PlatformMasterEntity entity = new PlatformMasterEntity();
    entity.setPlatform(view.getPlatform());
    entity.setDateAdded(Instant.now());
    entity.setPublish(true);
    if (view.getCreatedBy() != null) {
      entity.setCreatedBy(getUser(view.getCreatedBy()));
    }
    return entity;
  }

  @Override
  protected void updateEntity(PlatformMasterEntity entity, PlatformView view) {
    entity.setId(view.getId());
    entity.setMasterId(view.getMasterId());
    entity.setPrefix(view.getPrefix());
    entity.setIcesCode(view.getIcesCode());
    entity.setSourceUri(view.getSourceUri());
  }

  private GeosamplesUserEntity getUser(String username) {
    return geosamplesUserRepository.findById(username).orElseThrow(
        () -> new ApiException(
            HttpStatus.NOT_FOUND,
            ApiError.builder().error(String.format("User not found: %s", username)).build()
        )
    );
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected PlatformMasterRepository getRepository() {
    return platformMasterRepository;
  }

}
