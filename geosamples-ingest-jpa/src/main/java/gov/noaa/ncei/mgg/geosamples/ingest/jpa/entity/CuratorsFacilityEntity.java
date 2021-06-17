package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;




/*
	INST_CODE VARCHAR2(3),
	FACILITY_CODE VARCHAR2(10) not null
		constraint CURATORS_FACILITY_PK
			primary key,
	FACILITY VARCHAR2(50),
	ADDR_1 VARCHAR2(45),
	ADDR_2 VARCHAR2(45),
	ADDR_3 VARCHAR2(45),
	ADDR_4 VARCHAR2(45),
	CONTACT_1 VARCHAR2(45),
	CONTACT_2 VARCHAR2(45),
	CONTACT_3 VARCHAR2(45),
	EMAIL_LINK VARCHAR2(45),
	URL_LINK VARCHAR2(45),
	FTP_LINK VARCHAR2(45),
	OTHER_LINK VARCHAR2(45),
	FACILITY_COMMENT VARCHAR2(2000),
	LAST_UPDATE NUMBER(8),
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1)


 */
@Entity
@Table(name = "CURATORS_FACILITY")
public class CuratorsFacilityEntity {

  @Id
  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
  private String facilityCode;

  @Column(name = "INST_CODE", length = 3)
  private String instCode;

  @Column(name = "FACILITY", length = 50)
  private String facility;

  @Column(name = "ADDR_1", length = 45)
  private String addr1;

  @Column(name = "ADDR_2", length = 45)
  private String addr2;

  @Column(name = "ADDR_3", length = 45)
  private String addr3;

  @Column(name = "ADDR_4", length = 45)
  private String addr4;

  @Column(name = "EMAIL_LINK", length = 45)
  private String emailLink;

  @Column(name = "URL_LINK", length = 45)
  private String urlLink;

  @Column(name = "FTP_LINK", length = 45)
  private String ftpLink;

  @Column(name = "OTHER_LINK", length = 45)
  private String otherLink;

  @Column(name = "FACILITY_COMMENT", length = 2000)
  private String facilityComment;

  @Column(name = "LAST_UPDATE", length = 2000)
  private Integer lastUpdate;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsFacilityEntity that = (CuratorsFacilityEntity) o;
    return Objects.equals(facilityCode, that.facilityCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilityCode);
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  public String getInstCode() {
    return instCode;
  }

  public void setInstCode(String instCode) {
    this.instCode = instCode;
  }

  public String getFacility() {
    return facility;
  }

  public void setFacility(String facility) {
    this.facility = facility;
  }

  public String getAddr1() {
    return addr1;
  }

  public void setAddr1(String addr1) {
    this.addr1 = addr1;
  }

  public String getAddr2() {
    return addr2;
  }

  public void setAddr2(String addr2) {
    this.addr2 = addr2;
  }

  public String getAddr3() {
    return addr3;
  }

  public void setAddr3(String addr3) {
    this.addr3 = addr3;
  }

  public String getAddr4() {
    return addr4;
  }

  public void setAddr4(String addr4) {
    this.addr4 = addr4;
  }

  public String getEmailLink() {
    return emailLink;
  }

  public void setEmailLink(String emailLink) {
    this.emailLink = emailLink;
  }

  public String getUrlLink() {
    return urlLink;
  }

  public void setUrlLink(String urlLink) {
    this.urlLink = urlLink;
  }

  public String getFtpLink() {
    return ftpLink;
  }

  public void setFtpLink(String ftpLink) {
    this.ftpLink = ftpLink;
  }

  public String getOtherLink() {
    return otherLink;
  }

  public void setOtherLink(String otherLink) {
    this.otherLink = otherLink;
  }

  public String getFacilityComment() {
    return facilityComment;
  }

  public void setFacilityComment(String facilityComment) {
    this.facilityComment = facilityComment;
  }

  public Integer getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Integer lastUpdate) {
    this.lastUpdate = lastUpdate;
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
}
