package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DeviceSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DeviceView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController extends ControllerBase<DeviceView, DeviceSearchParameters, String, DeviceService> {

  @Autowired
  public DeviceController(DeviceService service) {
    super(service);
  }
}
