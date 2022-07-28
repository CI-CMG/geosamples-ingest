package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "platform",
    "masterId",
    "icesCode"
})
public class PlatformView {

  private Long id;

  @NotBlank
  @Size(max = 50)
  private String platform;
  private Integer masterId;
  @Size(max = 30)
  private String prefix;
  @Size(max = 4)
  private String icesCode;
  @Size(max = 255)
  private String sourceUri;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Integer getMasterId() {
    return masterId;
  }

  public void setMasterId(Integer masterId) {
    this.masterId = masterId;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getIcesCode() {
    return icesCode;
  }

  public void setIcesCode(String icesCode) {
    this.icesCode = icesCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
