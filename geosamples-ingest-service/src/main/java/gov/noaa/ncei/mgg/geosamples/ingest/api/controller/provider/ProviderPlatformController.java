package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderPlatformService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/platform")
public class ProviderPlatformController extends ProviderControllerBase<ProviderPlatformView, PlatformView, ProviderPlatformSearchParameters, PlatformSearchParameters, Long, ProviderPlatformService> {

  private final ProviderPlatformService service;
  @Autowired
  public ProviderPlatformController(ProviderPlatformService service) {
    super(service);
    this.service = service;
  }

  @GetMapping("/unapproved")
  public PagedItemsView<PlatformView> getUnapproved(@Valid ProviderPlatformSearchParameters searchParameters, Authentication authentication) {
    return service.searchUnapproved(searchParameters, authentication);
  }

}
