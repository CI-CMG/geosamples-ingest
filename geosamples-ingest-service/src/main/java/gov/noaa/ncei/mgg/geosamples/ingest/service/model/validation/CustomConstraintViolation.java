package gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation;

import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;

public class CustomConstraintViolation implements ConstraintViolation {

  private final String message;
  private final String propertyPath;

  public CustomConstraintViolation(String message, String propertyPath) {
    this.message = message;
    this.propertyPath = propertyPath;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getMessageTemplate() {
    return message;
  }

  @Override
  public Object getRootBean() {
    return null;
  }

  @Override
  public Class getRootBeanClass() {
    return SampleRowHolder.class;
  }

  @Override
  public Object getLeafBean() {
    return null;
  }

  @Override
  public Object[] getExecutableParameters() {
    return new Object[0];
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Path getPropertyPath() {
    return PathImpl.createPathFromString(propertyPath);
  }

  @Override
  public Object getInvalidValue() {
    return null;
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return null;
  }

  @Override
  public Object unwrap(Class aClass) {
    return null;
  }
}
