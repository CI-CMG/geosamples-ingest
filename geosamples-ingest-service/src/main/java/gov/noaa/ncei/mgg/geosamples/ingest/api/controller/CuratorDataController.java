package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CuratorDataResponse;
import gov.noaa.ncei.mgg.geosamples.ingest.service.CuratorPreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/curator-data")
public class CuratorDataController {

  private final CuratorPreviewService curatorPreviewService;

  @Autowired
  public CuratorDataController(CuratorPreviewService curatorPreviewService) {
    this.curatorPreviewService = curatorPreviewService;
  }

  @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public CuratorDataResponse upload(@RequestParam("file") MultipartFile file) {
    return curatorPreviewService.upload(file);
  }

}
