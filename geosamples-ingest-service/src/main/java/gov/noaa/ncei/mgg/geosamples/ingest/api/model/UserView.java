package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Sortable({
    "userName",
    "displayName",
})
public class UserView {

  @NotBlank
  private String userName;
  @NotBlank
  private String displayName;
  @NotBlank
  private String role;

  private AttachedFacilityVIew facility;

  private List<String> tokenAliases = new ArrayList<>(0);

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getRole() {
    return role;
  }

  public String setRole(String role) {
    return this.role = role;
  }

  public AttachedFacilityVIew getFacility() {
    return facility;
  }

  public void setFacility(AttachedFacilityVIew facility) {
    this.facility = facility;
  }

  public List<String> getTokenAliases() {
    return tokenAliases;
  }

  public void setTokenAliases(List<String> tokenAliases) {
    if(tokenAliases == null) {
      tokenAliases = new ArrayList<>(0);
    }
    this.tokenAliases = tokenAliases;
  }
}
