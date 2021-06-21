package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "lithology",
    "lithologyCode",
})
public class LithologyView {


  @NotBlank
  @Size(max = 40)
  private String lithology;

  @NotBlank
  @Size(max = 1)
  private String lithologyCode;

  @Size(max = 40)
  private String oldLithology;

  @Size(max = 255)
  private String sourceUri;

  public String getLithology() {
    return lithology;
  }

  public void setLithology(String lithology) {
    this.lithology = lithology;
  }

  public String getLithologyCode() {
    return lithologyCode;
  }

  public void setLithologyCode(String lithologyCode) {
    this.lithologyCode = lithologyCode;
  }

  public String getOldLithology() {
    return oldLithology;
  }

  public void setOldLithology(String oldLithology) {
    this.oldLithology = oldLithology;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
