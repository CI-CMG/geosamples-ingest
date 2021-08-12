package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
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
public class IntervalController  {

  private final IntervalService service;

  @Autowired
  public IntervalController(IntervalService service) {
    this.service = service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<IntervalView> search(@Valid IntervalSearchParameters searchParameters) {
    return service.search(searchParameters);
  }

  @GetMapping(path = "{imlgs}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public IntervalView get(@PathVariable("imlgs") String imlgs, @PathVariable("id") int id) {
    IntervalPk pk = new IntervalPk();
    pk.setInterval(id);
    pk.setImlgs(imlgs);
    return service.get(pk);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public IntervalView create(@Valid @RequestBody IntervalView view) {
    return service.create(view);
  }

  @PutMapping(path = "{imlgs}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public IntervalView update(@Valid @RequestBody IntervalView view, @PathVariable("imlgs") String imlgs, @PathVariable("id") int id) {
    IntervalPk pk = new IntervalPk();
    pk.setInterval(id);
    pk.setImlgs(imlgs);
    return service.update(view, pk);
  }

  @DeleteMapping(path = "{imlgs}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public IntervalView delete(@PathVariable("imlgs") String imlgs, @PathVariable("id") int id) {
    IntervalPk pk = new IntervalPk();
    pk.setInterval(id);
    pk.setImlgs(imlgs);
    return service.delete(pk);
  }
}
