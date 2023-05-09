package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalResource;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ApprovalResourceServiceBase;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ProviderServiceBase<I, E extends ApprovalResource<I>, PS extends PagingAndSortingParameters, S extends PS, PV, V extends PV, R extends JpaSpecificationExecutor<E> & JpaRepository<E, I>> {

  private final ApprovalResourceServiceBase<I, E, S, V, R> approvalServiceBase;

  private final GeosamplesUserRepository geosamplesUserRepository;

  protected ProviderServiceBase(ApprovalResourceServiceBase<I, E, S, V, R> approvalResourceServiceBase,
      GeosamplesUserRepository geosamplesUserRepository) {
    this.approvalServiceBase = approvalResourceServiceBase;
    this.geosamplesUserRepository = geosamplesUserRepository;
  }

  protected abstract boolean userCanAccessResource(String userFacilityCode, V view);
  protected abstract boolean userCannotModifyResource(String userFacilityCode, V view);
  public abstract ApiException getIntegrityViolationException();
  protected abstract V toResourceView(String userFacilityCode, PV view);
  protected abstract S transformSearchParameters(PS searchParameters, String userFacilityCode);

  public PV create(PV view, Authentication authentication) {
    String userFacilityCode = getUserFacilityCode(authentication);
    V resourceView = toResourceView(userFacilityCode, view);
    if (userCanAccessResource(userFacilityCode, resourceView)) {
      return approvalServiceBase.createWithNewApproval(toResourceView(userFacilityCode, view));
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public PV get(I id, Authentication authentication) {
    String userFacilityCode = getUserFacilityCode(authentication);
    V view = approvalServiceBase.get(id);
    if (userCanAccessResource(userFacilityCode, view)) {
      return view;
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public PagedItemsView<PV> search(PS searchParameters, Authentication authentication) {
    String userFacilityCode = getUserFacilityCode(authentication);
    S sampleSearchParameters = transformSearchParameters(searchParameters, userFacilityCode);
    PagedItemsView<V> resultPage = approvalServiceBase.search(sampleSearchParameters);
    return new PagedItemsView.Builder<PV>()
        .withItemsPerPage(resultPage.getItemsPerPage())
        .withTotalPages(resultPage.getTotalPages())
        .withPage(resultPage.getPage())
        .withTotalItems(resultPage.getTotalItems())
        .withItems(resultPage.getItems().stream().map(((i) -> (PV) i)).collect(Collectors.toList()))
        .build();
  }

  public PV update(I id, PV view, Authentication authentication) {
    String userFacilityCode = getUserFacilityCode(authentication);
    V existing = approvalServiceBase.get(id);
    if (userCanAccessResource(userFacilityCode, existing)) {
      if (userCannotModifyResource(userFacilityCode, existing)) {
        throw getCannotEditException();
      }
      return approvalServiceBase.update(toResourceView(userFacilityCode, view), id);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  public PV delete(I id, Authentication authentication) {
    String userFacilityCode = getUserFacilityCode(authentication);
    V existing = approvalServiceBase.get(id);
    if (userCanAccessResource(userFacilityCode, existing)) {
      if (userCannotModifyResource(userFacilityCode, existing)) {
        throw getCannotEditException();
      }
      return approvalServiceBase.delete(id);
    }
    throw approvalServiceBase.getNotFoundException();
  }

  private String getUserFacilityCode(Authentication authentication) {
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
