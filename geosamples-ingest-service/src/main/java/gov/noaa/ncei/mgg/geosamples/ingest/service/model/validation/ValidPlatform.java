package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidFacilityCode.ValidFacilityCodeValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidPlatform.ValidPlatformValidator;
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
@Constraint(validatedBy = ValidPlatformValidator.class)
@Documented
public @interface ValidPlatform {

  String message() default "Invalid Ship Name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidPlatformValidator implements ConstraintValidator<ValidPlatform, String> {

    private final PlatformMasterRepository repository;

    @Autowired
    public ValidPlatformValidator(PlatformMasterRepository repository) {
      this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return repository.existsByPlatformNormalized(value);
    }
  }

}
