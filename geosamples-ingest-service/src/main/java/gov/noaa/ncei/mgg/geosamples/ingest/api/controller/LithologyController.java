package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.LithologySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.LithologyView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.LithologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lithology")
public class LithologyController extends ControllerBase<LithologyView, LithologySearchParameters, String, LithologyService> {

  @Autowired
  public LithologyController(LithologyService service) {
    super(service);
  }
}
