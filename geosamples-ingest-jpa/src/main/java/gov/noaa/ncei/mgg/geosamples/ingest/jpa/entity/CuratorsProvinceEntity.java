package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_PROVINCE")
public class CuratorsProvinceEntity {

  /*
	PROVINCE VARCHAR2(35) not null
		constraint CURATORS_PROV_PK
			primary key,
   */
  @Id
  @Column(name = "PROVINCE", nullable = false, length = 35)
  private String province;

}
