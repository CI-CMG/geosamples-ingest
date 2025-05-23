package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CuratorDataResponse;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CuratorPreviewService {

  private final ExcelInputService excelInputService;
  private final ValidationService validationService;
  private final CuratorPreviewPersistenceService curatorPreviewPersistenceService;

  @Autowired
  public CuratorPreviewService(ExcelInputService excelInputService, ValidationService validationService,
      CuratorPreviewPersistenceService curatorPreviewPersistenceService) {
    this.excelInputService = excelInputService;
    this.validationService = validationService;
    this.curatorPreviewPersistenceService = curatorPreviewPersistenceService;
  }

  public CuratorDataResponse upload(MultipartFile file) {
    List<SampleRow> samples;
    try(InputStream in = file.getInputStream()) {
      samples = excelInputService.read(in);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file", e);
    }
    return upload(new SampleRowHolder(samples));
  }

  public CuratorDataResponse upload(SampleRowHolder sampleRowHolder) {
    validationService.validate(sampleRowHolder);
    List<SampleRow> rows = sampleRowHolder.getRows();
    curatorPreviewPersistenceService.save(sampleRowHolder);
    TreeSet<String> platforms = new TreeSet<>();
    TreeSet<String> facilityCodes = new TreeSet<>();
    TreeSet<String> cruises = new TreeSet<>();
    for(SampleRow row : rows) {
      platforms.add(row.getShipName());
      cruises.add(row.getCruiseId());
      facilityCodes.add(row.getFacilityCode());
    }

    CuratorDataResponse response = new CuratorDataResponse();
    response.setCruises(new ArrayList<>(cruises));
    response.setPlatforms(new ArrayList<>(platforms));
    response.setFacilityCodes(new ArrayList<>(facilityCodes));
    return response;
  }


}
