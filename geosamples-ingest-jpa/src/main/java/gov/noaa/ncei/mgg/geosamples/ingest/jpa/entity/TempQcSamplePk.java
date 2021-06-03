package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

/*
FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE
 */
public class TempQcSamplePk implements Serializable {

  private static final long serialVersionUID = 1L;

  /*
  	FACILITY_CODE VARCHAR2(10) not null
		constraint TEMPQC_SAMPLE_FACILITY_FK
			references CURATORS_FACILITY,
   */
  @Id
  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
  private String facilityCode;

  /*
  PLATFORM VARCHAR2(50) not null
		constraint TEMPQC_SAMPLE_PLATFORM_FK
			references PLATFORM_MASTER,
   */
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

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
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

  public String getPlatformId() {
    return platformId;
  }

  public void setPlatformId(String platformId) {
    this.platformId = platformId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TempQcSamplePk that = (TempQcSamplePk) o;
    return Objects.equals(facilityCode, that.facilityCode) && Objects.equals(platformId, that.platformId) && Objects
        .equals(cruise, that.cruise) && Objects.equals(sample, that.sample) && Objects.equals(deviceId, that.deviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilityCode, platformId, cruise, sample, deviceId);
  }
}
