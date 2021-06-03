package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLATFORM_MASTER")
public class PlatformMasterEntity {

  /*
	PLATFORM VARCHAR2(50) not null
		constraint PLATFORM_MASTER_PK
			primary key,
   */
  @Id
  @Column(name = "PLATFORM", nullable = false, length = 50)
  private String platform;

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }
}
