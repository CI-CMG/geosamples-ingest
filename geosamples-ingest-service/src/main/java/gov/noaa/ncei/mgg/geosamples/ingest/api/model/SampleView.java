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
}
