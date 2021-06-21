package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockMineralSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RockMineralView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.RockMineralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rock-mineral")
public class RockMineralController extends ControllerBase<RockMineralView, RockMineralSearchParameters, String, RockMineralService> {

  @Autowired
  public RockMineralController(RockMineralService service) {
    super(service);
  }
}
