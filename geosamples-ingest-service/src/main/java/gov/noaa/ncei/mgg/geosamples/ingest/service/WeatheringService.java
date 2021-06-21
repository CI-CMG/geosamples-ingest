package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.WeatheringSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.WeatheringView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
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
public class WeatheringService extends
    SearchServiceBase<CuratorsWeathMetaEntity, String, WeatheringSearchParameters, WeatheringView, CuratorsWeathMetaRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("weathering", "weathMeta");
    map.put("weatheringCode", "weathMetaCode");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsWeathMetaRepository curatorsWeathMetaRepository;

  @Autowired
  public WeatheringService(CuratorsWeathMetaRepository curatorsWeathMetaRepository) {
    this.curatorsWeathMetaRepository = curatorsWeathMetaRepository;
  }


  @Override
  protected List<Specification<CuratorsWeathMetaEntity>> getSpecs(WeatheringSearchParameters searchParameters) {
    List<Specification<CuratorsWeathMetaEntity>> specs = new ArrayList<>();

    List<String> weathering = searchParameters.getWeathering();
    List<String> weatheringCode = searchParameters.getWeatheringCode();

    if (!weathering.isEmpty()) {
      specs.add(SearchUtils.contains(weathering, CuratorsWeathMetaEntity_.WEATH_META));
    }
    if (!weatheringCode.isEmpty()) {
      specs.add(SearchUtils.equal(weatheringCode, CuratorsWeathMetaEntity_.WEATH_META_CODE));
    }

    return specs;
  }

  @Override
  protected WeatheringView toView(CuratorsWeathMetaEntity entity) {
    WeatheringView view = new WeatheringView();
    view.setWeathering(entity.getWeathMeta());
    view.setWeatheringCode(entity.getWeathMetaCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsWeathMetaEntity newEntityWithDefaultValues(String id) {
    CuratorsWeathMetaEntity entity = new CuratorsWeathMetaEntity();
    entity.setWeathMeta(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsWeathMetaEntity entity, WeatheringView view) {
    entity.setWeathMetaCode(view.getWeatheringCode());
    entity.setSourceUri(view.getSourceUri());
  }

  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsWeathMetaRepository getRepository() {
    return curatorsWeathMetaRepository;
  }

}
