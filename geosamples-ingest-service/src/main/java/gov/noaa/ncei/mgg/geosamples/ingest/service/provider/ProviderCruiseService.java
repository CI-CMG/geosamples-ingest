package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseView;
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

public class ProviderCruiseService extends ProviderServiceBase<Long, CuratorsCruiseEntity, ProviderCruiseSearchParameters, CruiseSearchParameters, ProviderCruiseView, CruiseView, CuratorsCruiseRepository> {

  @Autowired
  protected ProviderCruiseService(
      CruiseService cruiseService,
      GeosamplesUserRepository geosamplesUserRepository) {
    super(cruiseService, geosamplesUserRepository);
  }

  @Override
  protected boolean userCanAccessResource(String userFacilityCode, CruiseView view) {
    return view.getFacilityCodes().stream().anyMatch(facilityCode -> facilityCode.equals(userFacilityCode));
  }

  @Override
  protected boolean userCannotModifyResource(String userFacilityCode, CruiseView view) {
    return view.getFacilityCodes().size() > 1 || view.getApprovalState().equals(ApprovalState.APPROVED);
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
  protected CruiseView toResourceView(String userFacilityCode, ProviderCruiseView view, @Nullable CruiseView existing) {
    CruiseView cruiseView = new CruiseView();

    cruiseView.setFacilityCodes(Collections.singletonList(userFacilityCode)); // Important

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
  protected CruiseSearchParameters transformSearchParameters(ProviderCruiseSearchParameters searchParameters, String userFacilityCode) {
    CruiseSearchParameters cruiseSearchParameters = new CruiseSearchParameters();

    cruiseSearchParameters.setFacilityCodeEquals(Collections.singletonList(userFacilityCode)); // Important

    cruiseSearchParameters.setPage(searchParameters.getPage());
    cruiseSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    cruiseSearchParameters.setOrder(searchParameters.getOrder());
    cruiseSearchParameters.setCruiseNameContains(searchParameters.getCruiseNameContains());
    cruiseSearchParameters.setCruiseNameEquals(searchParameters.getCruiseNameEquals());
    cruiseSearchParameters.setYear(searchParameters.getYear());
    cruiseSearchParameters.setPublish(searchParameters.getPublish());
    cruiseSearchParameters.setPlatformEquals(searchParameters.getPlatformEquals());
    cruiseSearchParameters.setId(searchParameters.getId());
    return cruiseSearchParameters;
  }
}
