package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

  private static Map<HeaderNames, List<Integer>> parseHeader(Row row) {
    Map<HeaderNames, List<Integer>> headerMap = new LinkedHashMap<>();
    DataFormatter dataFormatter = new DataFormatter(Locale.US);
    Iterator<Cell> cellIterator = row.cellIterator();
    int index = -1;
    while (cellIterator.hasNext()) {
      index++;
      Cell cell = cellIterator.next();
      String cellValue = dataFormatter.formatCellValue(cell).trim();
      for (HeaderNames headerName : HeaderNames.values()) {
        if (headerName.getMatch().matcher(cellValue).matches()) {
          List<Integer> columnIndexes = headerMap.get(headerName);
          if (columnIndexes == null) {
            columnIndexes = new ArrayList<>(1);
            headerMap.put(headerName, columnIndexes);
            columnIndexes.add(index);
          } else if (headerName.isMultiValue()) {
            columnIndexes.add(index);
          } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Column " + headerName + " is duplicated").build());
          }
        }
      }
    }
    return headerMap;
  }

  private static String parseString(String value) {
    value = value.trim();
    if (StringUtils.isBlank(value)) {
      return null;
    }
    return value;
  }

  private static Double parseDouble(String value, int rowIndex, HeaderNames headerName, int columnIndex) {
    return parseNumber(value, rowIndex, headerName, columnIndex, Double::parseDouble);
  }

  private static Integer parseInteger(String value, int rowIndex, HeaderNames headerName, int columnIndex) {
    return parseNumber(value, rowIndex, headerName, columnIndex, Integer::parseInt);
  }

  private static <N extends Number> N parseNumber(String value, int rowIndex, HeaderNames headerName, int columnIndex, Function<String, N> parse) {
    value = parseString(value);
    if (value == null) {
      return null;
    }
    try {
      return parse.apply(value);
    } catch (NumberFormatException e) {
      throw new ApiException(HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error("Value at " + headerName + ":" + (rowIndex + 1) + " was unable to be parsed: " + e.getMessage())
              .fieldError("excel[" + rowIndex + "][" + columnIndex + "]", e.getMessage())
              .build());
    }
  }

  private static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);

  private static String parseString(DataFormatter dataFormatter, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    String value = getValue(dataFormatter, headers, row, headerName);
    if (value == null) {
      return null;
    }
    return value;
  }

  private static Integer parseInteger(DataFormatter df, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    return parseInteger(getValue(df, headers, row, headerName), row.getRowNum(), headerName, headers.get(headerName).get(0));
  }

  private static Double parseDouble(DataFormatter df, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    return parseDouble(getValue(df, headers, row, headerName), row.getRowNum(), headerName, headers.get(headerName).get(0));
  }

  private static LocalDate parseLocalDate(String value, int rowIndex, HeaderNames headerName, int columnIndex) {
    value = parseString(value);
    if (value == null) {
      return null;
    }
    try {
      return LocalDate.parse(value.replaceAll("\\D", ""), DTF);
    } catch (DateTimeParseException e) {
      throw new ApiException(HttpStatus.BAD_REQUEST,
          ApiError.builder()
              .error("Value at " + headerName + ":" + (rowIndex + 1) + " was unable to be parsed: " + e.getMessage())
              .fieldError("excel[" + rowIndex + "][" + columnIndex + "]", e.getMessage())
              .build());
    }
  }

  private static LocalDate parseLocalDate(DataFormatter df, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    String value = getValue(df, headers, row, headerName);
    if (value == null) {
      return null;
    }
    int columnIndex = headers.get(headerName).get(0);
    return parseLocalDate(value, row.getRowNum(), headerName, columnIndex);
  }

  private static String getValue(DataFormatter dataFormatter, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    List<String> values = getValues(dataFormatter, headers, row, headerName);
    if (values.isEmpty()) {
      return null;
    }
    return values.get(0);
  }

  private static List<String> getValues(DataFormatter dataFormatter, Map<HeaderNames, List<Integer>> headers, Row row, HeaderNames headerName) {
    List<Integer> indexes = headers.get(headerName);
    if (indexes == null) {
      return new ArrayList<>(0);
    }
    List<String> values = new ArrayList<>(indexes.size());
    for (Integer index : indexes) {
      String value = parseString(dataFormatter.formatCellValue(row.getCell(index)));
      if (value != null) {
        values.add(value);
      }
    }
    return values;
  }

  private static SampleRow parseSampleRow(DataFormatter df, Map<HeaderNames, List<Integer>> headers, Row row) {

    SampleRow sampleRow = new SampleRow();
    sampleRow.setFacilityCode(parseString(df, headers, row, HeaderNames.FACILITY_CODE));
    sampleRow.setShipName(parseString(df, headers, row, HeaderNames.SHIP_NAME));
    sampleRow.setCruiseId(parseString(df, headers, row, HeaderNames.CRUISE_ID));
    sampleRow.setSampleId(parseString(df, headers, row, HeaderNames.SAMPLE_ID));
    sampleRow.setDateCollected(parseLocalDate(df, headers, row, HeaderNames.DATE_COLLECTED));
    sampleRow.setEndDate(parseLocalDate(df, headers, row, HeaderNames.END_DATE));
    sampleRow.setBeginningLatitude(parseDouble(df, headers, row, HeaderNames.BEGINNING_LATITUDE));
    sampleRow.setBeginningLongitude(parseDouble(df, headers, row, HeaderNames.BEGINNING_LONGITUDE));
    sampleRow.setEndingLatitude(parseDouble(df, headers, row, HeaderNames.ENDING_LATITUDE));
    sampleRow.setEndingLongitude(parseDouble(df, headers, row, HeaderNames.ENDING_LONGITUDE));
    sampleRow.setBeginningWaterDepth(parseDouble(df, headers, row, HeaderNames.BEGINNING_WATER_DEPTH));
    sampleRow.setEndingWaterDepth(parseDouble(df, headers, row, HeaderNames.ENDING_WATER_DEPTH));
    sampleRow.setSamplingDeviceCode(parseString(df, headers, row, HeaderNames.SAMPLING_DEVICE_CODE));
    sampleRow.setStorageMethodCode(parseString(df, headers, row, HeaderNames.STORAGE_METHOD_CODE));
    sampleRow.setCoreLength(parseDouble(df, headers, row, HeaderNames.CORE_LENGTH));
    sampleRow.setCoreDiameter(parseDouble(df, headers, row, HeaderNames.CORE_DIAMETER));
    sampleRow.setDepthToTopOfInterval(parseDouble(df, headers, row, HeaderNames.DEPTH_TO_TOP_OF_INTERVAL));
    sampleRow.setDepthToBottomOfInterval(parseDouble(df, headers, row, HeaderNames.DEPTH_TO_BOTTOM_OF_INTERVAL));
    sampleRow.setPrimaryLithologicCompositionCode(parseString(df, headers, row, HeaderNames.PRIMARY_LITHOLOGIC_COMPOSITION_CODE));
    sampleRow.setPrimaryTextureCode(parseString(df, headers, row, HeaderNames.PRIMARY_TEXTURE_CODE));
    sampleRow.setSecondaryLithologicCompositionCode(parseString(df, headers, row, HeaderNames.SECONDARY_LITHOLOGIC_COMPOSITION_CODE));
    sampleRow.setSecondaryTextureCode(parseString(df, headers, row, HeaderNames.SECONDARY_TEXTURE_CODE));
    sampleRow.setOtherComponentCodes(getValues(df, headers, row, HeaderNames.OTHER_COMPONENT_CODE));
    sampleRow.setGeologicAgeCode(parseString(df, headers, row, HeaderNames.GEOLOGIC_AGE_CODE));
    sampleRow.setIntervalNumber(parseInteger(df, headers, row, HeaderNames.INTERVAL_NUMBER));
    sampleRow.setBulkWeight(parseDouble(df, headers, row, HeaderNames.BULK_WEIGHT));
    sampleRow.setPhysiographicProvinceCode(parseString(df, headers, row, HeaderNames.PHYSIOGRAPHIC_PROVINCE_CODE));
    sampleRow.setSampleLithologyCode(parseString(df, headers, row, HeaderNames.SAMPLE_LITHOLOGY_CODE));
    sampleRow.setSampleMineralogyCode(parseString(df, headers, row, HeaderNames.SAMPLE_MINERALOGY_CODE));
    sampleRow.setSampleWeatheringOrMetamorphismCode(parseString(df, headers, row, HeaderNames.SAMPLE_WEATHERING_OR_METAMORPHISM_CODE));
    sampleRow.setGlassRemarksCode(parseString(df, headers, row, HeaderNames.GLASS_REMARKS_CODE));
    sampleRow.setMunsellColor(parseString(df, headers, row, HeaderNames.MUNSEL_COLOR));
    sampleRow.setPrincipalInvestigator(parseString(df, headers, row, HeaderNames.PRINCIPAL_INVESTIGATOR));
    sampleRow.setSampleNotAvailable(parseString(df, headers, row, HeaderNames.SAMPLE_NOT_AVAILABLE) != null);
    sampleRow.setIgsn(parseString(df, headers, row, HeaderNames.ISGN));
    sampleRow.setAlternateCruise(parseString(df, headers, row, HeaderNames.ALTERNATE_CRUISE_OR_LEG));
    sampleRow.setDescription(parseString(df, headers, row, HeaderNames.FREE_FORM_DESCRIPTION_OF_COMPOSITION));
    sampleRow.setComments(parseString(df, headers, row, HeaderNames.COMMENTS_ON_SUBSAMPLE_OR_INTERVAL));

    return sampleRow;
  }

  public List<SampleRow> read(InputStream in) {

    List<SampleRow> sampleRows = new ArrayList<>();

    DataFormatter df = new DataFormatter(Locale.US);

    Workbook workbook;
    try {
      workbook = WorkbookFactory.create(in);
    } catch (Exception e) {
      LOGGER.error("Unable to read file", e);
      throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to read file: " + e.getMessage()).build());
    }

    if (workbook.getNumberOfSheets() != 1) {
      throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Multiple sheets provided. Only one is supported.").build());
    }

    Sheet sheet = workbook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.rowIterator();
    Map<HeaderNames, List<Integer>> headers = null;
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      if (row.getRowNum() == 0) {
        headers = parseHeader(row);
      } else {
        sampleRows.add(parseSampleRow(df, headers, row));
      }
    }
    return sampleRows;
  }

}
