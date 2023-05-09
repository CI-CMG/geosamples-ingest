package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.IntervalService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interval")
@Validated
public class IntervalController extends ApprovalControllerBase<IntervalView, IntervalSearchParameters, Long, IntervalService> {
  @Autowired
  public IntervalController(IntervalService service) {
    super(service);
  }
}
