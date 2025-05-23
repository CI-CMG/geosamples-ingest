package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Sortable({
    "publish",
    "imlgs",
    "interval",
    "igsn",
    "intervalIgsn",
    "cruise",
    "sample",
    "facility",
    "platform",
    "beginDate",
    "approvalState",
})
public class CombinedSampleIntervalView {

  private String cruise;
  private String sample;
  private String facility;
  private String platform;
  private String device;
  private String shipCode;
  private String beginDate;
  private String endDate;
  private Double lat;
  private Double lon;
  private Double endLat;
  private Double endLon;
  private Integer waterDepth;
  private Integer endWaterDepth;
  private String storageMeth;
  private Integer coredLength;
  private Integer coredLengthMm;
  private Integer coredDiam;
  private Integer coredDiamMm;
  private String pi;
  private String province;
  private String sampleLake;
  private Instant lastUpdate;
  private String igsn;
  private String leg;
  private String sampleComments;

  private String showSampl;

  private String imlgs;

  private Integer interval;
  private Long intervalId;
  private Integer depthTop;
  private Integer depthTopMm;
  private Integer depthBot;
  private Integer depthBotMm;
  private String lith1;
  private String text1;
  private String lith2;
  private String text2;
  private String comp1;
  private String comp2;
  private String comp3;
  private String comp4;
  private String comp5;
  private String comp6;
  private String description;
  private List<String> ages = new ArrayList<>(0);
  private Double weight;
  private String rockLith;
  private String rockMin;
  private String weathMeta;
  private String remark;
  private String munsellCode;
  private String munsell;
  private String exhaustCode;
  private String photoLink;
  private String intComments;
  private String intervalIgsn;
  private String intervalParentIsgn;
  private Boolean publish;
  
  private String dhCoreId;
  private Double dhCoreLength;
  private Double dhCoreLengthMm;
  private Integer dhCoreInterval;
  private Double dTopInDhCore;
  private Double dTopMmInDhCore;
  private Double dBotInDhCore;
  private Double dBotMmInDhCore;
  private String dhDevice;
  private Double cmcdTop;
  private Double mmcdTop;
  private Double cmcdBot;
  private Double mmcdBot;
  private String absoluteAgeTop;
  private String absoluteAgeBot;

  private ApprovalState approvalState;

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getFacility() {
    return facility;
  }

  public void setFacility(String facility) {
    this.facility = facility;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getShipCode() {
    return shipCode;
  }

  public void setShipCode(String shipCode) {
    this.shipCode = shipCode;
  }

  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }


  public Double getEndLat() {
    return endLat;
  }

  public void setEndLat(Double endLat) {
    this.endLat = endLat;
  }

  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public Double getEndLon() {
    return endLon;
  }

  public void setEndLon(Double endLon) {
    this.endLon = endLon;
  }

  public Integer getWaterDepth() {
    return waterDepth;
  }

  public void setWaterDepth(Integer waterDepth) {
    this.waterDepth = waterDepth;
  }

  public Integer getEndWaterDepth() {
    return endWaterDepth;
  }

  public void setEndWaterDepth(Integer endWaterDepth) {
    this.endWaterDepth = endWaterDepth;
  }

  public String getStorageMeth() {
    return storageMeth;
  }

  public void setStorageMeth(String storageMeth) {
    this.storageMeth = storageMeth;
  }

  public Integer getCoredLength() {
    return coredLength;
  }

  public void setCoredLength(Integer coredLength) {
    this.coredLength = coredLength;
  }

  public Integer getCoredLengthMm() {
    return coredLengthMm;
  }

  public void setCoredLengthMm(Integer coredLengthMm) {
    this.coredLengthMm = coredLengthMm;
  }

  public Integer getCoredDiam() {
    return coredDiam;
  }

  public void setCoredDiam(Integer coredDiam) {
    this.coredDiam = coredDiam;
  }

  public Integer getCoredDiamMm() {
    return coredDiamMm;
  }

  public void setCoredDiamMm(Integer coredDiamMm) {
    this.coredDiamMm = coredDiamMm;
  }

  public String getPi() {
    return pi;
  }

  public void setPi(String pi) {
    this.pi = pi;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getSampleLake() {
    return sampleLake;
  }

  public void setSampleLake(String sampleLake) {
    this.sampleLake = sampleLake;
  }

  public Instant getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Instant lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  public String getLeg() {
    return leg;
  }

  public void setLeg(String leg) {
    this.leg = leg;
  }

  public String getSampleComments() {
    return sampleComments;
  }

  public void setSampleComments(String sampleComments) {
    this.sampleComments = sampleComments;
  }

  public String getShowSampl() {
    return showSampl;
  }

  public void setShowSampl(String showSampl) {
    this.showSampl = showSampl;
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

  public Long getIntervalId() {
    return intervalId;
  }

  public void setIntervalId(Long intervalId) {
    this.intervalId = intervalId;
  }

  public Integer getDepthTop() {
    return depthTop;
  }

  public void setDepthTop(Integer depthTop) {
    this.depthTop = depthTop;
  }

  public Integer getDepthTopMm() {
    return depthTopMm;
  }

  public void setDepthTopMm(Integer depthTopMm) {
    this.depthTopMm = depthTopMm;
  }

  public Integer getDepthBot() {
    return depthBot;
  }

  public void setDepthBot(Integer depthBot) {
    this.depthBot = depthBot;
  }

  public Integer getDepthBotMm() {
    return depthBotMm;
  }

  public void setDepthBotMm(Integer depthBotMm) {
    this.depthBotMm = depthBotMm;
  }

  public String getLith1() {
    return lith1;
  }

  public void setLith1(String lith1) {
    this.lith1 = lith1;
  }

  public String getText1() {
    return text1;
  }

  public void setText1(String text1) {
    this.text1 = text1;
  }

  public String getLith2() {
    return lith2;
  }

  public void setLith2(String lith2) {
    this.lith2 = lith2;
  }

  public String getText2() {
    return text2;
  }

  public void setText2(String text2) {
    this.text2 = text2;
  }

  public String getComp1() {
    return comp1;
  }

  public void setComp1(String comp1) {
    this.comp1 = comp1;
  }

  public String getComp2() {
    return comp2;
  }

  public void setComp2(String comp2) {
    this.comp2 = comp2;
  }

  public String getComp3() {
    return comp3;
  }

  public void setComp3(String comp3) {
    this.comp3 = comp3;
  }

  public String getComp4() {
    return comp4;
  }

  public void setComp4(String comp4) {
    this.comp4 = comp4;
  }

  public String getComp5() {
    return comp5;
  }

  public void setComp5(String comp5) {
    this.comp5 = comp5;
  }

  public String getComp6() {
    return comp6;
  }

  public void setComp6(String comp6) {
    this.comp6 = comp6;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getAges() {
    return ages;
  }

  public void setAges(List<String> ages) {
    if (ages == null) {
      ages = new ArrayList<>(0);
    }
    this.ages = ages;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public String getRockLith() {
    return rockLith;
  }

  public void setRockLith(String rockLith) {
    this.rockLith = rockLith;
  }

  public String getRockMin() {
    return rockMin;
  }

  public void setRockMin(String rockMin) {
    this.rockMin = rockMin;
  }

  public String getWeathMeta() {
    return weathMeta;
  }

  public void setWeathMeta(String weathMeta) {
    this.weathMeta = weathMeta;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(String munsellCode) {
    this.munsellCode = munsellCode;
  }

  public String getMunsell() {
    return munsell;
  }

  public void setMunsell(String munsell) {
    this.munsell = munsell;
  }

  public String getExhaustCode() {
    return exhaustCode;
  }

  public void setExhaustCode(String exhaustCode) {
    this.exhaustCode = exhaustCode;
  }

  public String getPhotoLink() {
    return photoLink;
  }

  public void setPhotoLink(String photoLink) {
    this.photoLink = photoLink;
  }

  public String getIntComments() {
    return intComments;
  }

  public void setIntComments(String intComments) {
    this.intComments = intComments;
  }

  public String getIntervalIgsn() {
    return intervalIgsn;
  }

  public void setIntervalIgsn(String intervalIgsn) {
    this.intervalIgsn = intervalIgsn;
  }

  public String getIntervalParentIsgn() {
    return intervalParentIsgn;
  }

  public void setIntervalParentIsgn(String intervalParentIsgn) {
    this.intervalParentIsgn = intervalParentIsgn;
  }

  public Boolean isPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }

  public ApprovalState getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalState approvalState) {
    this.approvalState = approvalState;
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

  public Double getDhCoreLengthMm() {
    return dhCoreLengthMm;
  }

  public void setDhCoreLengthMm(Double dhCoreLengthMm) {
    this.dhCoreLengthMm = dhCoreLengthMm;
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

  public Double getdTopMmInDhCore() {
    return dTopMmInDhCore;
  }

  public void setdTopMmInDhCore(Double dTopMmInDhCore) {
    this.dTopMmInDhCore = dTopMmInDhCore;
  }

  public Double getdBotInDhCore() {
    return dBotInDhCore;
  }

  public void setdBotInDhCore(Double dBotInDhCore) {
    this.dBotInDhCore = dBotInDhCore;
  }

  public Double getdBotMmInDhCore() {
    return dBotMmInDhCore;
  }

  public void setdBotMmInDhCore(Double dBotMmInDhCore) {
    this.dBotMmInDhCore = dBotMmInDhCore;
  }

  public String getDhDevice() {
    return dhDevice;
  }

  public void setDhDevice(String dhDevice) {
    this.dhDevice = dhDevice;
  }

  public Double getCmcdTop() {
    return cmcdTop;
  }

  public void setCmcdTop(Double cmcdTop) {
    this.cmcdTop = cmcdTop;
  }

  public Double getMmcdTop() {
    return mmcdTop;
  }

  public void setMmcdTop(Double mmcdTop) {
    this.mmcdTop = mmcdTop;
  }

  public Double getCmcdBot() {
    return cmcdBot;
  }

  public void setCmcdBot(Double cmcdBot) {
    this.cmcdBot = cmcdBot;
  }

  public Double getMmcdBot() {
    return mmcdBot;
  }

  public void setMmcdBot(Double mmcdBot) {
    this.mmcdBot = mmcdBot;
  }

  public String getAbsoluteAgeTop() {
    return absoluteAgeTop;
  }

  public void setAbsoluteAgeTop(String absoluteAgeTop) {
    this.absoluteAgeTop = absoluteAgeTop;
  }

  public String getAbsoluteAgeBot() {
    return absoluteAgeBot;
  }

  public void setAbsoluteAgeBot(String absoluteAgeBot) {
    this.absoluteAgeBot = absoluteAgeBot;
  }
}
