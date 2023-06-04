package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseWriteView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderCruiseService;
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
@RequestMapping("/api/v1/provider/cruise")
@Tag(name = "Provider Cruise Controller", description = "Create, read, update, and delete cruises belonging to a provider")
public class ProviderCruiseController extends ProviderControllerBase<ProviderCruiseWriteView, CruiseView, ProviderCruiseSearchParameters, CruiseSearchParameters, Long, ProviderCruiseService> {

  @Override
  protected String getAttachedToChildMessage() {
    return "Cruise cannot be deleted with samples attached";
  }

  @Autowired
  public ProviderCruiseController(ProviderCruiseService service) {
    super(service);
  }

  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Search for cruises")
  public PagedItemsView<CruiseView> search(@Valid ProviderCruiseSearchParameters searchParameters, Authentication authentication) {
    return super.search(searchParameters, authentication);
  }

  @Override
  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a cruise")
  public CruiseView get(@PathVariable("id") Long id, Authentication authentication) {
    return super.get(id, authentication);
  }

  @Override
  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a cruise's approval status")
  public ApprovalView getApproval(@PathVariable("id") Long id, Authentication authentication) {
    return super.getApproval(id, authentication);
  }

  @Override
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a cruise")
  public CruiseView create(@Valid @RequestBody ProviderCruiseWriteView view, Authentication authentication) {
    return super.create(view, authentication);
  }

  @Override
  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a cruise")
  public CruiseView update(@Valid @RequestBody ProviderCruiseWriteView view, @PathVariable("id") Long id, Authentication authentication) {
    return super.update(view, id, authentication);
  }

  @Override
  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a cruise")
  public CruiseView delete(@PathVariable("id") Long id, Authentication authentication) {
    return super.delete(id, authentication);
  }
}
