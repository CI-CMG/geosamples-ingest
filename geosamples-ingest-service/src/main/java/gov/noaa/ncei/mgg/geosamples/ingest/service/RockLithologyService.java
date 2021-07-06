package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockLithologySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockLithologyView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RockLithologyService extends
    SearchServiceBase<CuratorsRockLithEntity, String, RockLithologySearchParameters, RockLithologyView, CuratorsRockLithRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("rockLithology", "rockLith");
    map.put("rockLithologyCode", "rockLithCode");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsRockLithRepository curatorsRockLithRepository;

  @Autowired
  public RockLithologyService(CuratorsRockLithRepository curatorsRockLithRepository) {
    this.curatorsRockLithRepository = curatorsRockLithRepository;
  }

  @Override
  protected List<Specification<CuratorsRockLithEntity>> getSpecs(RockLithologySearchParameters searchParameters) {
    List<Specification<CuratorsRockLithEntity>> specs = new ArrayList<>();

    List<String> rockLithology = searchParameters.getRockLithology();
    List<String> rockLithologyCode = searchParameters.getRockLithologyCode();

    if (!rockLithology.isEmpty()) {
      specs.add(SearchUtils.contains(rockLithology, CuratorsRockLithEntity_.ROCK_LITH));
    }
    if (!rockLithologyCode.isEmpty()) {
      specs.add(SearchUtils.equal(rockLithologyCode, CuratorsRockLithEntity_.ROCK_LITH_CODE));
    }

    return specs;
  }

  @Override
  protected RockLithologyView toView(CuratorsRockLithEntity entity) {
    RockLithologyView view = new RockLithologyView();
    view.setRockLithology(entity.getRockLith());
    view.setRockLithologyCode(entity.getRockLithCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsRockLithEntity newEntityWithDefaultValues(RockLithologyView view) {
    CuratorsRockLithEntity entity = new CuratorsRockLithEntity();
    entity.setRockLith(view.getRockLithology());
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsRockLithEntity entity, RockLithologyView view) {
    entity.setRockLithCode(view.getRockLithologyCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsRockLithRepository getRepository() {
    return curatorsRockLithRepository;
  }

}
