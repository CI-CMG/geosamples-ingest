package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.config.FormatUtils;

public class PositionDim {

  private final Double value;
  private final Integer degrees;
  private final String minutes;
  private final String direction;

  public PositionDim() {
    this(null, null, null, null);
  }

  public PositionDim(Double value, Integer degrees, Double minutes, String direction) {
    this.value = value;
    this.degrees = degrees;
    this.minutes = minutes == null ? null : FormatUtils.doubleToString2LeadingZeros2Decimal(minutes);
    this.direction = direction;
  }

  public Double getValue() {
    return value;
  }

  public Integer getDegrees() {
    return degrees;
  }

  public String getMinutes() {
    return minutes;
  }

  public String getDirection() {
    return direction;
  }
}
