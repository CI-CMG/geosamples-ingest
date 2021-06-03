package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	AGE_CODE VARCHAR2(2),
	AGE VARCHAR2(20) not null
		constraint CURATORS_AGE_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */
@Entity
@Table(name = "CURATORS_AGE")
public class CuratorsAgeEntity {


  @Id
  @Column(name = "AGE", nullable = false, length = 20)
  private String age;

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }
}
