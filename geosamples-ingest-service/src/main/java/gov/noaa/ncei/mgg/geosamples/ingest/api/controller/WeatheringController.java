package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.WeatheringSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.WeatheringView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.WeatheringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weathering")
public class WeatheringController extends ControllerBase<WeatheringView, WeatheringSearchParameters, String, WeatheringService> {

  @Autowired
  public WeatheringController(WeatheringService service) {
    super(service);
  }
}
