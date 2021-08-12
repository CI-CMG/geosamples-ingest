package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleIntervalService;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample-interval")
public class SampleIntervalController {

  private final SampleIntervalService sampleIntervalService;

  public SampleIntervalController(SampleIntervalService sampleIntervalService) {
    this.sampleIntervalService = sampleIntervalService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<CombinedSampleIntervalView> search(@Valid CombinedIntervalSampleSearchParameters searchParameters) {
    return sampleIntervalService.search(searchParameters);
  }

  @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public SimpleItemsView<CombinedSampleIntervalView> accept(@Valid @RequestBody SimpleItemsView<CombinedSampleIntervalView> patch) {
    return sampleIntervalService.patch(patch);
  }

}
