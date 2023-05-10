package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
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
    SearchServiceBase<CuratorsIntervalEntity, Long, CombinedIntervalSampleSearchParameters, CombinedSampleIntervalView, CuratorsIntervalRepository> {


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

    for (CombinedSampleIntervalView del : patch.getDelete()) {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findById(del.getImlgs())
          .orElseThrow(() -> new ApiException(
          HttpStatus.NOT_FOUND,
          ApiError.builder().error("Sample " + del.getImlgs() + " was not found.")
              .build()));
      curatorsIntervalRepository.findBySampleAndInterval(sample, del.getInterval())
          .ifPresent(entity -> {
            curatorsIntervalRepository.delete(entity);
            curatorsIntervalRepository.flush();
            items.removeIf(view -> del.getImlgs().equals(view.getImlgs()) && del.getInterval().equals(view.getInterval()));
          });
    }
    for (CombinedSampleIntervalView item : patch.getItems()) {
      CuratorsSampleTsqpEntity s = curatorsSampleTsqpRepository.findById(item.getImlgs())
          .orElseThrow(() -> new ApiException(
              HttpStatus.NOT_FOUND,
              ApiError.builder().error("Sample " + item.getImlgs() + " was not found.")
                  .build()));
      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(s, item.getInterval()).orElseThrow(() -> new ApiException(
          HttpStatus.NOT_FOUND,
          ApiError.builder().error("Sample Interval " + item.getImlgs() + "-" + item.getInterval() + " was not found.")
              .build()));
      //TODO only publish is supported
      if (item.isPublish() != null) {
        if (item.isPublish()) {
          if (interval.getApproval() != null) {
            if (!interval.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
              throw new ApiException(
                  HttpStatus.BAD_REQUEST,
                  ApiError.builder().error(String.format("Interval %s (%s) is not approved", interval.getInterval(), s.getImlgs()))
                      .build());
            }
          }
        }
        interval.setPublish(item.isPublish());
        interval = curatorsIntervalRepository.saveAndFlush(interval);
        CuratorsSampleTsqpEntity sample = interval.getSample();
        // Since a sample can have multiple intervals, only allow setting the sample publish to Y as setting this to N
        // would effectively make all sibling intervals unpublished
        if (item.isPublish() && !sample.isPublish()) {
          if (sample.getApproval() != null) {
            if (!sample.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
              throw new ApiException(
                  HttpStatus.BAD_REQUEST,
                  ApiError.builder().error(String.format("Sample %s is not approved", s.getImlgs()))
                      .build());
            }
          }
          sample.setPublish(true);
          sample = curatorsSampleTsqpRepository.saveAndFlush(sample);
          interval.setSample(sample);
        }
      }
      items.add(toView(interval));
    }
    SimpleItemsView<CombinedSampleIntervalView> result = new SimpleItemsView<>();
    result.setItems(items);
    result.setDelete(patch.getDelete());
    return result;
  }

}
