package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

@Sortable({"roleName"})
public class RoleView {

  private Long id;
  private Long version;
  @NotBlank
  private String roleName;
  private List<String> authorities = new ArrayList<>(0);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public List<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    if (authorities == null) {
      authorities = new ArrayList<>(0);
    }
    this.authorities = authorities;
  }
}
