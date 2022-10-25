package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_FACILITY")
public class CuratorsFacilityEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_FACILITY_SEQ")
  @SequenceGenerator(name = "CURATORS_FACILITY_SEQ", sequenceName = "CURATORS_FACILITY_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "FACILITY_CODE", nullable = false, length = 10)
  private String facilityCode;

  @Column(name = "INST_CODE", nullable = false,length = 3)
  private String instCode;

  @Column(name = "FACILITY", length = 100)
  private String facility;

  @Column(name = "ADDR_1", length = 100)
  private String addr1;

  @Column(name = "ADDR_2", length = 45)
  private String addr2;

  @Column(name = "ADDR_3", length = 45)
  private String addr3;

  @Column(name = "ADDR_4", length = 45)
  private String addr4;

  @Column(name = "CONTACT_1", length = 45)
  private String contact1;

  @Column(name = "CONTACT_2", length = 45)
  private String contact2;

  @Column(name = "CONTACT_3", length = 45)
  private String contact3;

  @Column(name = "EMAIL_LINK", length = 45)
  private String emailLink;

  @Column(name = "URL_LINK", length = 45)
  private String urlLink;

  @Column(name = "FTP_LINK", length = 45)
  private String ftpLink;

  @Column(name = "DOI_LINK", length = 45)
  private String doiLink;

  @Column(name = "FACILITY_COMMENT", length = 2000)
  private String facilityComment;

  @Column(name = "LAST_UPDATE", nullable = false)
  private Instant lastUpdate;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "facility")
  private List<CuratorsCruiseFacilityEntity> cruiseFacilities = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
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

  public String getDoiLink() {
    return doiLink;
  }

  public void setDoiLink(String doiLink) {
    this.doiLink = doiLink;
  }

  public String getFacilityComment() {
    return facilityComment;
  }

  public void setFacilityComment(String facilityComment) {
    this.facilityComment = facilityComment;
  }

  public Instant getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Instant lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public boolean isPublish() {
    return publish.equals("Y");
  }

  public void setPublish(boolean publish) {
    this.publish = publish ? "Y" : "N";
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContact1() {
    return contact1;
  }

  public void setContact1(String contact1) {
    this.contact1 = contact1;
  }

  public String getContact2() {
    return contact2;
  }

  public void setContact2(String contact2) {
    this.contact2 = contact2;
  }

  public String getContact3() {
    return contact3;
  }

  public void setContact3(String contact3) {
    this.contact3 = contact3;
  }
}
