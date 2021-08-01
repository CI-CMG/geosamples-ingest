package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleIntervalService extends
    SearchServiceBase<CuratorsIntervalEntity, IntervalPk, CombinedIntervalSampleSearchParameters, CombinedSampleIntervalView, CuratorsIntervalRepository> {



  private final CuratorsIntervalRepository curatorsIntervalRepository;

  @Autowired
  public SampleIntervalService(CuratorsIntervalRepository curatorsIntervalRepository) {
    this.curatorsIntervalRepository = curatorsIntervalRepository;
  }


  @Override
  protected List<Specification<CuratorsIntervalEntity>> getSpecs(CombinedIntervalSampleSearchParameters searchParameters) {
    return SampleIntervalUtils.getBaseSpecs(searchParameters);
  }

  @Override
  protected CombinedSampleIntervalView toView(CuratorsIntervalEntity entity) {
    return SampleIntervalUtils.toViewBase(entity);
  }

  @Override
  protected CuratorsIntervalEntity newEntityWithDefaultValues(CombinedSampleIntervalView view) {
    throw new UnsupportedOperationException("Update of CombinedSampleIntervalView is not supported");
  }

  @Override
  protected void updateEntity(CuratorsIntervalEntity entity, CombinedSampleIntervalView view) {
    throw new UnsupportedOperationException("Update of CombinedSampleIntervalView is not supported");
  }

  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return SampleIntervalUtils.SORT_MAPPING;
  }

  @Override
  protected CuratorsIntervalRepository getRepository() {
    return curatorsIntervalRepository;
  }

}
