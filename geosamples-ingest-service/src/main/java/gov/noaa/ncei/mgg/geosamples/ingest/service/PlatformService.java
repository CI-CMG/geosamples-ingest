package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlatformService extends
    SearchServiceBase<PlatformMasterEntity, String, PlatformSearchParameters, PlatformView, PlatformMasterRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(PlatformView.class);

  private final PlatformMasterRepository platformMasterRepository;

  @Autowired
  public PlatformService(PlatformMasterRepository platformMasterRepository) {
    this.platformMasterRepository = platformMasterRepository;
  }

  @Override
  protected List<Specification<PlatformMasterEntity>> getSpecs(PlatformSearchParameters searchParameters) {
    List<Specification<PlatformMasterEntity>> specs = new ArrayList<>();

    List<String> platform = searchParameters.getPlatform();
    List<Integer> masterId = searchParameters.getMasterId();
    List<String> icesCode = searchParameters.getIcesCode();

    if (!platform.isEmpty()) {
      specs.add(SearchUtils.contains(platform, PlatformMasterEntity_.PLATFORM));
    }
    if (!masterId.isEmpty()) {
      specs.add(SearchUtils.equal(masterId, PlatformMasterEntity_.MASTER_ID));
    }
    if (!icesCode.isEmpty()) {
      specs.add(SearchUtils.equal(icesCode, PlatformMasterEntity_.ICES_CODE));
    }

    return specs;
  }

  @Override
  protected PlatformView toView(PlatformMasterEntity entity) {
    PlatformView view = new PlatformView();
    view.setPlatform(entity.getPlatform());
    view.setMasterId(entity.getMasterId());
    view.setPrefix(entity.getPrefix());
    view.setIcesCode(entity.getIcesCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected PlatformMasterEntity newEntityWithDefaultValues(PlatformView view) {
    PlatformMasterEntity entity = new PlatformMasterEntity();
    entity.setPlatform(view.getPlatform());
    entity.setDateAdded(Instant.now());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(PlatformMasterEntity entity, PlatformView view) {
    entity.setMasterId(view.getMasterId());
    entity.setPrefix(view.getPrefix());
    entity.setIcesCode(view.getIcesCode());
    entity.setSourceUri(view.getSourceUri());
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
