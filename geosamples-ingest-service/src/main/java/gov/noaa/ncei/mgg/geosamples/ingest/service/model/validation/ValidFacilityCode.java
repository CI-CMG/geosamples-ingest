package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidFacilityCode.ValidFacilityCodeValidator;
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
@Constraint(validatedBy = ValidFacilityCodeValidator.class)
@Documented
public @interface ValidFacilityCode {

  String message() default "Invalid Facility Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidFacilityCodeValidator implements ConstraintValidator<ValidFacilityCode, String> {

    private final CuratorsFacilityRepository repository;

    @Autowired
    public ValidFacilityCodeValidator(CuratorsFacilityRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsById(value);
    }
  }

}
