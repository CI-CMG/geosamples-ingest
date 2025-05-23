package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderSampleService extends ProviderServiceBase<String, CuratorsSampleTsqpEntity, ProviderSampleSearchParameters, SampleSearchParameters, ProviderSampleView, SampleView, CuratorsSampleTsqpRepository> {

  @Autowired
  protected ProviderSampleService(GeosamplesUserRepository geosamplesUserRepository, SampleService sampleService) {
    super(sampleService, geosamplesUserRepository);
  }

  @Override
  protected boolean userCanAccessResource(String userInfo, SampleView view) {
    return userInfo.equals(view.getFacilityCode());
  }

  @Override
  protected void throwIfUserCannotAccessResource(String userInfo, SampleView view) throws ApiException {
    if (view.getApprovalState().equals(ApprovalState.APPROVED)) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("Cannot edit approved sample: %s", String.format("%s (%s)", view.getSample(), view.getCruise())))
              .build()
      );
    }
  }

  @Override
  public ApiException getIntegrityViolationException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder()
            .fieldError("igsn", "Invalid IGSN")
            .build()
    );
  }

  @Override
  protected SampleView toResourceView(String userInfo, ProviderSampleView view, @Nullable SampleView existing) {
    SampleView sampleView = new SampleView();

    sampleView.setFacilityCode(userInfo); // Important

    sampleView.setImlgs(view.getImlgs());
    sampleView.setCruise(view.getCruise());
    sampleView.setSample(view.getSample());
    sampleView.setPlatform(view.getPlatform());
    sampleView.setDeviceCode(view.getDeviceCode());
    sampleView.setBeginDate(view.getBeginDate());
    sampleView.setEndDate(view.getEndDate());
    sampleView.setLat(view.getLat());
    sampleView.setEndLat(view.getEndLat());
    sampleView.setLon(view.getLon());
    sampleView.setEndLon(view.getEndLon());
    sampleView.setWaterDepth(view.getWaterDepth());
    sampleView.setEndWaterDepth(view.getEndWaterDepth());
    sampleView.setStorageMethCode(view.getStorageMethCode());
    sampleView.setCoredLength(view.getCoredLength());
    sampleView.setCoredDiam(view.getCoredDiam());
    sampleView.setPi(view.getPi());
    sampleView.setProvinceCode(view.getProvinceCode());
    sampleView.setLake(view.getLake());
    sampleView.setIgsn(view.getIgsn());
    sampleView.setLeg(view.getLeg());
    sampleView.setSampleComments(view.getSampleComments());

    if (existing != null) {
      sampleView.setPublish(existing.getPublish());
      sampleView.setApprovalState(existing.getApprovalState());
      sampleView.setShowSampl(existing.getShowSampl());
    } else {
      sampleView.setPublish(false);
    }
    return sampleView;
  }

  @Override
  protected SampleSearchParameters transformSearchParameters(ProviderSampleSearchParameters searchParameters, String userInfo) {
    SampleSearchParameters sampleSearchParameters = new SampleSearchParameters();

    sampleSearchParameters.setFacilityCode(Collections.singletonList(userInfo)); // Important

    sampleSearchParameters.setPage(searchParameters.getPage());
    sampleSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    sampleSearchParameters.setOrder(searchParameters.getOrder());

    sampleSearchParameters.setImlgs(searchParameters.getImlgs());
    sampleSearchParameters.setCruise(searchParameters.getCruise());
    sampleSearchParameters.setCruiseYear(searchParameters.getCruiseYear());
    sampleSearchParameters.setSample(searchParameters.getSample());
    sampleSearchParameters.setPlatform(searchParameters.getPlatform());
    sampleSearchParameters.setDeviceCode(searchParameters.getDeviceCode());
    sampleSearchParameters.setIgsn(searchParameters.getIgsn());
    sampleSearchParameters.setBbox(searchParameters.getBbox());
    sampleSearchParameters.setApprovalState(searchParameters.getApprovalState());
    sampleSearchParameters.setPublish(searchParameters.getPublish());
    return sampleSearchParameters;
  }
}
