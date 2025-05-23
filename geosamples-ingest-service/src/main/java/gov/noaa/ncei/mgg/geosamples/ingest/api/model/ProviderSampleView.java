package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;

@Sortable({
    "imlgs",
    "cruise",
    "sample",
    "platform",
    "deviceCode"
})
public class ProviderSampleView {
  private String imlgs;
  private String cruise;

  private Integer cruiseYear;
  private String sample;
  private String platform;
  private String deviceCode;
  private String beginDate;
  private String endDate;
  private Double lat;
  private Double endLat;
  private Double lon;
  private Double endLon;
  private Integer waterDepth;
  private Integer endWaterDepth;
  private String storageMethCode;
  private Double coredLength;
  private Double coredDiam;
  private String pi;
  private String provinceCode;
  private String lake;
  private String igsn;
  private String leg;
  private String sampleComments;

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  public Integer getCruiseYear() {
    return cruiseYear;
  }

  public void setCruiseYear(Integer cruiseYear) {
    this.cruiseYear = cruiseYear;
  }

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public void setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
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

  public String getStorageMethCode() {
    return storageMethCode;
  }

  public void setStorageMethCode(String storageMethCode) {
    this.storageMethCode = storageMethCode;
  }

  public Double getCoredLength() {
    return coredLength;
  }

  public void setCoredLength(Double coredLength) {
    this.coredLength = coredLength;
  }

  public Double getCoredDiam() {
    return coredDiam;
  }

  public void setCoredDiam(Double coredDiam) {
    this.coredDiam = coredDiam;
  }

  public String getPi() {
    return pi;
  }

  public void setPi(String pi) {
    this.pi = pi;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = lake;
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
}
