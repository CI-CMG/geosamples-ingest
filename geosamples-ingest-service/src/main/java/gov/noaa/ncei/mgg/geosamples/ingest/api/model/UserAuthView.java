package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import java.util.List;

public class UserAuthView {

  private final String userName;
  private final String displayName;
  private final List<String> tokenAliases;
  private final List<String> authorities;

  public UserAuthView(UserView userView, List<String> authorities) {
    this.userName = userView.getUserName();
    this.displayName = userView.getDisplayName();
    this.tokenAliases = userView.getTokenAliases();
    this.authorities = authorities;
  }

  public String getUserName() {
    return userName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<String> getTokenAliases() {
    return tokenAliases;
  }

  public List<String> getAuthorities() {
    return authorities;
  }
}
