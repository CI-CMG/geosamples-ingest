package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderPlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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
@RequestMapping("/api/v1/provider/platform")
@Tag(name = "Provider Platform Controller", description = "Create, read, update, and delete platforms belonging to a provider")
public class ProviderPlatformController extends ProviderControllerBase<ProviderPlatformView, PlatformView, ProviderPlatformSearchParameters, PlatformSearchParameters, Long, ProviderPlatformService> {

  private final ProviderPlatformService service;

  @Override
  protected String getAttachedToChildMessage() {
    return "Platform cannot be deleted with cruises attached";
  }

  @Autowired
  public ProviderPlatformController(ProviderPlatformService service) {
    super(service);
    this.service = service;
  }

  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Search for platforms")
  public PagedItemsView<PlatformView> search(ProviderPlatformSearchParameters searchParameters, Authentication authentication) {
    return super.search(searchParameters, authentication);
  }

  @Override
  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a platform")
  public PlatformView get(@PathVariable("id") Long id, Authentication authentication) {
    return super.get(id, authentication);
  }

  @Override
  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a platform's approval status")
  public ApprovalView getApproval(@PathVariable("id") Long id, Authentication authentication) {
    return super.getApproval(id, authentication);
  }

  @Override
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a platform")
  public PlatformView create(ProviderPlatformView view, Authentication authentication) {
    return super.create(view, authentication);
  }

  @Override
  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a platform")
  public PlatformView update(ProviderPlatformView view, @PathVariable("id") Long id, Authentication authentication) {
    return super.update(view, id, authentication);
  }

  @Override
  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a platform")
  public PlatformView delete(@PathVariable("id") Long id, Authentication authentication) {
    return super.delete(id, authentication);
  }

  @GetMapping("/unapproved")
  @Operation(summary = "Search for platforms not yet accepted into official repository")
  public PagedItemsView<PlatformView> getUnapproved(@Valid ProviderPlatformSearchParameters searchParameters, Authentication authentication) {
    return service.searchUnapproved(searchParameters, authentication);
  }

}
