package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
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

  @Column(name = "ROCK_LITH_CODE", length = 2)
  private String rockLithCode;

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
    CuratorsRockLithEntity that = (CuratorsRockLithEntity) o;
    return Objects.equals(rockLith, that.rockLith);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rockLith);
  }

  public String getRockLith() {
    return rockLith;
  }

  public void setRockLith(String rockLith) {
    this.rockLith = rockLith;
  }

  public String getRockLithCode() {
    return rockLithCode;
  }

  public void setRockLithCode(String rockLithCode) {
    this.rockLithCode = rockLithCode;
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
