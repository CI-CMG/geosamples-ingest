package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TextureSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TextureView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TextureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/texture")
public class TextureController extends ControllerBase<TextureView, TextureSearchParameters, String, TextureService> {

  @Autowired
  public TextureController(TextureService service) {
    super(service);
  }
}
