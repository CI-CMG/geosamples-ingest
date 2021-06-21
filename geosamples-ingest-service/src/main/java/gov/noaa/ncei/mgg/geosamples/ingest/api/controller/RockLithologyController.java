package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockLithologySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockLithologyView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.RockLithologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rock-lithology")
public class RockLithologyController extends ControllerBase<RockLithologyView, RockLithologySearchParameters, String, RockLithologyService> {

  @Autowired
  public RockLithologyController(RockLithologyService service) {
    super(service);
  }
}
