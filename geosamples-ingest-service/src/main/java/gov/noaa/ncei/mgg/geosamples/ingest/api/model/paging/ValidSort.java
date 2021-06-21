package gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort.ValidSortValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({FIELD, METHOD, PARAMETER, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidSortValidator.class)
@Documented
public @interface ValidSort {

  String message() default "Invalid sort definition";

  Class<?> value();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class ValidSortValidator implements ConstraintValidator<ValidSort, String> {


    private Class<?> view = Sortable.class;

    @Override
    public void initialize(ValidSort constraintAnnotation) {
      view = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
      if (value == null) {
        return true;
      }
      String[] split = value.split(":");
      if (split.length != 2) {
        return false;
      }
      String fieldName = split[0];
      String dir = split[1];

      Sortable sortable = view.getAnnotation(Sortable.class);
      if (sortable == null) {
        return false;
      }
      Set<String> sortableFields = Arrays.stream(sortable.value()).collect(Collectors.toSet());
      if(!sortableFields.contains(fieldName)) {
        return false;
      }
      return Arrays.stream(SortDirection.values())
          .map(Enum::toString)
          .anyMatch(sd -> sd.equalsIgnoreCase(dir));
    }
  }

}
