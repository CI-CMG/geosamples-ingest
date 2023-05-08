package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import java.util.ArrayList;
import java.util.List;

public class IntervalSearchParameters extends ProviderIntervalSearchParameters {
  private List<String> facilityCode = new ArrayList<>(0);

  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if (facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
  }
}
