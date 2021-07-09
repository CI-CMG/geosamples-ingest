package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SpreadsheetValidationException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private final Validator validator;

  @Autowired
  public ValidationService(Validator validator) {
    this.validator = validator;
  }

  public void validate(SampleRowHolder sampleRowHolder) {
    Set<ConstraintViolation<?>> violationSet = (Set) validator.validate(sampleRowHolder);
    if (!violationSet.isEmpty()) {
      throw new SpreadsheetValidationException(sampleRowHolder, violationSet);
    }
  }

}
