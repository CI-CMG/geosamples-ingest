package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.IntervalService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderIntervalService extends ProviderServiceBase<Long, CuratorsIntervalEntity, ProviderIntervalSearchParameters, IntervalSearchParameters, ProviderIntervalView, IntervalView, CuratorsIntervalRepository> {

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
  protected boolean userCanAccessResource(String userInfo, IntervalView view) {
    if (view.getImlgs() == null || view.getImlgs().isEmpty()) {
      return false;
    }
    return curatorsSampleTsqpRepository.findByImlgs(view.getImlgs())
          .map(entity -> entity.getCruiseFacility().getFacility().getFacilityCode().equals(userInfo))
          .orElse(false);
  }

  @Override
  protected boolean userCannotModifyResource(String userInfo, IntervalView view) {
    return view.getApprovalState().equals(ApprovalState.APPROVED);
  }

  @Override
  public ApiException getIntegrityViolationException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder().error("Found conflicting interval").build()
    );
  }

  @Override
  protected IntervalView toResourceView(String userInfo, ProviderIntervalView view, @Nullable IntervalView existing) {
    if (view.getImlgs() == null || view.getImlgs().isEmpty()) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder().fieldError("imlgs", "Missing IMLGS").build()
      );
    }
    IntervalView intervalView = new IntervalView();
    intervalView.setId(view.getId());
    intervalView.setInterval(view.getInterval());
    intervalView.setDepthTop(view.getDepthTop());
    intervalView.setDepthBot(view.getDepthBot());
    intervalView.setDhCoreId(view.getDhCoreId());
    intervalView.setDhCoreLength(view.getDhCoreLength());
    intervalView.setDhCoreInterval(view.getDhCoreInterval());
    intervalView.setLithCode1(view.getLithCode1());
    intervalView.setLithCode2(view.getLithCode2());
    intervalView.setTextCode1(view.getTextCode1());
    intervalView.setTextCode2(view.getTextCode2());
    intervalView.setCompCode1(view.getCompCode1());
    intervalView.setCompCode2(view.getCompCode2());
    intervalView.setCompCode3(view.getCompCode3());
    intervalView.setCompCode4(view.getCompCode4());
    intervalView.setCompCode5(view.getCompCode5());
    intervalView.setCompCode6(view.getCompCode6());
    intervalView.setDescription(view.getDescription());
    intervalView.setAgeCodes(view.getAgeCodes());
    intervalView.setWeight(view.getWeight());
    intervalView.setRockLithCode(view.getRockLithCode());
    intervalView.setRockMinCode(view.getRockMinCode());
    intervalView.setWeathMetaCode(view.getWeathMetaCode());
    intervalView.setRemarkCode(view.getRemarkCode());
    intervalView.setMunsellCode(view.getMunsellCode());
    intervalView.setExhausted(view.getExhausted());
    intervalView.setPhotoLink(view.getPhotoLink());
    intervalView.setIntComments(view.getIntComments());
    intervalView.setIgsn(view.getIgsn());
    intervalView.setImlgs(view.getImlgs());

    if (existing != null) {
      intervalView.setApprovalState(existing.getApprovalState());
      intervalView.setPublish(existing.getPublish());
    } else {
      intervalView.setPublish(false);
    }
    if (userCanAccessResource(userInfo, intervalView)) {
      return intervalView;
    }
    throw intervalService.getNotFoundException();
  }

  @Override
  protected IntervalSearchParameters transformSearchParameters(ProviderIntervalSearchParameters searchParameters, String userInfo) {
    IntervalSearchParameters intervalSearchParameters = new IntervalSearchParameters();

    intervalSearchParameters.setFacilityCode(Collections.singletonList(userInfo)); // Important

    intervalSearchParameters.setPage(searchParameters.getPage());
    intervalSearchParameters.setItemsPerPage(searchParameters.getItemsPerPage());
    intervalSearchParameters.setOrder(searchParameters.getOrder());
    intervalSearchParameters.setInterval(searchParameters.getInterval());
    intervalSearchParameters.setImlgs(searchParameters.getImlgs());
    return intervalSearchParameters;
  }
}
