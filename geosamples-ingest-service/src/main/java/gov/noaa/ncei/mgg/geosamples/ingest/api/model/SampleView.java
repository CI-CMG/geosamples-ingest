package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;

@Sortable({
    "imlgs",
    "cruise",
    "sample",
    "facilityCode",
    "platform",
    "deviceCode"
})
public class SampleView extends ProviderSampleView {
  private String facilityCode;

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }
}
