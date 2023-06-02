package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseWriteView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.CruiseService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class ProviderCruiseService extends ProviderServiceBase<Long, CuratorsCruiseEntity, ProviderCruiseSearchParameters, CruiseSearchParameters, ProviderCruiseWriteView, CruiseView, CuratorsCruiseRepository> {

  @Autowired
  protected ProviderCruiseService(
      CruiseService cruiseService,
      GeosamplesUserRepository geosamplesUserRepository) {
    super(cruiseService, geosamplesUserRepository);
  }

  @Override
  protected boolean userCanAccessResource(String userInfo, CruiseView view) {
    return view.getFacilityCodes().stream().anyMatch(facilityCode -> facilityCode.equals(userInfo));
  }

  @Override
  protected void throwIfUserCannotAccessResource(String userInfo, CruiseView view) throws ApiException {
    if (view.getFacilityCodes().size() > 1) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("%s cannot be updated while it is associated with more than one facility", String.format("%s (%s)", view.getCruiseName(), view.getYear())))
              .build()
      );
    }
    if (view.getApprovalState().equals(ApprovalState.APPROVED)) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("Cannot edit approved cruise: %s", String.format("%s (%s)", view.getCruiseName(), view.getYear())))
              .build()
      );
    }
  }

  @Override
  public ApiException getIntegrityViolationException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder()
            .error("Found conflicting cruise")
            .build()
    );
  }

  @Override
  protected CruiseView toResourceView(String userInfo, ProviderCruiseWriteView view, @Nullable CruiseView existing) {
    CruiseView cruiseView = new CruiseView();

    cruiseView.setFacilityCodes(Collections.singletonList(userInfo)); // Important

    cruiseView.setId(view.getId());
    cruiseView.setYear(view.getYear());
    cruiseView.setCruiseName(view.getCruiseName());
    cruiseView.setPlatforms(view.getPlatforms());
    cruiseView.setLegs(view.getLegs());

    if (existing != null) {
      cruiseView.setPublish(existing.getPublish());
      cruiseView.setApprovalState(existing.getApprovalState());
    } else {
      cruiseView.setPublish(false);
    }
    return cruiseView;
  }

  @Override
  protected CruiseSearchParameters transformSearchParameters(ProviderCruiseSearchParameters searchParameters, String userInfo) {
    CruiseSearchParameters cruiseSearchParameters = new CruiseSearchParameters();

    cruiseSearchParameters.setFacilityCodeEquals(Collections.singletonList(userInfo)); // Important

    cruiseSearchParameters.setPage(searchParameters.getPage());
    cruiseSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    cruiseSearchParameters.setOrder(searchParameters.getOrder());
    cruiseSearchParameters.setCruiseNameContains(searchParameters.getCruiseNameContains());
    cruiseSearchParameters.setCruiseNameEquals(searchParameters.getCruiseNameEquals());
    cruiseSearchParameters.setYear(searchParameters.getYear());
    cruiseSearchParameters.setPublish(searchParameters.getPublish());
    cruiseSearchParameters.setPlatformEquals(searchParameters.getPlatformEquals());
    cruiseSearchParameters.setId(searchParameters.getId());
    cruiseSearchParameters.setApprovalState(searchParameters.getApprovalState());
    return cruiseSearchParameters;
  }
}
