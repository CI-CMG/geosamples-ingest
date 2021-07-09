package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.MunsellSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.MunsellView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.MunsellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/munsell")
public class MunsellController extends ControllerBase<MunsellView, MunsellSearchParameters, String, MunsellService> {

  @Autowired
  public MunsellController(MunsellService service) {
    super(service);
  }
}
