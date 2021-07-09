package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "munsell",
    "munsellCode",
})
public class MunsellView {


  @NotBlank
  @Size(max = 30)
  private String munsell;

  @NotBlank
  @Size(max = 10)
  private String munsellCode;

  public String getMunsell() {
    return munsell;
  }

  public void setMunsell(String munsell) {
    this.munsell = munsell;
  }

  public String getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(String munsellCode) {
    this.munsellCode = munsellCode;
  }
}
