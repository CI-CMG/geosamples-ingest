package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "rockLithology",
    "rockLithologyCode",
})
public class RockLithologyView {


  @NotBlank
  @Size(max = 100)
  private String rockLithology;

  @NotBlank
  @Size(max = 2)
  private String rockLithologyCode;

  @Size(max = 255)
  private String sourceUri;

  public String getRockLithology() {
    return rockLithology;
  }

  public void setRockLithology(String rockLithology) {
    this.rockLithology = rockLithology;
  }

  public String getRockLithologyCode() {
    return rockLithologyCode;
  }

  public void setRockLithologyCode(String rockLithologyCode) {
    this.rockLithologyCode = rockLithologyCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
