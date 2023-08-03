package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalResource;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ApprovalResourceServiceBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ProviderServiceBase<I, E extends ApprovalResource<I>, PS extends PagingAndSortingParameters, S extends PS, PV, V extends PV, R extends JpaSpecificationExecutor<E> & JpaRepository<E, I>> {

  private final ApprovalResourceServiceBase<I, E, S, V, R> approvalServiceBase;

  protected final GeosamplesUserRepository geosamplesUserRepository;

  protected ProviderServiceBase(ApprovalResourceServiceBase<I, E, S, V, R> approvalResourceServiceBase,
      GeosamplesUserRepository geosamplesUserRepository) {
    this.approvalServiceBase = approvalResourceServiceBase;
    this.geosamplesUserRepository = geosamplesUserRepository;
  }

  protected abstract boolean userCanAccessResource(String userInfo, V view);
  protected abstract void throwIfUserCannotAccessResource(String userInfo, V view) throws ApiException;
  public abstract ApiException getIntegrityViolationException();
  protected abstract V toResourceView(String userInfo, PV view, @Nullable V existing);
  protected abstract S transformSearchParameters(PS searchParameters, String userInfo);

  public V create(PV view, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    V resourceView = toResourceView(userInfo, view, null);
    if (userCanAccessResource(userInfo, resourceView)) {
      return approvalServiceBase.createWithNewApproval(resourceView);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public V get(I id, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    V view = approvalServiceBase.get(id);
    if (userCanAccessResource(userInfo, view)) {
      return view;
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public PagedItemsView<V> search(PS searchParameters, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    S sampleSearchParameters = transformSearchParameters(searchParameters, userInfo);
    return approvalServiceBase.search(sampleSearchParameters);
  }

  public V update(I id, PV view, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    V existing = approvalServiceBase.get(id);
    if (userCanAccessResource(userInfo, existing)) {
      throwIfUserCannotAccessResource(userInfo, existing);
      approvalServiceBase.update(toResourceView(userInfo, view, existing), id);
      ApprovalView approvalView = new ApprovalView();
      approvalView.setApprovalState(ApprovalState.PENDING);
      return approvalServiceBase.updateApproval(approvalView, id);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public V delete(I id, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    V existing = approvalServiceBase.get(id);
    if (userCanAccessResource(userInfo, existing)) {
      throwIfUserCannotAccessResource(userInfo, existing);
      return approvalServiceBase.delete(id);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public ApprovalView getApproval(I id, Authentication authentication) {
    String userInfo = getUserInfo(authentication);
    V existing = approvalServiceBase.get(id);
    if (userCanAccessResource(userInfo, existing)) {
      return approvalServiceBase.getApproval(id);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  protected String getUserInfo(Authentication authentication) {
    CuratorsFacilityEntity facility = geosamplesUserRepository.findById(authentication.getName()).orElseThrow(
        () -> new ApiException(HttpStatus.FORBIDDEN, ApiError.builder().build())
    ).getFacility();
    if (facility == null) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error(String.format("User %s has no assigned facility", authentication.getName()))
              .build()
      );
    }
    return facility.getFacilityCode();
  }

  private ApiException getCannotEditException() {
    return new ApiException(
        HttpStatus.BAD_REQUEST,
        ApiError.builder().error("User cannot update").build()
    );
  }
}
