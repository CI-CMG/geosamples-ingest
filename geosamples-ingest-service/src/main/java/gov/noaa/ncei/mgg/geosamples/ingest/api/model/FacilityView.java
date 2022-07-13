package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "facilityCode",
    "instCode",
    "facility"
})
public class FacilityView {


  @NotBlank
  @Size(max = 10)
  private String facilityCode;

  @Size(max = 3)
  private String instCode;

  @NotBlank
  @Size(max = 100)
  private String facility;

  @Size(max = 100)
  private String addr1;

  @Size(max = 45)
  private String addr2;

  @Size(max = 45)
  private String addr3;

  @Size(max = 45)
  private String addr4;

  @Size(max = 45)
  private String emailLink;

  @Size(max = 45)
  private String urlLink;

  @Size(max = 45)
  private String ftpLink;

  @Size(max = 45)
  private String doiLink;

  @Size(max = 2000)
  private String facilityComment;


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
}
