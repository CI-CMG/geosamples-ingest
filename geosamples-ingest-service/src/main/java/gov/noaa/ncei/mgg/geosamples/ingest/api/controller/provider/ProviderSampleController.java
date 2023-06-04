package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderSampleService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/sample")
@Tag(name = "Provider Sample Controller", description = "Create, read, update, and delete samples belonging to a provider")
public class ProviderSampleController extends ProviderControllerBase<ProviderSampleView, SampleView, ProviderSampleSearchParameters, SampleSearchParameters, String, ProviderSampleService> {

  @Override
  protected String getAttachedToChildMessage() {
    return null; // Samples can be deleted even if they have children, but the children will also be deleted
  }

  @Autowired
  public ProviderSampleController(ProviderSampleService service) {
    super(service);
  }

  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Search for samples")
  public PagedItemsView<SampleView> search(@Valid ProviderSampleSearchParameters searchParameters, Authentication authentication) {
    return super.search(searchParameters, authentication);
  }

  @Override
  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a sample")
  public SampleView get(@PathVariable("id") String id, Authentication authentication) {
    return super.get(id, authentication);
  }

  @Override
  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get sample's approval status")
  public ApprovalView getApproval(@PathVariable("id") String id, Authentication authentication) {
    return super.getApproval(id, authentication);
  }

  @Override
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a sample")
  public SampleView create(@Valid @RequestBody ProviderSampleView view, Authentication authentication) {
    return super.create(view, authentication);
  }

  @Override
  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a sample")
  public SampleView update(@Valid @RequestBody ProviderSampleView view, @PathVariable("id") String id, Authentication authentication) {
    return super.update(view, id, authentication);
  }

  @Override
  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a sample")
  public SampleView delete(@PathVariable("id") String id, Authentication authentication) {
    return super.delete(id, authentication);
  }
}
