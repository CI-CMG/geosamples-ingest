package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "province",
    "provinceCode",
})
public class ProvinceView {

  @NotBlank
  @Size(max = 35)
  private String province;

  @NotBlank
  @Size(max = 2)
  private String provinceCode;

  @Size(max = 40)
  private String provinceComment;

  @Size(max = 255)
  private String sourceUri;

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getProvinceComment() {
    return provinceComment;
  }

  public void setProvinceComment(String provinceComment) {
    this.provinceComment = provinceComment;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
