package gov.noaa.ncei.mgg.errorhandler;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiError {

  public static ApiErrorBuilder builder() {
    return new ApiErrorBuilder();
  }

  private final List<String> flashErrors = new ArrayList<>();
  private final Map<String, List<String>> formErrors = new HashMap<>();
  private JsonNode additionalData = null;

  public JsonNode getAdditionalData() {
    return additionalData;
  }

  public void setAdditionalData(JsonNode additionalData) {
    this.additionalData = additionalData;
  }

  public List<String> getFlashErrors() {
    return flashErrors;
  }

  public Map<String, List<String>> getFormErrors() {
    return formErrors;
  }

  public static class ApiErrorBuilder {
    private final ApiError apiError = new ApiError();

    public ApiErrorBuilder error(String error) {
      apiError.getFlashErrors().add(error);
      return this;
    }

    public ApiErrorBuilder additionalData(JsonNode data) {
      apiError.setAdditionalData(data);
      return this;
    }

    public ApiErrorBuilder fieldError(String path, String error) {
      List<String> errors = apiError.getFormErrors().computeIfAbsent(path, k -> new ArrayList<>());
      errors.add(error);
      return this;
    }

    public ApiError build() {
      return apiError;
    }

  }
}
