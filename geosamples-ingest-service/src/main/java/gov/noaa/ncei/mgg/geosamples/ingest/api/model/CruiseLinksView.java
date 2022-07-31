package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Sortable({
    "id",
    "cruiseName",
    "cruiseYear",
    "legName",
    "platform",
    "dataLink",
    "linkType",
    "LinkSource",
    "linkLevel",
    "publish"
})
public class CruiseLinksView {
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

  private Long cruisePlatform;

  @Size(max = 30)
  private String cruiseName;

  private Short cruiseYear;

  @Size(max = 50)
  private String platform;

  private Long leg;

  @Size(max = 30)
  private String legName;

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
    if(dataLink != null){
      dataLink = dataLink.trim().toUpperCase(Locale.ENGLISH);
    }
    this.dataLink = dataLink;
  }

  public String getLinkLevel() {
    return linkLevel;
  }

  public void setLinkLevel(String linkLevel) {
    if (linkLevel != null){
      linkLevel = linkLevel.trim().toUpperCase(Locale.ENGLISH);
    }
    this.linkLevel = linkLevel;
  }

  public String getLinkSource() {
    return linkSource;
  }

  public void setLinkSource(String linkSource) {
    if (linkSource != null) {
      linkSource = linkSource.trim().toUpperCase(Locale.ENGLISH);
    }
    this.linkSource = linkSource;
  }

  public String getLinkType() {
    return linkType;
  }

  public void setLinkType(String linkType) {
    if (linkType != null) {
      linkType = linkType.trim().toUpperCase(Locale.ENGLISH);
    }
    this.linkType = linkType;
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }

  public Long getCruisePlatform() {
    return cruisePlatform;
  }

  public void setCruisePlatform(Long cruisePlatform) {
    this.cruisePlatform = cruisePlatform;
  }

  public String getCruiseName() {
    return cruiseName;
  }

  public void setCruiseName(String cruiseName) {
    if (cruiseName != null) {
      cruiseName = cruiseName.trim().toUpperCase(Locale.ENGLISH);
    }
    this.cruiseName = cruiseName;
  }

  public Short getCruiseYear() {
    return cruiseYear;
  }

  public void setCruiseYear(Short cruiseYear) {
    this.cruiseYear = cruiseYear;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    if (platform != null) {
      platform = platform.trim().toUpperCase(Locale.ENGLISH);
    }
    this.platform = platform;
  }

  public Long getLeg() {
    return leg;
  }

  public void setLeg(Long leg) {
    this.leg = leg;
  }

  public String getLegName() {
    return legName;
  }

  public void setLegName(String legName) {
    if (legName != null) {
      legName = legName.trim().toUpperCase(Locale.ENGLISH);
    }
    this.legName = legName;
  }
}
