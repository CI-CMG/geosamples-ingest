package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.PkValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.NoDuplicateInterval.NoDuplicateIntervalValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Target(TYPE_USE)
@Retention(RUNTIME)
@Constraint(validatedBy = NoDuplicateIntervalValidator.class)
@Documented
public @interface NoDuplicateInterval {

  String message() default "Interval already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoDuplicateIntervalValidator implements ConstraintValidator<NoDuplicateInterval, PkValidator> {

    private final CuratorsIntervalRepository curatorsIntervalRepository;
    private final TempQcIntervalRepository tempQcIntervalRepository;

    @Autowired
    public NoDuplicateIntervalValidator(CuratorsIntervalRepository curatorsIntervalRepository,
        TempQcIntervalRepository tempQcIntervalRepository) {
      this.curatorsIntervalRepository = curatorsIntervalRepository;
      this.tempQcIntervalRepository = tempQcIntervalRepository;
    }


    @Override
    public boolean isValid(PkValidator pkValidator, ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();

      boolean valid = true;
      if(!pkValidator.isAddedPk()
          || curatorsIntervalRepository.existsById(pkValidator.getPk())
          || tempQcIntervalRepository.existsById(pkValidator.getPk())
          || tempQcIntervalRepository.existsById(pkValidator.getTempPk())) {
        valid = false;
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
            .addPropertyNode("rows["+pkValidator.getIndex()+"].intervalNumber").addConstraintViolation();
      }

      return valid;
    }
  }

}
