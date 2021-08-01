package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class IntervalIdView {

  //TODO add validation
  @NotBlank
  private String imlgs;
  @NotNull
  private Integer interval;

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }
}
