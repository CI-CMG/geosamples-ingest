package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRemarkCode.ValidRemarkCodeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidWeatheringCode.ValidWeatheringCodeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Target({FIELD, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidRemarkCodeValidator.class)
@Documented
public @interface ValidRemarkCode {

  String message() default "Invalid Glass Remarks Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidRemarkCodeValidator implements ConstraintValidator<ValidRemarkCode, String> {

    private final CuratorsRemarkRepository repository;

    @Autowired
    public ValidRemarkCodeValidator(CuratorsRemarkRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByRemarkCode(value);
    }
  }

}
