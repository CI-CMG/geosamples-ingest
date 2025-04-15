package gov.noaa.ncei.mgg.geosamples.ingest.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.noaa.ncei.mgg.geosamples.ingest.config.FormatUtils;

public class PositionDim {

  private final Double value;
  private final Integer degrees;
  private final String minutes;
  private final String direction;

  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  public PositionDim() {
    this(null, null, null, null);
  }

  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  public PositionDim(Double value, Integer degrees, Double minutes, String direction) {
    this.value = value;
    String min = minutes == null ? null : FormatUtils.doubleToString2LeadingZeros2Decimal(minutes);
    double rounded = min == null ? 0D : Double.parseDouble(min);
    if (rounded > 59.99) {
      if(degrees != null) {
        degrees = degrees + 1;
      }
      min = "00.00";
    }
    this.minutes = min;
    this.degrees = degrees;
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
