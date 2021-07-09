package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLithologyCode.ValidLithologyCodeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRockLithologyCode.ValidRockLithologyCodeValidator;
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
@Constraint(validatedBy = ValidRockLithologyCodeValidator.class)
@Documented
public @interface ValidRockLithologyCode {

  String message() default "Invalid Sample Lithology Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidRockLithologyCodeValidator implements ConstraintValidator<ValidRockLithologyCode, String> {

    private final CuratorsRockLithRepository repository;

    @Autowired
    public ValidRockLithologyCodeValidator(CuratorsRockLithRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByRockLithCode(value);
    }
  }

}
