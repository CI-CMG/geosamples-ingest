package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/interval")
public class ProviderIntervalController extends ProviderControllerBase<IntervalView, IntervalView, ProviderIntervalSearchParameters, IntervalSearchParameters, Long, ProviderIntervalService> {
  @Autowired
  public ProviderIntervalController(ProviderIntervalService service) {
    super(service);
  }
}
