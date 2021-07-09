package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidAgeCode.ValidAgeCodeCodeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidTextureCode.ValidTextureCodeValidator;
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
@Constraint(validatedBy = ValidAgeCodeCodeValidator.class)
@Documented
public @interface ValidAgeCode {

  String message() default "Invalid Age Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidAgeCodeCodeValidator implements ConstraintValidator<ValidAgeCode, String> {

    private final CuratorsAgeRepository repository;

    @Autowired
    public ValidAgeCodeCodeValidator(CuratorsAgeRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByAgeCode(value);
    }
  }

}
