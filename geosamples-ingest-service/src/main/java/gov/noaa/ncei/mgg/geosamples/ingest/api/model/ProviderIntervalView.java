package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;

@Sortable({
    "interval"
})
public class ProviderIntervalView {
    /*
   cruise = parentEntity.getCruise();
      facility = parentEntity.getFacility();
          platform = parentEntity.getPlatform();
          imlgs = parentEntity.getImlgs();
              parentIgsn = parentEntity.getIgsn();
                  shipCode = parentEntity.getShipCode();

    public void setParentEntity(P parentEntity) {






    sample = parentEntity.getSample();
    device = parentEntity.getDevice();
    setParentEntityInternal(parentEntity);
  }
   */


  private Long id;
  private Integer interval;
  private Double depthTop;
  private Double depthBot;
  private String dhCoreId;
  private Double dhCoreLength;
  private Integer dhCoreInterval;
  private Double dTopInDhCore;
  private Double dBotInDhCore;
  private String lithCode1;
  private String textCode1;
  private String lithCode2;
  private String textCode2;
  private String compCode1;
  private String compCode2;
  private String compCode3;
  private String compCode4;
  private String compCode5;
  private String compCode6;
  private String description;
  private List<String> ageCodes = new ArrayList<>(0);
  private Double weight;
  private String rockLithCode;
  private String rockMinCode;
  private String weathMetaCode;
  private String remarkCode;
  private String munsellCode;
  private Boolean exhausted;
  private String photoLink;
  private String unitNumber;
  private String intComments;
  private String dhDevice;
  private Double cdTop;
  private Double cdBot;
  private String igsn;
  private String imlgs;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  public Double getDepthTop() {
    return depthTop;
  }

  public void setDepthTop(Double depthTop) {
    this.depthTop = depthTop;
  }


  public Double getDepthBot() {
    return depthBot;
  }

  public void setDepthBot(Double depthBot) {
    this.depthBot = depthBot;
  }


  public String getDhCoreId() {
    return dhCoreId;
  }

  public void setDhCoreId(String dhCoreId) {
    this.dhCoreId = dhCoreId;
  }

  public Double getDhCoreLength() {
    return dhCoreLength;
  }

  public void setDhCoreLength(Double dhCoreLength) {
    this.dhCoreLength = dhCoreLength;
  }

  public Integer getDhCoreInterval() {
    return dhCoreInterval;
  }

  public void setDhCoreInterval(Integer dhCoreInterval) {
    this.dhCoreInterval = dhCoreInterval;
  }

  public Double getdTopInDhCore() {
    return dTopInDhCore;
  }

  public void setdTopInDhCore(Double dTopInDhCore) {
    this.dTopInDhCore = dTopInDhCore;
  }

  public Double getdBotInDhCore() {
    return dBotInDhCore;
  }

  public void setdBotInDhCore(Double dBotInDhCore) {
    this.dBotInDhCore = dBotInDhCore;
  }

  public String getLithCode1() {
    return lithCode1;
  }

  public void setLithCode1(String lithCode1) {
    this.lithCode1 = lithCode1;
  }

  public String getTextCode1() {
    return textCode1;
  }

  public void setTextCode1(String textCode1) {
    this.textCode1 = textCode1;
  }

  public String getLithCode2() {
    return lithCode2;
  }

  public void setLithCode2(String lithCode2) {
    this.lithCode2 = lithCode2;
  }

  public String getTextCode2() {
    return textCode2;
  }

  public void setTextCode2(String textCode2) {
    this.textCode2 = textCode2;
  }

  public String getCompCode1() {
    return compCode1;
  }

  public void setCompCode1(String compCode1) {
    this.compCode1 = compCode1;
  }

  public String getCompCode2() {
    return compCode2;
  }

  public void setCompCode2(String compCode2) {
    this.compCode2 = compCode2;
  }

  public String getCompCode3() {
    return compCode3;
  }

  public void setCompCode3(String compCode3) {
    this.compCode3 = compCode3;
  }

  public String getCompCode4() {
    return compCode4;
  }

  public void setCompCode4(String compCode4) {
    this.compCode4 = compCode4;
  }

  public String getCompCode5() {
    return compCode5;
  }

  public void setCompCode5(String compCode5) {
    this.compCode5 = compCode5;
  }

  public String getCompCode6() {
    return compCode6;
  }

  public void setCompCode6(String compCode6) {
    this.compCode6 = compCode6;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getAgeCodes() {
    return ageCodes;
  }

  public void setAgeCodes(List<String> ageCodes) {
    if (ageCodes == null) {
      ageCodes = new ArrayList<>(0);
    }
    this.ageCodes = ageCodes;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public String getRockLithCode() {
    return rockLithCode;
  }

  public void setRockLithCode(String rockLithCode) {
    this.rockLithCode = rockLithCode;
  }

  public String getRockMinCode() {
    return rockMinCode;
  }

  public void setRockMinCode(String rockMinCode) {
    this.rockMinCode = rockMinCode;
  }

  public String getWeathMetaCode() {
    return weathMetaCode;
  }

  public void setWeathMetaCode(String weathMetaCode) {
    this.weathMetaCode = weathMetaCode;
  }

  public String getRemarkCode() {
    return remarkCode;
  }

  public void setRemarkCode(String remarkCode) {
    this.remarkCode = remarkCode;
  }

  public String getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(String munsellCode) {
    this.munsellCode = munsellCode;
  }

  public Boolean getExhausted() {
    return exhausted;
  }

  public void setExhausted(Boolean exhausted) {
    this.exhausted = exhausted;
  }

  public String getPhotoLink() {
    return photoLink;
  }

  public void setPhotoLink(String photoLink) {
    this.photoLink = photoLink;
  }

  public String getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  public String getIntComments() {
    return intComments;
  }

  public void setIntComments(String intComments) {
    this.intComments = intComments;
  }

  public String getDhDevice() {
    return dhDevice;
  }

  public void setDhDevice(String dhDevice) {
    this.dhDevice = dhDevice;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  public Double getCdTop() {
    return cdTop;
  }

  public void setCdTop(Double cdTop) {
    this.cdTop = cdTop;
  }

  public Double getCdBot() {
    return cdBot;
  }

  public void setCdBot(Double cdBot) {
    this.cdBot = cdBot;
  }

}
