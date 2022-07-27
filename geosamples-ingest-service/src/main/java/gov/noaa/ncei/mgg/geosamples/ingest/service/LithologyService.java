package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.LithologySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.LithologyView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
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
public class LithologyService extends
    SearchServiceBase<CuratorsLithologyEntity, String, LithologySearchParameters, LithologyView, CuratorsLithologyRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(LithologyView.class);

  private final CuratorsLithologyRepository curatorsLithologyRepository;

  @Autowired
  public LithologyService(CuratorsLithologyRepository curatorsLithologyRepository) {
    this.curatorsLithologyRepository = curatorsLithologyRepository;
  }

  @Override
  protected List<Specification<CuratorsLithologyEntity>> getSpecs(LithologySearchParameters searchParameters) {
    List<Specification<CuratorsLithologyEntity>> specs = new ArrayList<>();

    List<String> lithology = searchParameters.getLithology();
    List<String> lithologyCode = searchParameters.getLithologyCode();

    if (!lithology.isEmpty()) {
      specs.add(SearchUtils.contains(lithology, CuratorsLithologyEntity_.LITHOLOGY));
    }
    if (!lithologyCode.isEmpty()) {
      specs.add(SearchUtils.equal(lithologyCode, CuratorsLithologyEntity_.LITHOLOGY_CODE));
    }

    return specs;
  }

  @Override
  protected LithologyView toView(CuratorsLithologyEntity entity) {
    LithologyView view = new LithologyView();
    view.setLithology(entity.getLithology());
    view.setLithologyCode(entity.getLithologyCode());
    view.setOldLithology(entity.getOldLithology());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsLithologyEntity newEntityWithDefaultValues(LithologyView view) {
    CuratorsLithologyEntity entity = new CuratorsLithologyEntity();
    entity.setLithology(view.getLithology());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsLithologyEntity entity, LithologyView view) {
    entity.setLithologyCode(view.getLithologyCode());
    entity.setOldLithology(view.getOldLithology());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsLithologyRepository getRepository() {
    return curatorsLithologyRepository;
  }

}
