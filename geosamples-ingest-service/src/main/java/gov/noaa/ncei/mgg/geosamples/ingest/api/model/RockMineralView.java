package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "rockMineral",
    "rockMineralCode",
})
public class RockMineralView {


  @NotBlank
  @Size(max = 35)
  private String rockMineral;

  @NotBlank
  @Size(max = 1)
  private String rockMineralCode;

  @Size(max = 255)
  private String sourceUri;

  public String getRockMineral() {
    return rockMineral;
  }

  public void setRockMineral(String rockMineral) {
    this.rockMineral = rockMineral;
  }

  public String getRockMineralCode() {
    return rockMineralCode;
  }

  public void setRockMineralCode(String rockMineralCode) {
    this.rockMineralCode = rockMineralCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
