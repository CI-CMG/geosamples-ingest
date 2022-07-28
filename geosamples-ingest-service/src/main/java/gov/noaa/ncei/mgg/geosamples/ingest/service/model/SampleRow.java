package gov.noaa.ncei.mgg.geosamples.ingest.service.model;

import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidAgeCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidDate;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidDeviceCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidFacilityCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLatitude;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLithologyCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidLongitude;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidMunsell;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidPlatform;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidProvinceCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRemarkCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRockLithologyCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidRockMineralCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidStorageMethodCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidTextureCode;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.ValidWeatheringCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SampleRow {

  @NotBlank(message = "Facility Code is required")
  @Size(max = 10, message = "Must be at most 10 characters")
  @ValidFacilityCode
  private String facilityCode;
  @NotBlank(message = "Ship Name is required")
  @Size(max = 50, message = "Must be at most 50 characters")
  @ValidPlatform
  private String shipName;
  @NotBlank(message = "Cruise ID is required")
  @Size(max = 30, message = "Cruise ID must be at most 30 characters")
  private String cruiseId;
  @NotBlank(message = "Sample ID is required")
  @Size(max = 30, message = "Sample ID must be at most 30 characters")
  private String sampleId;
  @Pattern(regexp = "(\\d{8})|(\\d{4})|(\\d{6})", message = "Date Collected must be YYYYMMDD, YYYY, or YYYYMM")
  @ValidDate(message = "Invalid Date Collected")
  private String dateCollected;
  @Pattern(regexp = "(\\d{8})|(\\d{4})|(\\d{6})", message = "End Date must be YYYYMMDD, YYYY, or YYYYMM")
  @ValidDate(message = "Invalid End Date")
  private String endDate;
  @ValidLatitude
  private Double beginningLatitude;
  @ValidLongitude
  private Double beginningLongitude;
  @ValidLatitude
  private Double endingLatitude;
  @ValidLongitude
  private Double endingLongitude;
  @Min(value = 0, message = "Beginning Water Depth must not be negative")
  private Double beginningWaterDepth;
  @Min(value = 0, message = "Ending Water Depth must not be negative")
  private Double endingWaterDepth;
  @NotBlank(message = "Device Code is required")
  @Size(max = 2, message = "Must be at most 2 characters")
  @ValidDeviceCode
  private String samplingDeviceCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidStorageMethodCode
  private String storageMethodCode;
  @Min(value = 0, message = "Core Length must not be negative")
  private Double coreLength;
  @Min(value = 0, message = "Core Diameter must not be negative")
  private Double coreDiameter;
  @Min(value = 0, message = "Depth To Top Of Interval must not be negative")
  private Double depthToTopOfInterval;
  @Min(value = 0, message = "Depth To Bottom Of Interval must not be negative")
  private Double depthToBottomOfInterval;
  @Size(max = 1, message = "Must be 1 character")
  @ValidLithologyCode
  private String primaryLithologicCompositionCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidTextureCode
  private String primaryTextureCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidLithologyCode
  private String secondaryLithologicCompositionCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidTextureCode
  private String secondaryTextureCode;
  @Valid
  private List<@Size(max = 1, message = "Must be 1 character") @ValidLithologyCode String> otherComponentCodes = new ArrayList<>(0);
  @Size(max = 2, message = "Must be at most 2 characters")
  @ValidAgeCode
  private String geologicAgeCode;
  @NotNull(message = "Interval # is required")
  private Integer intervalNumber;
  @Min(value = 0, message = "Bulk Weight must not be negative")
  private Double bulkWeight;
  @Size(max = 2, message = "Must be at most 2 characters")
  @ValidProvinceCode
  private String physiographicProvinceCode;
  @Size(max = 2, message = "Must be at most 2 characters")
  @ValidRockLithologyCode
  private String sampleLithologyCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidRockMineralCode
  private String sampleMineralogyCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidWeatheringCode
  private String sampleWeatheringOrMetamorphismCode;
  @Size(max = 1, message = "Must be 1 character")
  @ValidRemarkCode
  private String glassRemarksCode;
  @Size(max = 10, message = "Munsell Color must be at most 10 characters")
  //TODO Munsell colors can be duplicated, sholuld the spreadsheet use the code?
  @ValidMunsell
  private String munsellColor;
  @Size(max = 255, message = "Principal Investigator must be at most 255 characters")
  private String principalInvestigator;
  @Pattern(regexp = "X", message = "Sample Not Available must be X or empty")
  private String sampleNotAvailable;
  @Size(max = 9, message = "IGSN must be at most 9 characters")
  private String igsn;
  @Size(max = 30, message = "Alternate Cruise/Leg must be at most 30 characters")
  private String alternateCruise;
  @Size(max = 2000, message = "Free-form Description of Composition must be at most 2000 characters")
  private String description;
  @Size(max = 2000, message = "Comments on Subsample/Interval must be at most 2000 characters")
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
    if (facilityCode != null) {
      facilityCode = facilityCode.trim().toUpperCase(Locale.ENGLISH);
    }
    this.facilityCode = facilityCode;
  }

  public String getShipName() {
    return shipName;
  }

  public void setShipName(String shipName) {
    if (shipName != null) {
      shipName = shipName.trim().toUpperCase(Locale.ENGLISH);
    }
    this.shipName = shipName;
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    this.cruiseId = cruiseId;
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
    if (otherComponentCodes == null) {
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

  public String getDateCollected() {
    return dateCollected;
  }

  public void setDateCollected(String dateCollected) {
    this.dateCollected = dateCollected;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getSampleNotAvailable() {
    return sampleNotAvailable;
  }

  public void setSampleNotAvailable(String sampleNotAvailable) {
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
    if(alternateCruise != null) {
      alternateCruise = alternateCruise.trim().toUpperCase(Locale.ENGLISH);
    }
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
