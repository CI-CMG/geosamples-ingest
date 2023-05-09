package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.IntervalService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderIntervalService extends ProviderServiceBase<Long, CuratorsIntervalEntity, ProviderIntervalSearchParameters, IntervalSearchParameters, IntervalView, IntervalView, CuratorsIntervalRepository> {

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final IntervalService intervalService;

  @Autowired
  protected ProviderIntervalService(
      IntervalService intervalService,
      GeosamplesUserRepository geosamplesUserRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository) {
    super(intervalService, geosamplesUserRepository);
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.intervalService = intervalService;
  }

  @Override
  protected boolean userCanAccessResource(String userFacilityCode, IntervalView view) {
    if (view.getImlgs() == null || view.getImlgs().isEmpty()) {
      return false;
    }
    return curatorsSampleTsqpRepository.findByImlgs(view.getImlgs())
          .map(entity -> entity.getCruiseFacility().getFacility().getFacilityCode().equals(userFacilityCode))
          .orElse(false);
  }

  @Override
  protected boolean userCannotModifyResource(String userFacilityCode, IntervalView view) {
    return false;
  }

  @Override
  public ApiException getIntegrityViolationException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder().error("Found conflicting interval").build()
    );
  }

  @Override
  protected IntervalView toResourceView(String userFacilityCode, IntervalView view) {
    if (view.getImlgs() == null || view.getImlgs().isEmpty()) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder().fieldError("imlgs", "Missing IMLGS").build()
      );
    }
    if (userCanAccessResource(userFacilityCode, view)) {
      return view;
    }
    throw intervalService.getNotFoundException();
  }

  @Override
  protected IntervalSearchParameters transformSearchParameters(ProviderIntervalSearchParameters searchParameters, String userFacilityCode) {
    IntervalSearchParameters intervalSearchParameters = new IntervalSearchParameters();

    intervalSearchParameters.setFacilityCode(Collections.singletonList(userFacilityCode)); // Important

    intervalSearchParameters.setPage(searchParameters.getPage());
    intervalSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    intervalSearchParameters.setOrder(searchParameters.getOrder());
    intervalSearchParameters.setInterval(searchParameters.getInterval());
    intervalSearchParameters.setImlgs(searchParameters.getImlgs());
    return intervalSearchParameters;
  }
}
