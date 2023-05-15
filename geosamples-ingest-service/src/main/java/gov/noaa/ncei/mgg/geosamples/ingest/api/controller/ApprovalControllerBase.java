package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ApprovalResourceServiceBase;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public class ApprovalControllerBase<V, S extends PagingAndSortingParameters, I, T extends ApprovalResourceServiceBase<I, ?, S, V, ?>> extends ControllerBase<V, S, I, T> {

  protected final T service;

  public ApprovalControllerBase(T service) {
    super(service);
    this.service = service;
  }

  @PatchMapping(path = "/review/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public V review(@PathVariable I id, @Valid @RequestBody ApprovalView approval) {
    return service.updateApproval(approval, id);
  }

  @GetMapping(path = "/approval/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApprovalView getApproval(@PathVariable I id) {
    return service.getApproval(id);
  }
}
