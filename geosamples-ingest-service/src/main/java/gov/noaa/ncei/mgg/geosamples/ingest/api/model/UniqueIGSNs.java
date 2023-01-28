package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UniqueIGSNs.UniqueIGSNsValidator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.Payload;

@Target({FIELD, METHOD, PARAMETER, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueIGSNsValidator.class)
@Documented
public @interface UniqueIGSNs {

  String message() default "Found duplicate IGSN";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class UniqueIGSNsValidator implements ConstraintValidator<UniqueIGSNs, SampleRowHolder> {

    @Override
    public boolean isValid(SampleRowHolder value, ConstraintValidatorContext context) {
      List<String> duplicateIgsns = value.getRows().stream()
          .map(SampleRow::getIgsn)
          .filter(i -> i != null && !i.isEmpty())
          .collect(Collectors.groupingBy(Function.identity()))
          .entrySet()
          .stream()
          .filter(e -> e.getValue().size() > 1)
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());

      ConstraintViolationBuilder builder = null;
      for (int i = 0; i < value.getRows().size(); i++) {
        if (duplicateIgsns.contains(value.getRows().get(i).getIgsn())) {
          builder = context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate());
          builder.addContainerElementNode(String.format("rows[%s]", i), List.class, i).addPropertyNode("igsn").addConstraintViolation();
        }
      }
      return builder == null;
    }

  }

}
