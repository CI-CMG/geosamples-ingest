package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
ROCK_LITH_CODE VARCHAR2(2),
	ROCK_LITH VARCHAR2(100) not null
		constraint CURATORS_RLITH_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */
@Entity
@Table(name = "CURATORS_ROCK_LITH")
public class CuratorsRockLithEntity {


  @Id
  @Column(name = "ROCK_LITH", nullable = false, length = 100)
  private String rockLith;

  public String getRockLith() {
    return rockLith;
  }

  public void setRockLith(String rockLith) {
    this.rockLith = rockLith;
  }
}
