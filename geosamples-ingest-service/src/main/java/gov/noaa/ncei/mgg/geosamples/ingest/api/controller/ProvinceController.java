package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProvinceSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProvinceView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/province")
public class ProvinceController extends ControllerBase<ProvinceView, ProvinceSearchParameters, String, ProvinceService> {

  @Autowired
  public ProvinceController(ProvinceService service) {
    super(service);
  }
}
