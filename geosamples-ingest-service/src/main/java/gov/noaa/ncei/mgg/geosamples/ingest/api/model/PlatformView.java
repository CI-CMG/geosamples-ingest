package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "platform",
    "masterId",
    "icesCode"
})
public class PlatformView extends ProviderPlatformView {

  private String createdBy;

  private ApprovalState approvalState;

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public ApprovalState getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalState approvalState) {
    this.approvalState = approvalState;
  }
}
