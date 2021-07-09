package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLithologyCode.ValidLithologyCodeValidator;
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
@Constraint(validatedBy = ValidLithologyCodeValidator.class)
@Documented
public @interface ValidLithologyCode {

  String message() default "Invalid Lithologic Composition Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidLithologyCodeValidator implements ConstraintValidator<ValidLithologyCode, String> {

    private final CuratorsLithologyRepository repository;

    @Autowired
    public ValidLithologyCodeValidator(CuratorsLithologyRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByLithologyCode(value);
    }
  }

}
