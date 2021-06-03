package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	ROCK_MIN_CODE VARCHAR2(1),
	ROCK_MIN VARCHAR2(35) not null
		constraint CURATORS_MIN_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */
@Entity
@Table(name = "CURATORS_ROCK_MIN")
public class CuratorsRockMinEntity {


  @Id
  @Column(name = "ROCK_MIN", nullable = false, length = 35)
  private String rockMin;

  public String getRockMin() {
    return rockMin;
  }

  public void setRockMin(String rockMin) {
    this.rockMin = rockMin;
  }
}
