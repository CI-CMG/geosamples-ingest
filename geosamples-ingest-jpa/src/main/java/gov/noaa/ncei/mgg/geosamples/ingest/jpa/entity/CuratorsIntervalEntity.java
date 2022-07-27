package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_INTERVAL")
public class CuratorsIntervalEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_INTERVAL_SEQ")
  @SequenceGenerator(name = "CURATORS_INTERVAL_SEQ", sequenceName = "CURATORS_INTERVAL_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "INTERVAL", nullable = false)
  private Integer interval;

  @Column(name = "DEPTH_TOP")
  private Integer depthTop;

  @Column(name = "DEPTH_TOP_MM")
  private Integer depthTopMm;

  @Column(name = "DEPTH_BOT")
  private Integer depthBot;

  @Column(name = "DEPTH_BOT_MM")
  private Integer depthBotMm;

  @Column(name = "DHCORE_ID", length = 30)
  private String dhCoreId;

  @Column(name = "DHCORE_LENGTH")
  private Integer dhCoreLength;

  @Column(name = "DHCORE_LENGTH_MM")
  private Integer dhCoreLengthMm;

  @Column(name = "DHCORE_INTERVAL")
  private Integer dhCoreInterval;

  @Column(name = "DTOP_IN_DHCORE")
  private Integer dTopInDhCore;

  @Column(name = "DTOP_MM_IN_DHCORE")
  private Integer dTopMmInDhCore;

  @Column(name = "DBOT_IN_DHCORE")
  private Integer dBotInDhCore;

  @Column(name = "DBOT_MM_IN_DHCORE")
  private Integer dBotMmInDhCore;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LITH1")
  private CuratorsLithologyEntity lith1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TEXT1")
  private CuratorsTextureEntity text1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LITH2")
  private CuratorsLithologyEntity lith2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TEXT2")
  private CuratorsTextureEntity text2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP1")
  private CuratorsLithologyEntity comp1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP2")
  private CuratorsLithologyEntity comp2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP3")
  private CuratorsLithologyEntity comp3;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP4")
  private CuratorsLithologyEntity comp4;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP5")
  private CuratorsLithologyEntity comp5;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMP6")
  private CuratorsLithologyEntity comp6;

  @Column(name = "DESCRIPTION", length = 2000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "AGE")
  private CuratorsAgeEntity age;

  @Column(name = "ABSOLUTE_AGE_TOP", length = 50)
  private String absoluteAgeTop;

  @Column(name = "ABSOLUTE_AGE_BOT", length = 50)
  private String absoluteAgeBot;

  @Column(name = "WEIGHT", columnDefinition = "NUMBER")
  private Double weight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROCK_LITH")
  private CuratorsRockLithEntity rockLith;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROCK_MIN")
  private CuratorsRockMinEntity rockMin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WEATH_META")
  private CuratorsWeathMetaEntity weathMeta;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REMARK")
  private CuratorsRemarkEntity remark;

  @Column(name = "MUNSELL_CODE", length = 10)
  private String munsellCode;

  @Column(name = "MUNSELL", length = 30)
  private String munsell;

  @Column(name = "EXHAUST_CODE", length = 1)
  private String exhaustCode;

  @Column(name = "PHOTO_LINK", length = 500)
  private String photoLink;

  @Column(name = "LAKE", length = 50)
  private String lake;

  @Column(name = "UNIT_NUMBER", length = 50)
  private String unitNumber;

  @Column(name = "INT_COMMENTS", length = 2000)
  private String intComments;

  @Column(name = "DHDEVICE", length = 50)
  private String dhDevice;

  @Column(name = "CMCD_TOP")
  private Integer cmcdTop;

  @Column(name = "MMCD_TOP")
  private Integer mmcdTop;

  @Column(name = "CMCD_BOT")
  private Integer cmcdBot;

  @Column(name = "MMCD_BOT")
  private Integer mmcdBot;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "IGSN", length = 9)
  private String igsn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IMLGS", nullable = false)
  private CuratorsSampleTsqpEntity sample;


  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
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

  public String getDhCoreId() {
    return dhCoreId;
  }

  public void setDhCoreId(String dhCoreId) {
    this.dhCoreId = dhCoreId;
  }

  public Integer getDhCoreLength() {
    return dhCoreLength;
  }

  public void setDhCoreLength(Integer dhCoreLength) {
    this.dhCoreLength = dhCoreLength;
  }

  public Integer getDhCoreLengthMm() {
    return dhCoreLengthMm;
  }

  public void setDhCoreLengthMm(Integer dhCoreLengthMm) {
    this.dhCoreLengthMm = dhCoreLengthMm;
  }

  public Integer getDhCoreInterval() {
    return dhCoreInterval;
  }

  public void setDhCoreInterval(Integer dhCoreInterval) {
    this.dhCoreInterval = dhCoreInterval;
  }

  public Integer getdTopInDhCore() {
    return dTopInDhCore;
  }

  public void setdTopInDhCore(Integer dTopInDhCore) {
    this.dTopInDhCore = dTopInDhCore;
  }

  public Integer getdTopMmInDhCore() {
    return dTopMmInDhCore;
  }

  public void setdTopMmInDhCore(Integer dTopMmInDhCore) {
    this.dTopMmInDhCore = dTopMmInDhCore;
  }

  public Integer getdBotInDhCore() {
    return dBotInDhCore;
  }

  public void setdBotInDhCore(Integer dBotInDhCore) {
    this.dBotInDhCore = dBotInDhCore;
  }

  public Integer getdBotMmInDhCore() {
    return dBotMmInDhCore;
  }

  public void setdBotMmInDhCore(Integer dBotMmInDhCore) {
    this.dBotMmInDhCore = dBotMmInDhCore;
  }

  public CuratorsLithologyEntity getLith1() {
    return lith1;
  }

  public void setLith1(CuratorsLithologyEntity lith1) {
    this.lith1 = lith1;
  }

  public CuratorsTextureEntity getText1() {
    return text1;
  }

  public void setText1(CuratorsTextureEntity text1) {
    this.text1 = text1;
  }

  public CuratorsLithologyEntity getLith2() {
    return lith2;
  }

  public void setLith2(CuratorsLithologyEntity lith2) {
    this.lith2 = lith2;
  }

  public CuratorsTextureEntity getText2() {
    return text2;
  }

  public void setText2(CuratorsTextureEntity text2) {
    this.text2 = text2;
  }

  public CuratorsLithologyEntity getComp1() {
    return comp1;
  }

  public void setComp1(CuratorsLithologyEntity comp1) {
    this.comp1 = comp1;
  }

  public CuratorsLithologyEntity getComp2() {
    return comp2;
  }

  public void setComp2(CuratorsLithologyEntity comp2) {
    this.comp2 = comp2;
  }

  public CuratorsLithologyEntity getComp3() {
    return comp3;
  }

  public void setComp3(CuratorsLithologyEntity comp3) {
    this.comp3 = comp3;
  }

  public CuratorsLithologyEntity getComp4() {
    return comp4;
  }

  public void setComp4(CuratorsLithologyEntity comp4) {
    this.comp4 = comp4;
  }

  public CuratorsLithologyEntity getComp5() {
    return comp5;
  }

  public void setComp5(CuratorsLithologyEntity comp5) {
    this.comp5 = comp5;
  }

  public CuratorsLithologyEntity getComp6() {
    return comp6;
  }

  public void setComp6(CuratorsLithologyEntity comp6) {
    this.comp6 = comp6;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CuratorsAgeEntity getAge() {
    return age;
  }

  public void setAge(CuratorsAgeEntity age) {
    this.age = age;
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

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public CuratorsRockLithEntity getRockLith() {
    return rockLith;
  }

  public void setRockLith(CuratorsRockLithEntity rockLith) {
    this.rockLith = rockLith;
  }

  public CuratorsRockMinEntity getRockMin() {
    return rockMin;
  }

  public void setRockMin(CuratorsRockMinEntity rockMin) {
    this.rockMin = rockMin;
  }

  public CuratorsWeathMetaEntity getWeathMeta() {
    return weathMeta;
  }

  public void setWeathMeta(CuratorsWeathMetaEntity weathMeta) {
    this.weathMeta = weathMeta;
  }

  public CuratorsRemarkEntity getRemark() {
    return remark;
  }

  public void setRemark(CuratorsRemarkEntity remark) {
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

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = lake;
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

  public Integer getCmcdTop() {
    return cmcdTop;
  }

  public void setCmcdTop(Integer cmcdTop) {
    this.cmcdTop = cmcdTop;
  }

  public Integer getMmcdTop() {
    return mmcdTop;
  }

  public void setMmcdTop(Integer mmcdTop) {
    this.mmcdTop = mmcdTop;
  }

  public Integer getCmcdBot() {
    return cmcdBot;
  }

  public void setCmcdBot(Integer cmcdBot) {
    this.cmcdBot = cmcdBot;
  }

  public Integer getMmcdBot() {
    return mmcdBot;
  }

  public void setMmcdBot(Integer mmcdBot) {
    this.mmcdBot = mmcdBot;
  }

  public boolean isPublish() {
    return publish.equals("Y");
  }

  public void setPublish(boolean publish) {
    this.publish = publish ? "Y" : "N";
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String isgn) {
    this.igsn = isgn;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CuratorsSampleTsqpEntity getSample() {
    return sample;
  }

  public void setSample(CuratorsSampleTsqpEntity sample) {
    this.sample = sample;
  }
}
