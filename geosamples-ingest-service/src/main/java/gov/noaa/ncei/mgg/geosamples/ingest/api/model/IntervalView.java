package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.util.ArrayList;
import java.util.List;

@Sortable({
    "interval",
    "approvalState",
})
public class IntervalView extends ProviderIntervalView {
  private ApprovalState approvalState;

  private Boolean publish;

  public ApprovalState getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalState approvalState) {
    this.approvalState = approvalState;
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }
}
