package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.service.ExcelService;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidDate.ValidDateValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
@Documented
public @interface ValidDate {

  String message() default "Invalid date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidDateValidator implements ConstraintValidator<ValidDate, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      DateTimeFormatter dtf;
      switch (value.length()) {
        case 8:
          dtf = ExcelService.DTF_YMD;
          break;
        case 6:
          dtf = ExcelService.DTF_YM;
          break;
        case 4:
          dtf = ExcelService.DTF_Y;
          break;
        default:
          return false;
      }
      try {
        LocalDate.parse(value, dtf);
      } catch (DateTimeParseException e) {
        return false;
      }
      return true;
    }
  }

}
