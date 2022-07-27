package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockMineralSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockMineralView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
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
public class RockMineralService extends
    SearchServiceBase<CuratorsRockMinEntity, String, RockMineralSearchParameters, RockMineralView, CuratorsRockMinRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("rockMineral", "rockMin");
    map.put("rockMineralCode", "rockMinCode");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsRockMinRepository curatorsRockMinRepository;

  @Autowired
  public RockMineralService(CuratorsRockMinRepository curatorsRockMinRepository) {
    this.curatorsRockMinRepository = curatorsRockMinRepository;
  }

  @Override
  protected List<Specification<CuratorsRockMinEntity>> getSpecs(RockMineralSearchParameters searchParameters) {
    List<Specification<CuratorsRockMinEntity>> specs = new ArrayList<>();

    List<String> rockMineral = searchParameters.getRockMineral();
    List<String> rockMineralCode = searchParameters.getRockMineralCode();

    if (!rockMineral.isEmpty()) {
      specs.add(SearchUtils.contains(rockMineral, CuratorsRockMinEntity_.ROCK_MIN));
    }
    if (!rockMineralCode.isEmpty()) {
      specs.add(SearchUtils.equal(rockMineralCode, CuratorsRockMinEntity_.ROCK_MIN_CODE));
    }

    return specs;
  }

  @Override
  protected RockMineralView toView(CuratorsRockMinEntity entity) {
    RockMineralView view = new RockMineralView();
    view.setRockMineral(entity.getRockMin());
    view.setRockMineralCode(entity.getRockMinCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsRockMinEntity newEntityWithDefaultValues(RockMineralView view) {
    CuratorsRockMinEntity entity = new CuratorsRockMinEntity();
    entity.setRockMin(view.getRockMineral());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsRockMinEntity entity, RockMineralView view) {
    entity.setRockMinCode(view.getRockMineralCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsRockMinRepository getRepository() {
    return curatorsRockMinRepository;
  }

}
