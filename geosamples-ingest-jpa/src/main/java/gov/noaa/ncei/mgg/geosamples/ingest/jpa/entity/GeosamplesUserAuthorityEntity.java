package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GEOSAMPLES_USER_AUTHORITY")
@IdClass(GeosamplesUserAuthorityEntityPk.class)
public class GeosamplesUserAuthorityEntity implements EntityWithId<GeosamplesUserAuthorityEntityPk> {


  @Id
  @Column(name = "USERNAME", nullable = false, length = 200)
  private String username;
  @Id
  @Column(name = "AUTHORITY_NAME", nullable = false, length = 100)
  private String authorityName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USERNAME", nullable = false, insertable = false, updatable = false)
  private GeosamplesUserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "AUTHORITY_NAME", nullable = false, insertable = false, updatable = false)
  private GeosamplesAuthorityEntity authority;

  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

  @Override
  public GeosamplesUserAuthorityEntityPk getId() {
    GeosamplesUserAuthorityEntityPk id = new GeosamplesUserAuthorityEntityPk();
    id.setUsername(username);
    id.setAuthorityName(authorityName);
    return id;
  }

  public GeosamplesUserEntity getUser() {
    return user;
  }

  public void setUser(GeosamplesUserEntity user) {
    this.user = user;
    if (user == null) {
      this.username = null;
    } else {
      this.username = user.getUsername();
    }
  }

  public GeosamplesAuthorityEntity getAuthority() {
    return authority;
  }

  public void setAuthority(GeosamplesAuthorityEntity authority) {
    this.authority = authority;
    if (authority == null) {
      this.authorityName = null;
    } else {
      this.authorityName = authority.getAuthorityName();
    }
  }

  public String getUsername() {
    return username;
  }

  public String getAuthorityName() {
    return authorityName;
  }
}
