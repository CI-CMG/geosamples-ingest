package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.Locale;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Sortable({
    "imlgs",
    "linkType",
    "linkLevel"
})
public class SampleLinksView {
  private Long id;

  @Size(max = 500)
  private String dataLink;

  @Size(max = 30)
  private String linkLevel;

  @Size(max = 30)
  private String linkSource;

  @Size(max = 30)
  private String linkType;

  @NotNull
  private Boolean publish;

  @NotBlank
  @Size(max = 15)
  private String imlgs;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDataLink() {
    return dataLink;
  }

  public void setDataLink(String dataLink) {
    this.dataLink = dataLink.trim().toUpperCase(Locale.ENGLISH);;
  }

  public String getLinkLevel() {
    return linkLevel;
  }

  public void setLinkLevel(String linkLevel) {
    this.linkLevel = linkLevel.trim().toUpperCase(Locale.ENGLISH);;
  }

  public String getLinkSource() {
    return linkSource;
  }

  public void setLinkSource(String linkSource) {
    this.linkSource = linkSource.trim().toUpperCase(Locale.ENGLISH);;
  }

  public String getLinkType() {
    return linkType;
  }

  public void setLinkType(String linkType) {
    this.linkType = linkType.trim().toUpperCase(Locale.ENGLISH);;
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }
}
