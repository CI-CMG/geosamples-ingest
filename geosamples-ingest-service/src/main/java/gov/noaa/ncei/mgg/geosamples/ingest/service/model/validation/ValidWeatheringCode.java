package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRockMineralCode.ValidRockMineralCodeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidWeatheringCode.ValidWeatheringCodeValidator;
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
@Constraint(validatedBy = ValidWeatheringCodeValidator.class)
@Documented
public @interface ValidWeatheringCode {

  String message() default "Invalid Sample Weathering or Metamorphism Code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidWeatheringCodeValidator implements ConstraintValidator<ValidWeatheringCode, String> {

    private final CuratorsWeathMetaRepository repository;

    @Autowired
    public ValidWeatheringCodeValidator(CuratorsWeathMetaRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByWeathMetaCode(value);
    }
  }

}
