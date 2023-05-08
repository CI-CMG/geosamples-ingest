package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/sample")
public class ProviderSampleController extends ProviderControllerBase<ProviderSampleView, SampleView, ProviderSampleSearchParameters, SampleSearchParameters, String, ProviderSampleService> {
  @Autowired
  public ProviderSampleController(ProviderSampleService service) {
    super(service);
  }
}
