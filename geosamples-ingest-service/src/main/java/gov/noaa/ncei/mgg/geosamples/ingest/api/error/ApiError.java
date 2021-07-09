package gov.noaa.ncei.mgg.geosamples.ingest.api.error;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

  public void addFlashError(String error) {
    flashErrors.add(error.trim());
  }

  public void addFormError(String path, String error) {
    List<String> errors = formErrors.get(Objects.requireNonNull(path));
    if(errors == null) {
      errors = new LinkedList<>();
      formErrors.put(path, errors);
    }
    errors.add(error.trim());
  }

  public static class ApiErrorBuilder {
    private final ApiError apiError = new ApiError();

    public ApiErrorBuilder error(String error) {
      apiError.addFlashError(error);
      return this;
    }

    public ApiErrorBuilder additionalData(JsonNode data) {
      apiError.setAdditionalData(data);
      return this;
    }

    public ApiErrorBuilder fieldError(String path, String error) {
      List<String> errors = apiError.getFormErrors().get(path);
      if(errors == null) {
        errors = new ArrayList<>();
        apiError.getFormErrors().put(path, errors);
      }
      errors.add(error);
      return this;
    }

    public ApiError build() {
      return apiError;
    }

  }
}
