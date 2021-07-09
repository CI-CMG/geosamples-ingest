package gov.noaa.ncei.mgg.geosamples.ingest.service.model;

import java.util.Set;
import javax.validation.ConstraintViolation;

public class SpreadsheetValidationException extends RuntimeException {

  private final SampleRowHolder sampleRowHolder;
  private final Set<ConstraintViolation<?>> violations;


  public SpreadsheetValidationException(SampleRowHolder sampleRowHolder,
      Set<ConstraintViolation<?>> violations) {
    this.sampleRowHolder = sampleRowHolder;
    this.violations = violations;
  }

  public SampleRowHolder getSampleRowHolder() {
    return sampleRowHolder;
  }

  public Set<ConstraintViolation<?>> getViolations() {
    return violations;
  }
}
