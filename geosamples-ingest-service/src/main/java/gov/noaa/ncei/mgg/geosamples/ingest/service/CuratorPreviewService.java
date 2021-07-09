package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CuratorDataResponse;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CuratorPreviewService {

  private final ExcelService excelService;
  private final ValidationService validationService;
  private final CuratorPreviewPersistenceService curatorPreviewPersistenceService;

  @Autowired
  public CuratorPreviewService(ExcelService excelService, ValidationService validationService,
      CuratorPreviewPersistenceService curatorPreviewPersistenceService) {
    this.excelService = excelService;
    this.validationService = validationService;
    this.curatorPreviewPersistenceService = curatorPreviewPersistenceService;
  }

  public CuratorDataResponse upload(MultipartFile file) {
    List<SampleRow> samples;
    try(InputStream in = file.getInputStream()) {
      samples = excelService.read(in);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file", e);
    }
    return upload(new SampleRowHolder(samples));
  }

  public CuratorDataResponse upload(SampleRowHolder sampleRowHolder) {
    validationService.validate(sampleRowHolder);
    curatorPreviewPersistenceService.save(sampleRowHolder.getRows());
    return new CuratorDataResponse();
  }


}
