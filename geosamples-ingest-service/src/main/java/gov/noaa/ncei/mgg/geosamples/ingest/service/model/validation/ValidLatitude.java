package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLatitude.ValidLatitudeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLongitude.ValidLongitudeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidLatitudeValidator.class)
@Documented
public @interface ValidLatitude {

  String message() default "Latitude must be between -90 and +90";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidLatitudeValidator implements ConstraintValidator<ValidLatitude, Double> {


    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return value >= -90D && value <= 90D;
    }
  }

}
