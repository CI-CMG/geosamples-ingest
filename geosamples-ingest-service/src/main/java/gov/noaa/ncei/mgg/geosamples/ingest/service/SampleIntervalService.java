package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
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
public class SampleIntervalService extends
    SearchServiceBase<CuratorsIntervalEntity, IntervalPk, CombinedIntervalSampleSearchParameters, CombinedSampleIntervalView, CuratorsIntervalRepository> {



  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  public SampleIntervalService(CuratorsIntervalRepository curatorsIntervalRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository) {
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
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

  public SimpleItemsView<CombinedSampleIntervalView> patch(SimpleItemsView<CombinedSampleIntervalView> patch) {
    List<CombinedSampleIntervalView> items = new ArrayList<>(patch.getItems().size());
    for (CombinedSampleIntervalView item : patch.getItems()) {
      IntervalPk pk = new IntervalPk();
      pk.setInterval(item.getInterval());
      pk.setImlgs(item.getImlgs());
      CuratorsIntervalEntity interval = curatorsIntervalRepository.findById(pk).orElseThrow(() -> new ApiException(
          HttpStatus.NOT_FOUND,
          ApiError.builder().error("Sample Interval " + item.getImlgs() + "-" + item.getInterval() + " was not found.")
              .build()));
      //TODO only publish is supported
      if (item.isPublish() != null) {
        interval.setPublish(item.isPublish() ? "Y" : "N");
        interval = curatorsIntervalRepository.saveAndFlush(interval);
        CuratorsSampleTsqpEntity sample = interval.getParentEntity();
        if((!"Y".equals(sample.getPublish()) && item.isPublish()) || (!"N".equals(sample.getPublish()) && !item.isPublish())) {
          sample.setPublish(item.isPublish() ? "Y" : "N");
          sample = curatorsSampleTsqpRepository.saveAndFlush(sample);
          interval.setParentEntity(sample);
        }
      }
      items.add(toView(interval));
    }
    SimpleItemsView<CombinedSampleIntervalView> result = new SimpleItemsView<>();
    result.setItems(items);
    return result;
  }

}
