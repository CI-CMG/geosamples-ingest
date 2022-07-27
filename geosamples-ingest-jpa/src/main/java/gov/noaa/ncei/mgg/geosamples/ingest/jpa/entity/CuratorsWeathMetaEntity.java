package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
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

  @Column(name = "WEATH_META_CODE", length = 1)
  private String weathMetaCode;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsWeathMetaEntity that = (CuratorsWeathMetaEntity) o;
    return Objects.equals(weathMeta, that.weathMeta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weathMeta);
  }

  public String getWeathMeta() {
    return weathMeta;
  }

  public void setWeathMeta(String weathMeta) {
    this.weathMeta = weathMeta;
  }

  public String getWeathMetaCode() {
    return weathMetaCode;
  }

  public void setWeathMetaCode(String weathMetaCode) {
    this.weathMetaCode = weathMetaCode;
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
