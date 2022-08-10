package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;

@Sortable({
    "userName",
    "displayName",
})
public class UserView {

  private String userName;
  private String displayName;
  private List<String> authorities = new ArrayList<>(0);
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

  public List<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    if(authorities == null) {
      authorities = new ArrayList<>(0);
    }
    this.authorities = authorities;
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
