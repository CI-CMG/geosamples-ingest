package gov.noaa.ncei.mgg.geosamples.ingest.service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleRow {

  private String facilityCode;
  private String shipName;
  private String cruiseId;
  private String sampleId;
  private LocalDate dateCollected;
  private LocalDate endDate;
  private Double beginningLatitude;
  private Double beginningLongitude;
  private Double endingLatitude;
  private Double endingLongitude;
  private Double beginningWaterDepth;
  private Double endingWaterDepth;
  private String samplingDeviceCode;
  private String storageMethodCode;
  private Double coreLength;
  private Double coreDiameter;
  private Double depthToTopOfInterval;
  private Double depthToBottomOfInterval;
  private String primaryLithologicCompositionCode;
  private String primaryTextureCode;
  private String secondaryLithologicCompositionCode;
  private String secondaryTextureCode;
  private List<String> otherComponentCodes = new ArrayList<>(0);
  private String geologicAgeCode;
  private Integer intervalNumber;
  private Double bulkWeight;
  private String physiographicProvinceCode;
  private String sampleLithologyCode;
  private String sampleMineralogyCode;
  private String sampleWeatheringOrMetamorphismCode;
  private String glassRemarksCode;
  private String munsellColor;
  private String principalInvestigator;
  private boolean sampleNotAvailable = false;
  private String igsn;
  private String alternateCruise;
  private String description;
  private String comments;

  public String getSampleId() {
    return sampleId;
  }

  public void setSampleId(String sampleId) {
    this.sampleId = sampleId;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  public String getShipName() {
    return shipName;
  }

  public void setShipName(String shipName) {
    this.shipName = shipName;
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    this.cruiseId = cruiseId;
  }

  public LocalDate getDateCollected() {
    return dateCollected;
  }

  public void setDateCollected(LocalDate dateCollected) {
    this.dateCollected = dateCollected;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Double getBeginningLatitude() {
    return beginningLatitude;
  }

  public void setBeginningLatitude(Double beginningLatitude) {
    this.beginningLatitude = beginningLatitude;
  }

  public Double getBeginningLongitude() {
    return beginningLongitude;
  }

  public void setBeginningLongitude(Double beginningLongitude) {
    this.beginningLongitude = beginningLongitude;
  }

  public Double getEndingLatitude() {
    return endingLatitude;
  }

  public void setEndingLatitude(Double endingLatitude) {
    this.endingLatitude = endingLatitude;
  }

  public Double getEndingLongitude() {
    return endingLongitude;
  }

  public void setEndingLongitude(Double endingLongitude) {
    this.endingLongitude = endingLongitude;
  }

  public Double getBeginningWaterDepth() {
    return beginningWaterDepth;
  }

  public void setBeginningWaterDepth(Double beginningWaterDepth) {
    this.beginningWaterDepth = beginningWaterDepth;
  }

  public Double getEndingWaterDepth() {
    return endingWaterDepth;
  }

  public void setEndingWaterDepth(Double endingWaterDepth) {
    this.endingWaterDepth = endingWaterDepth;
  }

  public String getSamplingDeviceCode() {
    return samplingDeviceCode;
  }

  public void setSamplingDeviceCode(String samplingDeviceCode) {
    this.samplingDeviceCode = samplingDeviceCode;
  }

  public String getStorageMethodCode() {
    return storageMethodCode;
  }

  public void setStorageMethodCode(String storageMethodCode) {
    this.storageMethodCode = storageMethodCode;
  }

  public Double getCoreLength() {
    return coreLength;
  }

  public void setCoreLength(Double coreLength) {
    this.coreLength = coreLength;
  }

  public Double getCoreDiameter() {
    return coreDiameter;
  }

  public void setCoreDiameter(Double coreDiameter) {
    this.coreDiameter = coreDiameter;
  }

  public Double getDepthToTopOfInterval() {
    return depthToTopOfInterval;
  }

  public void setDepthToTopOfInterval(Double depthToTopOfInterval) {
    this.depthToTopOfInterval = depthToTopOfInterval;
  }

  public Double getDepthToBottomOfInterval() {
    return depthToBottomOfInterval;
  }

  public void setDepthToBottomOfInterval(Double depthToBottomOfInterval) {
    this.depthToBottomOfInterval = depthToBottomOfInterval;
  }

  public String getPrimaryLithologicCompositionCode() {
    return primaryLithologicCompositionCode;
  }

  public void setPrimaryLithologicCompositionCode(String primaryLithologicCompositionCode) {
    this.primaryLithologicCompositionCode = primaryLithologicCompositionCode;
  }

  public String getPrimaryTextureCode() {
    return primaryTextureCode;
  }

  public void setPrimaryTextureCode(String primaryTextureCode) {
    this.primaryTextureCode = primaryTextureCode;
  }

  public String getSecondaryLithologicCompositionCode() {
    return secondaryLithologicCompositionCode;
  }

  public void setSecondaryLithologicCompositionCode(String secondaryLithologicCompositionCode) {
    this.secondaryLithologicCompositionCode = secondaryLithologicCompositionCode;
  }

  public String getSecondaryTextureCode() {
    return secondaryTextureCode;
  }

  public void setSecondaryTextureCode(String secondaryTextureCode) {
    this.secondaryTextureCode = secondaryTextureCode;
  }

  public List<String> getOtherComponentCodes() {
    return otherComponentCodes;
  }

  public void setOtherComponentCodes(List<String> otherComponentCodes) {
    if(otherComponentCodes == null) {
      otherComponentCodes = new ArrayList<>(0);
    }
    this.otherComponentCodes = otherComponentCodes;
  }

  public void addOtherComponentCode(String code) {
    otherComponentCodes.add(code);
  }

  public String getGeologicAgeCode() {
    return geologicAgeCode;
  }

  public void setGeologicAgeCode(String geologicAgeCode) {
    this.geologicAgeCode = geologicAgeCode;
  }

  public Integer getIntervalNumber() {
    return intervalNumber;
  }

  public void setIntervalNumber(Integer intervalNumber) {
    this.intervalNumber = intervalNumber;
  }

  public Double getBulkWeight() {
    return bulkWeight;
  }

  public void setBulkWeight(Double bulkWeight) {
    this.bulkWeight = bulkWeight;
  }

  public String getPhysiographicProvinceCode() {
    return physiographicProvinceCode;
  }

  public void setPhysiographicProvinceCode(String physiographicProvinceCode) {
    this.physiographicProvinceCode = physiographicProvinceCode;
  }

  public String getSampleLithologyCode() {
    return sampleLithologyCode;
  }

  public void setSampleLithologyCode(String sampleLithologyCode) {
    this.sampleLithologyCode = sampleLithologyCode;
  }

  public String getSampleMineralogyCode() {
    return sampleMineralogyCode;
  }

  public void setSampleMineralogyCode(String sampleMineralogyCode) {
    this.sampleMineralogyCode = sampleMineralogyCode;
  }

  public String getSampleWeatheringOrMetamorphismCode() {
    return sampleWeatheringOrMetamorphismCode;
  }

  public void setSampleWeatheringOrMetamorphismCode(String sampleWeatheringOrMetamorphismCode) {
    this.sampleWeatheringOrMetamorphismCode = sampleWeatheringOrMetamorphismCode;
  }

  public String getGlassRemarksCode() {
    return glassRemarksCode;
  }

  public void setGlassRemarksCode(String glassRemarksCode) {
    this.glassRemarksCode = glassRemarksCode;
  }

  public String getMunsellColor() {
    return munsellColor;
  }

  public void setMunsellColor(String munsellColor) {
    this.munsellColor = munsellColor;
  }

  public String getPrincipalInvestigator() {
    return principalInvestigator;
  }

  public void setPrincipalInvestigator(String principalInvestigator) {
    this.principalInvestigator = principalInvestigator;
  }

  public boolean isSampleNotAvailable() {
    return sampleNotAvailable;
  }

  public void setSampleNotAvailable(boolean sampleNotAvailable) {
    this.sampleNotAvailable = sampleNotAvailable;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  public String getAlternateCruise() {
    return alternateCruise;
  }

  public void setAlternateCruise(String alternateCruise) {
    this.alternateCruise = alternateCruise;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
