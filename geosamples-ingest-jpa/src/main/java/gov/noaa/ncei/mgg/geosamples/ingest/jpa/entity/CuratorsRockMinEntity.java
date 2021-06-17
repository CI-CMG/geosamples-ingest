package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
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

  @Column(name = "ROCK_MIN_CODE", length = 1)
  private String rockMinCode;

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
    CuratorsRockMinEntity that = (CuratorsRockMinEntity) o;
    return Objects.equals(rockMin, that.rockMin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rockMin);
  }

  public String getRockMin() {
    return rockMin;
  }

  public void setRockMin(String rockMin) {
    this.rockMin = rockMin;
  }

  public String getRockMinCode() {
    return rockMinCode;
  }

  public void setRockMinCode(String rockMinCode) {
    this.rockMinCode = rockMinCode;
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
