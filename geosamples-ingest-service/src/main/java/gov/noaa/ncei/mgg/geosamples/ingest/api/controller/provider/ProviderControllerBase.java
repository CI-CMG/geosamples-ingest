package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderServiceBase;
import javax.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public abstract class ProviderControllerBase<PV, V extends PV, PS extends PagingAndSortingParameters, S extends PS, I, T extends ProviderServiceBase<I, ?, PS, S, PV, V, ?>> {

  protected final T service;

  protected abstract String getAttachedToChildMessage();

  public ProviderControllerBase(T service) {
    this.service = service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<V> search(@Valid PS searchParameters, Authentication authentication) {
    return service.search(searchParameters, authentication);
  }

  private I replaceSlash(I id) {
    if(id instanceof String) {
      return (I) ((String) id).replaceAll("\\*\\|\\*", "/");
    }
    return id;
  }

  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public V get(@PathVariable("id") I id, Authentication authentication) {
    return service.get(replaceSlash(id), authentication);
  }

  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApprovalView getApproval(@PathVariable("id") I id, Authentication authentication) {
    return service.getApproval(replaceSlash(id), authentication);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public V create(@Valid @RequestBody PV view, Authentication authentication) {
    try {
      return service.create(view, authentication);
    } catch (DataIntegrityViolationException e) {
      throw service.getIntegrityViolationException();
    }
  }

  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public V update(@Valid @RequestBody PV view, @PathVariable("id") I id, Authentication authentication) {
    try {
      return service.update(replaceSlash(id), view, authentication);
    } catch (DataIntegrityViolationException e) {
      throw service.getIntegrityViolationException();
    }
  }

  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public V delete(@PathVariable("id") I id, Authentication authentication) {
    try {
      return service.delete(replaceSlash(id), authentication);
    } catch (DataIntegrityViolationException e) {
      Throwable mostSpecificCause = e.getMostSpecificCause();
      if (mostSpecificCause.getMessage() != null && mostSpecificCause.getMessage().contains("child record found")) {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ApiError.builder()
                .error(getAttachedToChildMessage())
                .build()
        );
      } else {
        throw service.getIntegrityViolationException();
      }
    }
  }

}
