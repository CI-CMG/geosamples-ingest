package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GEOSAMPLES_AUTHORITY")
public class GeosamplesAuthorityEntity implements EntityWithId<String> {

  @Id
  @Column(name = "AUTHORITY_NAME", nullable = false, length = 100)
  private String authorityName;

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
    return authorityName;
  }

  public String getAuthorityName() {
    return authorityName;
  }

  public void setAuthorityName(String authorityName) {
    this.authorityName = authorityName;
  }
}
