package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	MASTER_ID NUMBER(8),
	PLATFORM VARCHAR2(50) not null
		constraint PLATFORM_MASTER_PK
			primary key,
	DATE_ADDED DATE,
	PUBLISH VARCHAR2(1),
	PREVIOUS_STATE VARCHAR2(1),
	PREFIX VARCHAR2(30),
	ICES_CODE VARCHAR2(4),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "PLATFORM_MASTER")
public class PlatformMasterEntity {

  @Id
  @Column(name = "PLATFORM", nullable = false, length = 50)
  private String platform;

  @Column(name = "MASTER_ID")
  private Integer masterId;

  @Column(name = "DATE_ADDED")
  private Instant dateAdded;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "PREFIX", length = 30)
  private String prefix;

  @Column(name = "ICES_CODE", length = 4)
  private String icesCode;

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
    PlatformMasterEntity that = (PlatformMasterEntity) o;
    return Objects.equals(platform, that.platform);
  }

  @Override
  public int hashCode() {
    return Objects.hash(platform);
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Integer getMasterId() {
    return masterId;
  }

  public void setMasterId(Integer masterId) {
    this.masterId = masterId;
  }

  public Instant getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Instant dateAdded) {
    this.dateAdded = dateAdded;
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

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getIcesCode() {
    return icesCode;
  }

  public void setIcesCode(String icesPrefix) {
    this.icesCode = icesPrefix;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
