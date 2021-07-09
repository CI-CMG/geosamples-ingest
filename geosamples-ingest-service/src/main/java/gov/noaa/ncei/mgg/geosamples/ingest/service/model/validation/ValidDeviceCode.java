package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidDeviceCode.ValidDeviceCodeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidDeviceCodeValidator.class)
@Documented
public @interface ValidDeviceCode {

  String message() default "Invalid Device Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidDeviceCodeValidator implements ConstraintValidator<ValidDeviceCode, String> {

    private final CuratorsDeviceRepository repository;

    @Autowired
    public ValidDeviceCodeValidator(CuratorsDeviceRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByDeviceCode(value);
    }
  }

}
