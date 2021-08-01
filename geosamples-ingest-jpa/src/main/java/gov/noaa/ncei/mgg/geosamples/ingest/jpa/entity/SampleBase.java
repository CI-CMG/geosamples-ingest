package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.locationtech.jts.geom.Geometry;

/*
	FACILITY_CODE VARCHAR2(10) not null
		constraint TEMPQC_SAMPLE_FACILITY_FK
			references CURATORS_FACILITY,
	SHIP_CODE VARCHAR2(4),
	PLATFORM VARCHAR2(50) not null
		constraint TEMPQC_SAMPLE_PLATFORM_FK
			references PLATFORM_MASTER,
	CRUISE VARCHAR2(30) not null,
	SAMPLE VARCHAR2(30) not null,
	DEVICE VARCHAR2(30) not null
		constraint TEMPQC_SAMPLE_DEVICE_FK
			references CURATORS_DEVICE,
	BEGIN_DATE VARCHAR2(8),
	END_DATE VARCHAR2(8),
	LAT NUMBER(9,5),
	LATDEG NUMBER(2),
	LATMIN VARCHAR2(5),
	NS VARCHAR2(1),
	END_LAT NUMBER(9,5),
	END_LATDEG NUMBER(2),
	END_LATMIN VARCHAR2(5),
	END_NS VARCHAR2(1),
	LON NUMBER(10,5),
	LONDEG NUMBER(3),
	LONMIN VARCHAR2(5),
	EW VARCHAR2(1),
	END_LON NUMBER(10,5),
	END_LONDEG NUMBER(3),
	END_LONMIN VARCHAR2(5),
	END_EW VARCHAR2(1),
	LATLON_ORIG VARCHAR2(1),
	WATER_DEPTH NUMBER(5),
	END_WATER_DEPTH NUMBER(5),
	STORAGE_METH VARCHAR2(35)
		constraint TEMPQC_SAMPLE_STORAGE_FK
			references CURATORS_STORAGE_METH,
	CORED_LENGTH NUMBER(6),
	CORED_LENGTH_MM NUMBER(2),
	CORED_DIAM NUMBER(3),
	CORED_DIAM_MM NUMBER(2),
	PI VARCHAR2(255),
	PROVINCE VARCHAR2(35)
		constraint TEMPQC_SAMPLE_PROVINCE_FK
			references CURATORS_PROVINCE,
	LAKE VARCHAR2(50),
	OTHER_LINK VARCHAR2(500),
	LAST_UPDATE VARCHAR2(8),
	IGSN VARCHAR2(9),
	LEG VARCHAR2(30),
	SAMPLE_COMMENTS VARCHAR2(2000),
	PUBLISH VARCHAR2(1),
	PREVIOUS_STATE VARCHAR2(1),
	OBJECTID NUMBER(38),
	SHAPE MDSYS.SDO_GEOMETRY,
	SHOW_SAMPL VARCHAR2(254),
	IMLGS VARCHAR2(15),
	constraint TEMPQC_SAMPLE_PK
		primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE)
 */


//@IdClass(TempQcSamplePk.class)
@MappedSuperclass
public abstract class SampleBase {

//  @Id
//  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
//  private String facilityCode;
//
//  @Id
//  @Column(name = "PLATFORM", nullable = false, length = 50)
//  private String platformId;

  @Column(name = "CRUISE", nullable = false, length = 30)
  private String cruise;

  @Column(name = "SAMPLE", nullable = false, length = 30)
  private String sample;

//  @Id
//  @Column(name = "DEVICE", nullable = false, length = 30)
//  private String deviceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FACILITY_CODE", nullable = false)
  private CuratorsFacilityEntity facility;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PLATFORM", nullable = false)
  private PlatformMasterEntity platform;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEVICE", nullable = false)
  private CuratorsDeviceEntity device;

  @Column(name = "SHIP_CODE", length = 4)
  private String shipCode;

  @Column(name = "BEGIN_DATE", length = 8)
  private String beginDate;

  @Column(name = "END_DATE", length = 8)
  private String endDate;

  @Column(name = "LAT", columnDefinition = "NUMBER")
  private Double lat;

  @Column(name = "LATDEG")
  private Integer latDeg;

  @Column(name = "LATMIN", length = 5)
  private String latMin;

  @Column(name = "NS", length = 1)
  private String ns;

  @Column(name = "END_LAT", columnDefinition = "NUMBER")
  private Double endLat;

  @Column(name = "END_LATDEG")
  private Integer endLatDeg;

  @Column(name = "END_LATMIN", length = 5)
  private String endLatMin;

  @Column(name = "END_NS", length = 1)
  private String endNs;

  @Column(name = "LON", columnDefinition = "NUMBER")
  private Double lon;

  @Column(name = "LONDEG")
  private Integer lonDeg;

  @Column(name = "LONMIN", length = 5)
  private String lonMin;

  @Column(name = "EW", length = 1)
  private String ew;

  @Column(name = "END_LON", columnDefinition = "NUMBER")
  private Double endLon;

  @Column(name = "END_LONDEG")
  private Integer endLonDeg;

  @Column(name = "END_LONMIN", length = 5)
  private String endLonMin;

  @Column(name = "END_EW", length = 1)
  private String endEw;

  @Column(name = "LATLON_ORIG", length = 1)
  private String latLonOrig;

  @Column(name = "WATER_DEPTH")
  private Integer waterDepth;

  @Column(name = "END_WATER_DEPTH")
  private Integer endWaterDepth;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STORAGE_METH")
  private CuratorsStorageMethEntity storageMeth;

  @Column(name = "CORED_LENGTH")
  private Integer coredLength;

  @Column(name = "CORED_LENGTH_MM")
  private Integer coredLengthMm;

  @Column(name = "CORED_DIAM")
  private Integer coredDiam;

  @Column(name = "CORED_DIAM_MM")
  private Integer coredDiamMm;

  @Column(name = "PI", length = 255)
  private String pi;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PROVINCE")
  private CuratorsProvinceEntity province;

  @Column(name = "LAKE", length = 50)
  private String lake;

  @Column(name = "OTHER_LINK", length = 500)
  private String otherLink;

  @Column(name = "LAST_UPDATE", length = 8)
  private String lastUpdate;

  @Column(name = "IGSN", length = 9, unique = true)
  private String igsn;

  @Column(name = "LEG", length = 30)
  private String leg;

  @Column(name = "SAMPLE_COMMENTS", length = 2000)
  private String sampleComments;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "OBJECTID", nullable = false, unique = true)
  private Long objectId;

  @Column(name = "SHAPE")
  private Geometry shape;

  @Column(name = "SHOW_SAMPL", length = 254)
  private String showSampl;

  @Id
  @Column(name = "IMLGS", length = 20, nullable = false)
  private String imlgs;

//  private TempQcSamplePk getPk() {
//    TempQcSamplePk pk = new TempQcSamplePk();
//    pk.setFacilityCode(facilityCode);
//    pk.setPlatformId(platformId);
//    pk.setCruise(cruise);
//    pk.setSample(sample);
//    pk.setDeviceId(deviceId);
//    return pk;
//  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleBase that = (SampleBase) o;
    return Objects.equals(imlgs, that.imlgs);
  }

  @Override
  public int hashCode() {
    return 1;
  }

//  public String getFacilityCode() {
//    return facilityCode;
//  }
//
//  public String getPlatformId() {
//    return platformId;
//  }

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

//  public String getDeviceId() {
//    return deviceId;
//  }

  public CuratorsFacilityEntity getFacility() {
    return facility;
  }

  public void setFacility(CuratorsFacilityEntity facility) {
//    facilityCode = facility == null ? null : facility.getFacilityCode();
    this.facility = facility;
  }

  public PlatformMasterEntity getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformMasterEntity platform) {
//    platformId = platform == null ? null : platform.getPlatform();
    this.platform = platform;
  }

  public CuratorsDeviceEntity getDevice() {
    return device;
  }

  public void setDevice(CuratorsDeviceEntity device) {
//    deviceId = device == null ? null : device.getDevice();
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

  public Integer getLatDeg() {
    return latDeg;
  }

  public void setLatDeg(Integer latDeg) {
    this.latDeg = latDeg;
  }

  public String getLatMin() {
    return latMin;
  }

  public void setLatMin(String latMin) {
    this.latMin = latMin;
  }

  public String getNs() {
    return ns;
  }

  public void setNs(String ns) {
    this.ns = ns;
  }

  public Double getEndLat() {
    return endLat;
  }

  public void setEndLat(Double endLat) {
    this.endLat = endLat;
  }

  public Integer getEndLatDeg() {
    return endLatDeg;
  }

  public void setEndLatDeg(Integer endLatDeg) {
    this.endLatDeg = endLatDeg;
  }

  public String getEndLatMin() {
    return endLatMin;
  }

  public void setEndLatMin(String endLatMin) {
    this.endLatMin = endLatMin;
  }

  public String getEndNs() {
    return endNs;
  }

  public void setEndNs(String endNs) {
    this.endNs = endNs;
  }

  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public Integer getLonDeg() {
    return lonDeg;
  }

  public void setLonDeg(Integer lonDeg) {
    this.lonDeg = lonDeg;
  }

  public String getLonMin() {
    return lonMin;
  }

  public void setLonMin(String lonMin) {
    this.lonMin = lonMin;
  }

  public String getEw() {
    return ew;
  }

  public void setEw(String ew) {
    this.ew = ew;
  }

  public Double getEndLon() {
    return endLon;
  }

  public void setEndLon(Double endLon) {
    this.endLon = endLon;
  }

  public Integer getEndLonDeg() {
    return endLonDeg;
  }

  public void setEndLonDeg(Integer endLonDeg) {
    this.endLonDeg = endLonDeg;
  }

  public String getEndLonMin() {
    return endLonMin;
  }

  public void setEndLonMin(String endLonMin) {
    this.endLonMin = endLonMin;
  }

  public String getEndEw() {
    return endEw;
  }

  public void setEndEw(String endEw) {
    this.endEw = endEw;
  }

  public String getLatLonOrig() {
    return latLonOrig;
  }

  public void setLatLonOrig(String latLonOrig) {
    this.latLonOrig = latLonOrig;
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

  public CuratorsStorageMethEntity getStorageMeth() {
    return storageMeth;
  }

  public void setStorageMeth(CuratorsStorageMethEntity storageMeth) {
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

  public CuratorsProvinceEntity getProvince() {
    return province;
  }

  public void setProvince(CuratorsProvinceEntity province) {
    this.province = province;
  }

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = lake;
  }

  public String getOtherLink() {
    return otherLink;
  }

  public void setOtherLink(String otherLink) {
    this.otherLink = otherLink;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
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

  public Long getObjectId() {
    return objectId;
  }

  public void setObjectId(Long objectId) {
    this.objectId = objectId;
  }

  public Geometry getShape() {
    return shape;
  }

  public void setShape(Geometry shape) {
    this.shape = shape;
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

}
