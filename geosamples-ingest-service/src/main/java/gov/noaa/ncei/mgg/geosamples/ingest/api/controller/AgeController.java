package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AgeSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AgeView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.AgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/age")
public class AgeController extends ControllerBase<AgeView, AgeSearchParameters, String, AgeService> {

  @Autowired
  public AgeController(AgeService service) {
    super(service);
  }
}
