package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/platform")
public class ProviderPlatformController extends ProviderControllerBase<ProviderPlatformView, PlatformView, ProviderPlatformSearchParameters, PlatformSearchParameters, Long, ProviderPlatformService> {
  @Autowired
  public ProviderPlatformController(ProviderPlatformService service) {
    super(service);
  }

}
