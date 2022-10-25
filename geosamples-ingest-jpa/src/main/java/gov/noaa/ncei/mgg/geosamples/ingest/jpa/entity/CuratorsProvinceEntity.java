package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
	PROVINCE_CODE VARCHAR2(2),
	PROVINCE VARCHAR2(35) not null
		constraint CURATORS_PROV_PK
			primary key,
	PROVINCE_COMMENT VARCHAR2(40),
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "CURATORS_PROVINCE")
public class CuratorsProvinceEntity {


  @Id
  @Column(name = "PROVINCE", nullable = false, length = 35)
  private String province;

  @Column(name = "PROVINCE_CODE", length = 2)
  private String provinceCode;

  @Column(name = "PROVINCE_COMMENT", length = 40)
  private String provinceComment;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "province")
  private List<CuratorsSampleTsqpEntity> samples = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsProvinceEntity that = (CuratorsProvinceEntity) o;
    return Objects.equals(province, that.province);
  }

  @Override
  public int hashCode() {
    return Objects.hash(province);
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getProvinceComment() {
    return provinceComment;
  }

  public void setProvinceComment(String provinceComment) {
    this.provinceComment = provinceComment;
  }

  public boolean isPublish() {
    return publish.equals("Y");
  }

  public void setPublish(boolean publish) {
    this.publish = publish ? "Y" : "N";
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
