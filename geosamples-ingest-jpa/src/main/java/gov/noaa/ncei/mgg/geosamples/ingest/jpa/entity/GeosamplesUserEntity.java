package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "GEOSAMPLES_USER")
public class GeosamplesUserEntity implements EntityWithId<String> {

  @Id
  @Column(name = "USER_NAME", nullable = false, length = 200)
  private String userName;

  @Version
  @Column(name = "VERSION", nullable = false)
  private int version;

  @Column(name = "DISPLAY_NAME", nullable = false, length = 200)
  private String displayName;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GeosamplesUserAuthorityEntity> userAuthorities = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GeosamplesTokenEntity> tokens = new ArrayList<>();

  public void addUserAuthority(GeosamplesUserAuthorityEntity userAuthority) {
    EntityUtil.addAndParent(this, userAuthorities, userAuthority, this::removeUserAuthority, userAuthority::setUser);
  }

  public void removeUserAuthority(GeosamplesUserAuthorityEntity userAuthority) {
    EntityUtil.removeAndOrphan(userAuthorities, userAuthority, userAuthority::setUser);
  }

  public void clearUserAuthorities() {
    EntityUtil.clearAndOrphan(userAuthorities, GeosamplesUserAuthorityEntity::setUser);
  }

  public List<GeosamplesUserAuthorityEntity> getUserAuthorities() {
    return Collections.unmodifiableList(userAuthorities);
  }

  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

  @Override
  public String getId() {
    return userName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String username) {
    this.userName = username;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<GeosamplesTokenEntity> getTokens() {
    return Collections.unmodifiableList(tokens);
  }

  public void addToken(GeosamplesTokenEntity token) {
    EntityUtil.addAndParent(this, tokens, token, this::removeToken, token::setUser);
  }

  public void removeToken(GeosamplesTokenEntity token) {
    EntityUtil.removeAndOrphan(tokens, token, token::setUser);
  }

  public void clearTokens() {
    EntityUtil.clearAndOrphan(tokens, GeosamplesTokenEntity::setUser);
  }
}
