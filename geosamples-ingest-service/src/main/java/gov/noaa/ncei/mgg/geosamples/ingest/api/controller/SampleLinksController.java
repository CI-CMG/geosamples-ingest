package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample-link")
public class SampleLinksController extends ControllerBase<SampleLinksView, SampleLinksSearchParameters, String, SampleLinksService> {

  @Autowired
  public SampleLinksController(SampleLinksService service) {
    super(service);
  }

}
