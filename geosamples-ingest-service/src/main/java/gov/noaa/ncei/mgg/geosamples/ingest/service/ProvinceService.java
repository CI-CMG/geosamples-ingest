package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProvinceView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProvinceSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProvinceView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsProvinceRepository;
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
public class ProvinceService extends
    SearchServiceBase<CuratorsProvinceEntity, String, ProvinceSearchParameters, ProvinceView, CuratorsProvinceRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(ProvinceView.class);

  private final CuratorsProvinceRepository curatorsProvinceRepository;

  @Autowired
  public ProvinceService(CuratorsProvinceRepository curatorsProvinceRepository) {
    this.curatorsProvinceRepository = curatorsProvinceRepository;
  }
  
  @Override
  protected List<Specification<CuratorsProvinceEntity>> getSpecs(ProvinceSearchParameters searchParameters) {
    List<Specification<CuratorsProvinceEntity>> specs = new ArrayList<>();

    List<String> province = searchParameters.getProvince();
    List<String> provinceCode = searchParameters.getProvinceCode();
    List<String> provinceComment = searchParameters.getProvinceComment();

    if (!province.isEmpty()) {
      specs.add(SearchUtils.contains(province, CuratorsProvinceEntity_.PROVINCE));
    }
    if (!provinceCode.isEmpty()) {
      specs.add(SearchUtils.equal(provinceCode, CuratorsProvinceEntity_.PROVINCE_CODE));
    }
    if (!provinceComment.isEmpty()) {
      specs.add(SearchUtils.contains(provinceComment, CuratorsProvinceEntity_.PROVINCE_COMMENT));
    }

    return specs;
  }

  @Override
  protected ProvinceView toView(CuratorsProvinceEntity entity) {
    ProvinceView view = new ProvinceView();
    view.setProvince(entity.getProvince());
    view.setProvinceCode(entity.getProvinceCode());
    view.setProvinceComment(entity.getProvinceComment());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsProvinceEntity newEntityWithDefaultValues(ProvinceView view) {
    CuratorsProvinceEntity entity = new CuratorsProvinceEntity();
    entity.setProvince(view.getProvince());
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsProvinceEntity entity, ProvinceView view) {
    entity.setProvinceCode(view.getProvinceCode());
    entity.setProvinceComment(view.getProvinceComment());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsProvinceRepository getRepository() {
    return curatorsProvinceRepository;
  }

}
