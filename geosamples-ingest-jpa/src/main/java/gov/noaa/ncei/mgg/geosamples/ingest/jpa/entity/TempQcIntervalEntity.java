package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
	FACILITY_CODE VARCHAR2(10) not null
		constraint TEMPQC_INTERVAL_FACILITY_FK
			references CURATORS_FACILITY,
	SHIP_CODE VARCHAR2(4),
	PLATFORM VARCHAR2(50) not null
		constraint TEMPQC_INTERVAL_PLATFORM_FK
			references PLATFORM_MASTER,
	CRUISE VARCHAR2(30) not null,
	SAMPLE VARCHAR2(30) not null,
	DEVICE VARCHAR2(30) not null
		constraint TEMPQC_INTERVAL_DEVICE_FK
			references CURATORS_DEVICE,
	INTERVAL NUMBER(6) not null,
	DEPTH_TOP NUMBER(6),
	DEPTH_TOP_MM NUMBER(2),
	DEPTH_BOT NUMBER(6),
	DEPTH_BOT_MM NUMBER(2),
	DHCORE_ID VARCHAR2(30),
	DHCORE_LENGTH NUMBER(6),
	DHCORE_LENGTH_MM NUMBER(2),
	DHCORE_INTERVAL NUMBER(3),
	DTOP_IN_DHCORE NUMBER(6),
	DTOP_MM_IN_DHCORE NUMBER(2),
	DBOT_IN_DHCORE NUMBER(6),
	DBOT_MM_IN_DHCORE NUMBER(2),
	LITH1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_LITH1_FK
			references CURATORS_LITHOLOGY,
	TEXT1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_TEXT1_FK
			references CURATORS_TEXTURE,
	LITH2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_LITH2_FK
			references CURATORS_LITHOLOGY,
	TEXT2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_TEXT2_FK
			references CURATORS_TEXTURE,
	COMP1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP1_FK
			references CURATORS_LITHOLOGY,
	COMP2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP2_FK
			references CURATORS_LITHOLOGY,
	COMP3 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP3_FK
			references CURATORS_LITHOLOGY,
	COMP4 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP4_FK
			references CURATORS_LITHOLOGY,
	COMP5 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP5_FK
			references CURATORS_LITHOLOGY,
	COMP6 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP6_FK
			references CURATORS_LITHOLOGY,
	DESCRIPTION VARCHAR2(2000),
	AGE VARCHAR2(20)
		constraint TEMPQC_INTERVAL_AGE_FK
			references CURATORS_AGE,
	ABSOLUTE_AGE_TOP VARCHAR2(50),
	ABSOLUTE_AGE_BOT VARCHAR2(50),
	WEIGHT NUMBER(8,3),
	ROCK_LITH VARCHAR2(100)
		constraint TEMPQC_INTERVAL_RKLITH_FK
			references CURATORS_ROCK_LITH,
	ROCK_MIN VARCHAR2(35)
		constraint TEMPQC_INTERVAL_RKMIN_FK
			references CURATORS_ROCK_MIN,
	WEATH_META VARCHAR2(30)
		constraint TEMPQC_INTERVAL_WEATH_FK
			references CURATORS_WEATH_META,
	REMARK VARCHAR2(70)
		constraint TEMPQC_INTERVAL_REMARK_FK
			references CURATORS_REMARK,
	MUNSELL_CODE VARCHAR2(10),
	MUNSELL VARCHAR2(30),
	EXHAUST_CODE VARCHAR2(1),
	PHOTO_LINK VARCHAR2(500),
	LAKE VARCHAR2(50),
	UNIT_NUMBER VARCHAR2(50),
	INT_COMMENTS VARCHAR2(2000),
	DHDEVICE VARCHAR2(50),
	CMCD_TOP NUMBER(6),
	MMCD_TOP NUMBER(1),
	CMCD_BOT NUMBER(6),
	MMCD_BOT NUMBER(1),
	PUBLISH VARCHAR2(1),
	PREVIOUS_STATE VARCHAR2(1),
	IGSN VARCHAR2(9),
	IMLGS VARCHAR2(15),
	PARENT_IGSN VARCHAR2(9),
	constraint TEMPQC_INTERVAL_PK
		primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE, INTERVAL)
 */
@Entity
@Table(name = "TEMPQC_INTERVAL")
@IdClass(TempQcIntervalPk.class)
public class TempQcIntervalEntity {

  @Id
  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
  private String facilityCode;

  @Id
  @Column(name = "PLATFORM", nullable = false, length = 50)
  private String platformId;

  @Id
  @Column(name = "CRUISE", nullable = false, length = 30)
  private String cruise;

  @Id
  @Column(name = "SAMPLE", nullable = false, length = 30)
  private String sample;

  @Id
  @Column(name = "DEVICE", nullable = false, length = 30)
  private String deviceId;

  @Id
  @Column(name = "INTERVAL", nullable = false)
  private Integer interval;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FACILITY_CODE", nullable = false, insertable = false, updatable = false)
  private CuratorsFacilityEntity facility;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PLATFORM", nullable = false, insertable = false, updatable = false)
  private PlatformMasterEntity platform;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEVICE", nullable = false, insertable = false, updatable = false)
  private CuratorsDeviceEntity device;

  @Column(name = "SHIP_CODE", length = 4)
  private String shipCode;

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
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "IGSN", length = 9)
  private String isgn;

  @Column(name = "IMLGS", length = 15)
  private String imlgs;

  @Column(name = "PARENT_IGSN", length = 9)
  private String parentIsgn;

  private TempQcIntervalPk getPk() {
    TempQcIntervalPk pk = new TempQcIntervalPk();
    pk.setFacilityCode(facilityCode);
    pk.setPlatformId(platformId);
    pk.setCruise(cruise);
    pk.setSample(sample);
    pk.setDeviceId(deviceId);
    pk.setInterval(interval);
    return pk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TempQcIntervalEntity that = (TempQcIntervalEntity) o;
    return Objects.equals(getPk(), that.getPk());
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilityCode);
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public String getPlatformId() {
    return platformId;
  }

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

  public String getDeviceId() {
    return deviceId;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  public CuratorsFacilityEntity getFacility() {
    return facility;
  }

  public void setFacility(CuratorsFacilityEntity facility) {
    facilityCode = facility == null ? null : facility.getFacilityCode();
    this.facility = facility;
  }

  public PlatformMasterEntity getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformMasterEntity platform) {
    platformId = platform == null ? null : platform.getPlatform();
    this.platform = platform;
  }

  public CuratorsDeviceEntity getDevice() {
    return device;
  }

  public void setDevice(CuratorsDeviceEntity device) {
    deviceId = device == null ? null : device.getDevice();
    this.device = device;
  }

  public String getShipCode() {
    return shipCode;
  }

  public void setShipCode(String shipCode) {
    this.shipCode = shipCode;
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

  public String getPublish() {
    return publish;
  }

  public void setPublish(String publish) {
    this.publish = publish;
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

  public String getIsgn() {
    return isgn;
  }

  public void setIsgn(String isgn) {
    this.isgn = isgn;
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public String getParentIsgn() {
    return parentIsgn;
  }

  public void setParentIsgn(String parentIsgn) {
    this.parentIsgn = parentIsgn;
  }
}
