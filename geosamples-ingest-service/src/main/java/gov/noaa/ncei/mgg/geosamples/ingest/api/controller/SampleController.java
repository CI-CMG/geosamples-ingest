package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleController extends ControllerBase<SampleView, SampleSearchParameters, String, SampleService> {

  private final SampleService service;

  @Autowired
  public SampleController(SampleService service) {
    super(service);
    this.service = service;
  }

  @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public SimpleItemsView<SampleView> accept(@Valid @RequestBody SimpleItemsView<SampleView> patch) {
    return service.patch(patch);
  }
}
