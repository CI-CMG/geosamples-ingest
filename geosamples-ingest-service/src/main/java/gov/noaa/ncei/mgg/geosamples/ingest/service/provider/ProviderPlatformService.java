package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.PlatformService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderPlatformService extends ProviderServiceBase<Long, PlatformMasterEntity, ProviderPlatformSearchParameters, PlatformSearchParameters, ProviderPlatformView, PlatformView, PlatformMasterRepository> {

  private final PlatformService platformService;
  @Autowired
  protected ProviderPlatformService(
      PlatformService platformService,
      GeosamplesUserRepository geosamplesUserRepository) {
    super(platformService, geosamplesUserRepository);
    this.platformService = platformService;
  }

  @Override
  protected String getUserInfo(Authentication authentication) {
    GeosamplesUserEntity user = geosamplesUserRepository.findById(authentication.getName()).orElseThrow(
        () -> new ApiException(
            HttpStatus.NOT_FOUND,
            ApiError.builder().error(String.format("User not found: %s", authentication.getName())).build()
        )
    );
    return user.getUserName();
  }

  @Override
  protected boolean userCanAccessResource(String userInfo, PlatformView view) {
    return view.getCreatedBy() != null && view.getCreatedBy().equals(userInfo);
  }

  @Override
  protected void throwIfUserCannotAccessResource(String userInfo, PlatformView view) throws ApiException {
    if (view.getCreatedBy() == null) {
      throw new ApiException(
          HttpStatus.NOT_FOUND,
          ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build()
      );
    }

    if (view.getApprovalState() == null) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("Cannot edit platform without approval state: %s", view.getPlatform()))
              .build()
      );
    }

    if (view.getApprovalState().equals(ApprovalState.APPROVED)) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("Cannot edit approved platform: %s", view.getPlatform()))
              .build()
      );
    }

  }

  @Override
  public ApiException getIntegrityViolationException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder()
            .fieldError("platform", "Invalid platform name")
            .build()
    );
  }

  @Override
  protected PlatformView toResourceView(String userInfo, ProviderPlatformView view, @Nullable PlatformView existing) {
    PlatformView platformView = new PlatformView();
    platformView.setId(view.getId());
    platformView.setPlatform(view.getPlatform());
    platformView.setMasterId(view.getMasterId());
    platformView.setPrefix(view.getPrefix());
    platformView.setIcesCode(view.getIcesCode());
    platformView.setSourceUri(view.getSourceUri());

    platformView.setCreatedBy(userInfo); // Important

    if (existing != null) {
      platformView.setApprovalState(existing.getApprovalState());
    }

    return platformView;
  }

  @Override
  protected PlatformSearchParameters transformSearchParameters(ProviderPlatformSearchParameters searchParameters, String userInfo) {
    PlatformSearchParameters platformSearchParameters = new PlatformSearchParameters();

    List<String> createdBy = new ArrayList<>();
    createdBy.add(userInfo);
    createdBy.add(null);
    platformSearchParameters.setCreatedBy(createdBy); // Important

    platformSearchParameters.setPage(searchParameters.getPage());
    platformSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    platformSearchParameters.setOrder(searchParameters.getOrder());
    platformSearchParameters.setPlatform(searchParameters.getPlatform());
    platformSearchParameters.setMasterId(searchParameters.getMasterId());
    platformSearchParameters.setIcesCode(searchParameters.getIcesCode());
    platformSearchParameters.setId(searchParameters.getId());
    platformSearchParameters.setApprovalState(searchParameters.getApprovalState());
    return platformSearchParameters;
  }

  public PagedItemsView<PlatformView> searchUnapproved(ProviderPlatformSearchParameters searchParameters, Authentication authentication) {
    PlatformSearchParameters platformSearchParameters = transformSearchParameters(searchParameters, authentication.getName());
    platformSearchParameters.setCreatedBy(Collections.singletonList(authentication.getName()));
    return platformService.search(platformSearchParameters);
  }
}
