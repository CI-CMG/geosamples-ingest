package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class GeosamplesUserAuthorityEntityPk implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "USER_NAME", nullable = false, length = 200)
  private String userName;
  @Id
  @Column(name = "AUTHORITY_NAME", nullable = false, length = 100)
  private String authorityName;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String username) {
    this.userName = username;
  }

  public String getAuthorityName() {
    return authorityName;
  }

  public void setAuthorityName(String authorityName) {
    this.authorityName = authorityName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeosamplesUserAuthorityEntityPk that = (GeosamplesUserAuthorityEntityPk) o;
    return Objects.equals(userName, that.userName) && Objects.equals(authorityName, that.authorityName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, authorityName);
  }
}
