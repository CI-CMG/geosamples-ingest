package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RemarkSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RemarkView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/remark")
public class RemarkController extends ControllerBase<RemarkView, RemarkSearchParameters, String, RemarkService> {

  @Autowired
  public RemarkController(RemarkService service) {
    super(service);
  }
}
