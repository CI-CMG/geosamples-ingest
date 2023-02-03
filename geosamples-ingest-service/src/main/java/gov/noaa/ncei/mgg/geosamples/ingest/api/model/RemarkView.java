package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Sortable({
    "remark",
    "remarkCode",
})
public class RemarkView {

  private Long id;

  @NotBlank
  @Size(max = 70)
  private String remark;

  @NotBlank
  @Size(max = 1)
  private String remarkCode;

  @Size(max = 255)
  private String sourceUri;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemarkCode() {
    return remarkCode;
  }

  public void setRemarkCode(String remarkCode) {
    this.remarkCode = remarkCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
