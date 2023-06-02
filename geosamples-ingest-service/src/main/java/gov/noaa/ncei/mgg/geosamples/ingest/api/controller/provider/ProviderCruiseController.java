package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseWriteView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderCruiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/cruise")
public class ProviderCruiseController extends ProviderControllerBase<ProviderCruiseWriteView, CruiseView, ProviderCruiseSearchParameters, CruiseSearchParameters, Long, ProviderCruiseService> {

  @Override
  protected String getAttachedToChildMessage() {
    return "Cruise cannot be deleted with samples attached";
  }

  @Autowired
  public ProviderCruiseController(ProviderCruiseService service) {
    super(service);
  }
}
