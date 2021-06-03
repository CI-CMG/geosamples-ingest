package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
	LITHOLOGY_CODE VARCHAR2(1),
	LITHOLOGY VARCHAR2(40) not null
		constraint CUR_LITH_PK
			primary key,
	OLD_LITHOLOGY VARCHAR2(40),
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "CURATORS_LITHOLOGY")
public class CuratorsLithologyEntity {

  /*
	DEVICE VARCHAR2(30) not null
		constraint CURATORS_DEVICE_PK
			primary key,
   */
  @Id
  @Column(name = "LITHOLOGY", nullable = false, length = 40)
  private String lithology;

  public String getLithology() {
    return lithology;
  }

  public void setLithology(String lithology) {
    this.lithology = lithology;
  }
}
