package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidAgeCodes.ValidAgeCodesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Target({TYPE_USE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidAgeCodesValidator.class)
@Documented
public @interface ValidAgeCodes {

  String message() default "Invalid Age Codes";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidAgeCodesValidator implements ConstraintValidator<ValidAgeCodes, List<String>> {

    private final CuratorsAgeRepository repository;

    @Autowired
    public ValidAgeCodesValidator(CuratorsAgeRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return value.stream().filter(repository::existsByAgeCode).count() == value.size();
    }
  }

}
