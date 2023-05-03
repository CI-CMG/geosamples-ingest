package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AttachedFacilityVIew {

  @NotNull
  private Long id;
  @NotBlank
  private String facilityCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

}
