package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;

@Sortable({
    "imlgs",
    "cruise",
    "sample",
    "facilityCode",
    "platform",
    "deviceCode",
    "approvalState",
})
public class SampleView extends ProviderSampleView {
  private ApprovalState approvalState;

  private String facilityCode;

  private Boolean publish;

  private String showSampl;

  private Double coredDiamMm;

  private Double coredLengthMm;

  public ApprovalState getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalState approvalState) {
    this.approvalState = approvalState;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }

  public String getShowSampl() {
    return showSampl;
  }

  public void setShowSampl(String showSampl) {
    this.showSampl = showSampl;
  }

  public Double getCoredDiamMm() {
    return coredDiamMm;
  }

  public void setCoredDiamMm(Double coredDiamMm) {
    this.coredDiamMm = coredDiamMm;
  }

  public Double getCoredLengthMm() {
    return coredLengthMm;
  }

  public void setCoredLengthMm(Double coredLengthMm) {
    this.coredLengthMm = coredLengthMm;
  }
}
