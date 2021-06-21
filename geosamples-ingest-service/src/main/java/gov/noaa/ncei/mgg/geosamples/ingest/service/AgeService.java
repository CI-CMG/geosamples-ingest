package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AgeSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AgeView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgeService extends
    SearchServiceBase<CuratorsAgeEntity, String, AgeSearchParameters, AgeView, CuratorsAgeRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(AgeView.class);

  private final CuratorsAgeRepository curatorsAgeRepository;

  @Autowired
  public AgeService(CuratorsAgeRepository curatorsAgeRepository) {
    this.curatorsAgeRepository = curatorsAgeRepository;
  }

  @Override
  protected List<Specification<CuratorsAgeEntity>> getSpecs(AgeSearchParameters searchParameters) {
    List<Specification<CuratorsAgeEntity>> specs = new ArrayList<>();

    List<String> age = searchParameters.getAge();
    List<String> ageCode = searchParameters.getAgeCode();

    if (!age.isEmpty()) {
      specs.add(SearchUtils.contains(age, CuratorsAgeEntity_.AGE));
    }
    if (!ageCode.isEmpty()) {
      specs.add(SearchUtils.equal(ageCode, CuratorsAgeEntity_.AGE_CODE));
    }

    return specs;
  }

  @Override
  protected AgeView toView(CuratorsAgeEntity entity) {
    AgeView view = new AgeView();
    view.setAge(entity.getAge());
    view.setAgeCode(entity.getAgeCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsAgeEntity newEntityWithDefaultValues(String id) {
    CuratorsAgeEntity entity = new CuratorsAgeEntity();
    entity.setAge(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsAgeEntity entity, AgeView view) {
    entity.setAgeCode(view.getAgeCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsAgeRepository getRepository() {
    return curatorsAgeRepository;
  }

}
