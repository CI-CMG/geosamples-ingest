package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.FacilitySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.FacilityView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/facility")
public class FacilityController extends ControllerBase<FacilityView, FacilitySearchParameters, Long, FacilityService> {

  @Autowired
  public FacilityController(FacilityService service) {
    super(service);
  }
}
