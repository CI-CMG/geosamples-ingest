package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import java.util.ArrayList;
import java.util.List;

public class CuratorDataResponse {

  private List<String> platforms = new ArrayList<>(0);
  private List<String> cruises = new ArrayList<>(0);
  private List<String> facilityCodes = new ArrayList<>(0);

  public List<String> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<String> platforms) {
    this.platforms = platforms;
  }

  public List<String> getCruises() {
    return cruises;
  }

  public void setCruises(List<String> cruises) {
    this.cruises = cruises;
  }

  public List<String> getFacilityCodes() {
    return facilityCodes;
  }

  public void setFacilityCodes(List<String> facilityCodes) {
    this.facilityCodes = facilityCodes;
  }
}
