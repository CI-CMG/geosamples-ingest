package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_FACILITY")
public class CuratorsFacilityEntity {

  /*
  FACILITY_CODE VARCHAR2(10) not null
		constraint CURATORS_FACILITY_PK
			primary key,
   */
  @Id
  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
  private String facilityCode;

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }
}
