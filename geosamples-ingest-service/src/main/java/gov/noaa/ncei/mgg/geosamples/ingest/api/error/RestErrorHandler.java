package gov.noaa.ncei.mgg.geosamples.ingest.api.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiError.ApiErrorBuilder;
import gov.noaa.ncei.mgg.errorhandler.ErrorHandler;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SpreadsheetValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestErrorHandler extends ErrorHandler {

  private final ObjectMapper objectMapper;

  @Autowired
  public RestErrorHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(SpreadsheetValidationException.class)
  public ResponseEntity<ApiError> handleSpreadsheetValidationException(SpreadsheetValidationException ex) {
    ApiErrorBuilder errorBuilder = buildFromViolations(ex.getViolations(), true);
    errorBuilder.additionalData(objectMapper.convertValue(ex.getSampleRowHolder(), JsonNode.class));
    return new ResponseEntity<>(errorBuilder.build(), HttpStatus.BAD_REQUEST);
  }

}
