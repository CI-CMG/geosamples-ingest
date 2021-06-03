package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	WEATH_META_CODE VARCHAR2(1),
	WEATH_META VARCHAR2(30) not null
		constraint CURATORS_WEATH_META_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */
@Entity
@Table(name = "CURATORS_WEATH_META")
public class CuratorsWeathMetaEntity {


  @Id
  @Column(name = "WEATH_META", nullable = false, length = 30)
  private String weathMeta;

  public String getWeathMeta() {
    return weathMeta;
  }

  public void setWeathMeta(String weathMeta) {
    this.weathMeta = weathMeta;
  }
}
