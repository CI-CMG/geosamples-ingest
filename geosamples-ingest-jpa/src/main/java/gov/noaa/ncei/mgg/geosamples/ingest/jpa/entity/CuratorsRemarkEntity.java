package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	REMARK_CODE VARCHAR2(1),
	REMARK VARCHAR2(70) not null
		constraint CURATORS_REMARK_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */
@Entity
@Table(name = "CURATORS_REMARK")
public class CuratorsRemarkEntity {


  @Id
  @Column(name = "REMARK", nullable = false, length = 70)
  private String remark;

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
