package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
@Constraint(validatedBy = ValidLongitudeValidator.class)
@Documented
public @interface ValidLongitude {

  String message() default "Longitude must be between -180 and +180";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidLongitudeValidator implements ConstraintValidator<ValidLongitude, Double> {


    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return value >= -180D && value <= 180D;
    }
  }

}
