package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_STORAGE_METH")
public class CuratorsStorageMethEntity {

  /*
STORAGE_METH VARCHAR2(35) not null
		constraint CURATORS_STORAGE_PK
			primary key,
   */
  @Id
  @Column(name = "STORAGE_METH", nullable = false, length = 35)
  private String storageMeth;

}
