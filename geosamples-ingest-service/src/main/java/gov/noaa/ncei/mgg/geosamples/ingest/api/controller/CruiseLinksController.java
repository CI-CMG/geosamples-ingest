package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseLinksView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.CruiseLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cruise-link")
public class CruiseLinksController extends ControllerBase<CruiseLinksView, CruiseLinksSearchParameters, Long, CruiseLinksService> {

  @Autowired
  public CruiseLinksController(CruiseLinksService service) {
    super(service);
  }

}
