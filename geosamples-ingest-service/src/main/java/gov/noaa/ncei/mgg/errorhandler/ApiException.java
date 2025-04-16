package gov.noaa.ncei.mgg.errorhandler;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final ApiError apiError;
  private final HttpStatus httpStatus;

  public ApiException(HttpStatus httpStatus, ApiError apiError) {
    super();
    this.httpStatus = httpStatus;
    this.apiError = apiError;
  }

  public ApiError getApiError() {
    return apiError;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
