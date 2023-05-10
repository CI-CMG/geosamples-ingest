package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Sortable({
    "id",
    "year",
    "cruiseName",
    "publish"
})
public class CruiseView extends ProviderCruiseView {
  private ApprovalState approvalState;

  private List<String> facilityCodes = new ArrayList<>(0);

  private Boolean publish;

  public ApprovalState getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(ApprovalState approvalState) {
    this.approvalState = approvalState;
  }

  public List<String> getFacilityCodes() {
    return facilityCodes;
  }

  public void setFacilityCodes(List<String> facilityCodes) {
    if (facilityCodes == null) {
      facilityCodes = new ArrayList<>(0);
    }
    this.facilityCodes = facilityCodes.stream().map(v -> v.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }
}
