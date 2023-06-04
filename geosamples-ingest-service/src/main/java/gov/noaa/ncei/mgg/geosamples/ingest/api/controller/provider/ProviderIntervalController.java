package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderIntervalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/interval")
@Tag(name = "Provider Subsample/Interval Controller", description = "Create, read, update, and delete subsamples/intervals belonging to a provider")
public class ProviderIntervalController extends ProviderControllerBase<ProviderIntervalView, IntervalView, ProviderIntervalSearchParameters, IntervalSearchParameters, Long, ProviderIntervalService> {

  @Override
  protected String getAttachedToChildMessage() {
    return null; // Interval has no children
  }

  @Autowired
  public ProviderIntervalController(ProviderIntervalService service) {
    super(service);
  }

  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Search for subsamples/intervals")
  public PagedItemsView<IntervalView> search(ProviderIntervalSearchParameters searchParameters, Authentication authentication) {
    return super.search(searchParameters, authentication);
  }

  @Override
  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a subsample/interval")
  public IntervalView get(@PathVariable("id") Long id, Authentication authentication) {
    return super.get(id, authentication);
  }

  @Override
  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a subsample/interval's approval status")
  public ApprovalView getApproval(@PathVariable("id") Long id, Authentication authentication) {
    return super.getApproval(id, authentication);
  }

  @Override
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a subsample/interval")
  public IntervalView create(ProviderIntervalView view, Authentication authentication) {
    return super.create(view, authentication);
  }

  @Override
  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a subsample/interval")
  public IntervalView update(ProviderIntervalView view, @PathVariable("id") Long id, Authentication authentication) {
    return super.update(view, id, authentication);
  }

  @Override
  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a subsample/interval")
  public IntervalView delete(@PathVariable("id") Long id, Authentication authentication) {
    return super.delete(id, authentication);
  }
}
