package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.StorageMethodSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.StorageMethodView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.StorageMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage-method")
public class StorageMethodController extends ControllerBase<StorageMethodView, StorageMethodSearchParameters, String, StorageMethodService> {

  @Autowired
  public StorageMethodController(StorageMethodService service) {
    super(service);
  }
}
