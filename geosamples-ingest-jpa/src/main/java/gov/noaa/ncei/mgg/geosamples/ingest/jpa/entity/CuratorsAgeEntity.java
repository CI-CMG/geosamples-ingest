package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
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

  @Column(name = "AGE_CODE", length = 2)
  private String ageCode;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsAgeEntity that = (CuratorsAgeEntity) o;
    return Objects.equals(age, that.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(age);
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getAgeCode() {
    return ageCode;
  }

  public void setAgeCode(String ageCode) {
    this.ageCode = ageCode;
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

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
