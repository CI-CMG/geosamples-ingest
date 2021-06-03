package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_DEVICE")
public class CuratorsDeviceEntity {

  /*
	DEVICE VARCHAR2(30) not null
		constraint CURATORS_DEVICE_PK
			primary key,
   */
  @Id
  @Column(name = "DEVICE", nullable = false, length = 30)
  private String device;

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }
}
