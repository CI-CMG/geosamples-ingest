package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class GeosamplesUserAuthorityEntityPk implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "USERNAME", nullable = false, length = 200)
  private String username;
  @Id
  @Column(name = "AUTHORITY_NAME", nullable = false, length = 100)
  private String authorityName;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
    return Objects.equals(username, that.username) && Objects.equals(authorityName, that.authorityName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, authorityName);
  }
}
