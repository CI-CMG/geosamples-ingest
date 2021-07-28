package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TempSampleIntervalService;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/temp-sample-interval")
public class TempSampleIntervalController {

  private final TempSampleIntervalService tempSampleIntervalService;

  public TempSampleIntervalController(TempSampleIntervalService tempSampleIntervalService) {
    this.tempSampleIntervalService = tempSampleIntervalService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<CombinedSampleIntervalView> search(@Valid CombinedIntervalSampleSearchParameters searchParameters) {
    return tempSampleIntervalService.search(searchParameters);
  }

}
