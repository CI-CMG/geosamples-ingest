package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsProvinceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidProvinceCode.ValidProvinceCodeValidator;
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
@Constraint(validatedBy = ValidProvinceCodeValidator.class)
@Documented
public @interface ValidProvinceCode {

  String message() default "Invalid Physiographic Province Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidProvinceCodeValidator implements ConstraintValidator<ValidProvinceCode, String> {

    private final CuratorsProvinceRepository repository;

    @Autowired
    public ValidProvinceCodeValidator(CuratorsProvinceRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByProvinceCode(value);
    }
  }

}
