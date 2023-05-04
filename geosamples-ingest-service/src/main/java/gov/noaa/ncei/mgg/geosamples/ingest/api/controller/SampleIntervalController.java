package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ExcelWorkbookWriter;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleIntervalService;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

  @GetMapping(path = "/export")
  public void export(HttpServletResponse response, @Valid CombinedIntervalSampleSearchParameters searchParameters) {
    searchParameters.setItemsPerPage(1000);

    PagedItemsView<CombinedSampleIntervalView> page = sampleIntervalService.search(searchParameters);
    if (page.getTotalItems() == 0) {
      throw new ApiException(
          HttpStatus.BAD_REQUEST,
          ApiError.builder().error("Query returned no results to export").build()
      );
    }
    response.setContentType(new MediaType("application", "vnd.ms-excel").toString());
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx");
    try (
        OutputStream outputStream = response.getOutputStream();
        ExcelWorkbookWriter excelWorkBookWriter = new ExcelWorkbookWriter(outputStream)
    ) {
      while (page.getPage() <= page.getTotalPages()) {
        excelWorkBookWriter.writeToSheet(
            page.getItems().stream()
                .map(SampleIntervalController::toSampleRow)
                .collect(Collectors.toList())
        );

        searchParameters.setPage(searchParameters.getPage() + 1);
        page = sampleIntervalService.search(searchParameters);
      }
      outputStream.flush();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to export to file");
    }
  }

  private static SampleRow toSampleRow(CombinedSampleIntervalView view) {
    SampleRow sampleRow = new SampleRow();
    sampleRow.setFacilityCode(view.getFacility());
    sampleRow.setShipName(view.getPlatform());
    sampleRow.setCruiseId(view.getCruise());
    sampleRow.setSampleId(view.getSample());
    sampleRow.setDateCollected(view.getBeginDate());
    sampleRow.setEndDate(view.getEndDate());
    sampleRow.setBeginningLatitude(view.getLat());
    sampleRow.setBeginningLongitude(view.getLon());
    sampleRow.setEndingLatitude(view.getEndLat());
    sampleRow.setEndingLongitude(view.getEndLon());
    if (view.getWaterDepth() != null) {
      sampleRow.setBeginningWaterDepth(Double.valueOf(view.getWaterDepth()));
    }
    if (view.getEndWaterDepth() != null) {
      sampleRow.setEndingWaterDepth(Double.valueOf(view.getEndWaterDepth()));
    }
    sampleRow.setSamplingDeviceCode(view.getDevice());
    sampleRow.setStorageMethodCode(view.getStorageMeth());
    if (view.getCoredLength() != null) {
      sampleRow.setCoreLength(Double.valueOf(view.getCoredLength()));
    }
    if (view.getCoredDiam() != null) {
      sampleRow.setCoreDiameter(Double.valueOf(view.getCoredDiam()));
    }
    if (view.getDepthTop() != null) {
      sampleRow.setDepthToTopOfInterval(Double.valueOf(view.getDepthTop()));
    }
    if (view.getDepthBot() != null) {
      sampleRow.setDepthToBottomOfInterval(Double.valueOf(view.getDepthBot()));
    }
    sampleRow.setPrimaryLithologicCompositionCode(view.getLith1());
    sampleRow.setPrimaryTextureCode(view.getText1());
    sampleRow.setSecondaryLithologicCompositionCode(view.getLith2());
    sampleRow.setSecondaryTextureCode(view.getText2());
    sampleRow.setOtherComponentCodes(
        Stream.of(
        view.getComp1(), view.getComp2(), view.getComp3(), view.getComp4(), view.getComp5(), view.getComp6()
        ).filter(c -> c != null && !c.isEmpty())
            .collect(Collectors.toList())
    );
    sampleRow.setGeologicAgeCode(view.getAges());
    sampleRow.setIntervalNumber(view.getInterval());
    sampleRow.setBulkWeight(view.getWeight());
    sampleRow.setPhysiographicProvinceCode(view.getProvince());
    sampleRow.setSampleLithologyCode(view.getRockLith());
    sampleRow.setSampleMineralogyCode(view.getRockMin());
    sampleRow.setSampleWeatheringOrMetamorphismCode(view.getWeathMeta());
    sampleRow.setGlassRemarksCode(view.getRemark());
    sampleRow.setMunsellColor(view.getMunsellCode());
    sampleRow.setPrincipalInvestigator(view.getPi());
    sampleRow.setSampleNotAvailable(view.getExhaustCode());
    sampleRow.setIgsn(view.getIgsn());
    sampleRow.setAlternateCruise(view.getLeg());
    sampleRow.setDescription(view.getDescription());
    sampleRow.setComments(view.getIntComments());
    return sampleRow;
  }

}
