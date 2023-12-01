package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelWorkbookWriter implements Closeable {

  private final SXSSFSheet singleValuedSheet;

  private final SXSSFWorkbook workbook;
  private final OutputStream outputStream;
  private int rowNum = 1;

  public ExcelWorkbookWriter(OutputStream outputStream) {
    this.workbook = new SXSSFWorkbook();
    this.workbook.setCompressTempFiles(true);

    this.singleValuedSheet = workbook.createSheet(SheetName.SINGLE_VALUE);
    this.singleValuedSheet.setRandomAccessWindowSize(100); // keep 100 rows in memory at once, the rest are kept in compressed tmp files on disk

    this.outputStream = outputStream;
  }

  public void writeHeader() {
    Row row = singleValuedSheet.createRow(0);
    int cellNum = 0;
    for (HeaderNames headerName : HeaderNames.values()) {
      if (!headerName.equals(HeaderNames.OTHER_COMPONENT_CODE)) {
        row.createCell(cellNum).setCellValue(headerName.getText());
        cellNum += 1;
      }
    }
    for (int i = 0; i < 6; i++) {
      row.createCell(cellNum).setCellValue(String.format("%s (%s)", HeaderNames.OTHER_COMPONENT_CODE.getText(), i + 1));
      cellNum += 1;
    }
  }

  public void writeToSheet(List<SampleRow> sampleRows) throws IOException {
    workbook.setActiveSheet(workbook.getSheetIndex(SheetName.SINGLE_VALUE));
    Row row;

    for (SampleRow sampleRow : sampleRows) {
      row = this.singleValuedSheet.createRow(rowNum);
      if (sampleRow.getFacilityCode() != null) {
       row.createCell(0).setCellValue(sampleRow.getFacilityCode());
      }
      if (sampleRow.getShipName() != null) {
       row.createCell(1).setCellValue(sampleRow.getShipName());
      }
      if (sampleRow.getCruiseId() != null) {
       row.createCell(2).setCellValue(sampleRow.getCruiseId());
      }
      if (sampleRow.getSampleId() != null) {
       row.createCell(3).setCellValue(sampleRow.getSampleId());
      }
      if (sampleRow.getDateCollected() != null) {
       row.createCell(4).setCellValue(sampleRow.getDateCollected());
      }
      if (sampleRow.getEndDate() != null) {
       row.createCell(5).setCellValue(sampleRow.getEndDate());
      }
      if (sampleRow.getBeginningLatitude() != null) {
       row.createCell(6).setCellValue(sampleRow.getBeginningLatitude());
      }
      if (sampleRow.getBeginningLongitude() != null) {
       row.createCell(7).setCellValue(sampleRow.getBeginningLongitude());
      }
      if (sampleRow.getEndingLatitude() != null) {
       row.createCell(8).setCellValue(sampleRow.getEndingLatitude());
      }
      if (sampleRow.getEndingLongitude() != null) {
       row.createCell(9).setCellValue(sampleRow.getEndingLongitude());
      }
      if (sampleRow.getBeginningWaterDepth() != null) {
       row.createCell(10).setCellValue(sampleRow.getBeginningWaterDepth());
      }
      if (sampleRow.getEndingWaterDepth() != null) {
       row.createCell(11).setCellValue(sampleRow.getEndingWaterDepth());
      }
      if (sampleRow.getSamplingDeviceCode() != null) {
       row.createCell(12).setCellValue(sampleRow.getSamplingDeviceCode());
      }
      if (sampleRow.getStorageMethodCode() != null) {
       row.createCell(13).setCellValue(sampleRow.getStorageMethodCode());
      }
      if (sampleRow.getCoreLength() != null) {
       row.createCell(14).setCellValue(sampleRow.getCoreLength());
      }
      if (sampleRow.getCoreDiameter() != null) {
       row.createCell(15).setCellValue(sampleRow.getCoreDiameter());
      }
      if (sampleRow.getDepthToTopOfInterval() != null) {
       row.createCell(16).setCellValue(sampleRow.getDepthToTopOfInterval());
      }
      if (sampleRow.getDepthToBottomOfInterval() != null) {
       row.createCell(17).setCellValue(sampleRow.getDepthToBottomOfInterval());
      }
      if (sampleRow.getPrimaryLithologicCompositionCode() != null) {
       row.createCell(18).setCellValue(sampleRow.getPrimaryLithologicCompositionCode());
      }
      if (sampleRow.getPrimaryTextureCode() != null) {
       row.createCell(19).setCellValue(sampleRow.getPrimaryTextureCode());
      }
      if (sampleRow.getSecondaryLithologicCompositionCode() != null) {
       row.createCell(20).setCellValue(sampleRow.getSecondaryLithologicCompositionCode());
      }
      if (sampleRow.getSecondaryTextureCode() != null) {
       row.createCell(21).setCellValue(sampleRow.getSecondaryTextureCode());
      }
      if (sampleRow.getGeologicAgeCode() != null) {
        row.createCell(22).setCellValue(
            sampleRow.getGeologicAgeCode().stream()
                .sorted(Comparator.comparing(String::toString))
                .collect(Collectors.joining(", "))
        );
      }
      if (sampleRow.getIntervalNumber() != null) {
       row.createCell(23).setCellValue(sampleRow.getIntervalNumber());
      }
      if (sampleRow.getBulkWeight() != null) {
       row.createCell(24).setCellValue(sampleRow.getBulkWeight());
      }
      if (sampleRow.getPhysiographicProvinceCode() != null) {
       row.createCell(25).setCellValue(sampleRow.getPhysiographicProvinceCode());
      }
      if (sampleRow.getSampleLithologyCode() != null) {
       row.createCell(26).setCellValue(sampleRow.getSampleLithologyCode());
      }
      if (sampleRow.getSampleMineralogyCode() != null) {
       row.createCell(27).setCellValue(sampleRow.getSampleMineralogyCode());
      }
      if (sampleRow.getSampleWeatheringOrMetamorphismCode() != null) {
       row.createCell(28).setCellValue(sampleRow.getSampleWeatheringOrMetamorphismCode());
      }
      if (sampleRow.getGlassRemarksCode() != null) {
       row.createCell(29).setCellValue(sampleRow.getGlassRemarksCode());
      }
      if (sampleRow.getMunsellColor() != null) {
       row.createCell(30).setCellValue(sampleRow.getMunsellColor());
      }
      if (sampleRow.getPrincipalInvestigator() != null) {
       row.createCell(31).setCellValue(sampleRow.getPrincipalInvestigator());
      }
      if (sampleRow.getSampleNotAvailable() != null) {
       row.createCell(32).setCellValue(sampleRow.getSampleNotAvailable());
      }
      if (sampleRow.getIgsn() != null) {
       row.createCell(33).setCellValue(sampleRow.getIgsn());
      }
      if (sampleRow.getIntervalIgsn() != null) {
        row.createCell(34).setCellValue(sampleRow.getIntervalIgsn());
      }
      if (sampleRow.getAlternateCruise() != null) {
       row.createCell(35).setCellValue(sampleRow.getAlternateCruise());
      }
      if (sampleRow.getDescription() != null) {
       row.createCell(36).setCellValue(sampleRow.getDescription());
      }

      if(sampleRow.getSampleLake() != null){
        row.createCell(37).setCellValue(sampleRow.getSampleLake());
      }
      if(sampleRow.getSampleComments() != null){
        row.createCell(38).setCellValue(sampleRow.getSampleComments());
      }
      if (sampleRow.getComments() != null) {
        row.createCell(39).setCellValue(sampleRow.getComments());
      }
      if (sampleRow.getOtherComponentCodes() != null) {
        int currentCellNum = 40;
        for (String otherComponent : sampleRow.getOtherComponentCodes()) {
          row.createCell(currentCellNum).setCellValue(otherComponent);
          currentCellNum += 1;
        }
      }

      rowNum += 1;
    }
  }

  @Override
  public void close() throws IOException {
    workbook.write(outputStream);

    workbook.dispose(); // cleans up temporary files
    workbook.close();
  }

  public static class SheetName {
    public static final String SINGLE_VALUE = "Data Output";

  }
}
