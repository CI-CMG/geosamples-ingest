package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcSampleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.NoDuplicateIgsn.NoDuplicateIgsnValidator;
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
@Constraint(validatedBy = NoDuplicateIgsnValidator.class)
@Documented
public @interface NoDuplicateIgsn {

  String message() default "IGSN already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoDuplicateIgsnValidator implements ConstraintValidator<NoDuplicateIgsn, SampleRowHolder> {

    private final TempQcSampleRepository tempQcSampleRepository;
    private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

    @Autowired
    public NoDuplicateIgsnValidator(TempQcSampleRepository tempQcSampleRepository,
        CuratorsSampleTsqpRepository curatorsSampleTsqpRepository) {
      this.tempQcSampleRepository = tempQcSampleRepository;
      this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    }


    @Override
    public boolean isValid(SampleRowHolder sampleRowHolder, ConstraintValidatorContext context) {
      context.disableDefaultConstraintViolation();
      Set<String> igsnSet = new HashSet<>();
      boolean valid = true;
      for (int i = 0; i < sampleRowHolder.getRows().size(); i++) {
        SampleRow row = sampleRowHolder.getRows().get(i);
        String igsn = row.getIgsn();
        if(igsn != null) {
          if(!igsnSet.add(igsn) || tempQcSampleRepository.existsByIgsn(igsn) || curatorsSampleTsqpRepository.existsByIgsn(igsn)) {
            valid = false;
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("rows["+i+"].igsn").addConstraintViolation();
          }
        }
      }

      return valid;
    }
  }

}
