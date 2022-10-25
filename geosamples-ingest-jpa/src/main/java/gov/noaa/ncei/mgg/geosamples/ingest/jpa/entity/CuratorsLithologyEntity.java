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
	LITHOLOGY_CODE VARCHAR2(1),
	LITHOLOGY VARCHAR2(40) not null
		constraint CUR_LITH_PK
			primary key,
	OLD_LITHOLOGY VARCHAR2(40),
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "CURATORS_LITHOLOGY")
public class CuratorsLithologyEntity {


  @Id
  @Column(name = "LITHOLOGY", length = 40)
  private String lithology;

  @Column(name = "LITHOLOGY_CODE", length = 1)
  private String lithologyCode;

  @Column(name = "OLD_LITHOLOGY", length = 40)
  private String oldLithology;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "lith1")
  private List<CuratorsIntervalEntity> intervalLith1 = new ArrayList<>();

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "lith2")
  private List<CuratorsIntervalEntity> intervalLith2 = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsLithologyEntity that = (CuratorsLithologyEntity) o;
    return Objects.equals(lithology, that.lithology);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lithology);
  }

  public String getLithology() {
    return lithology;
  }

  public void setLithology(String lithology) {
    this.lithology = lithology;
  }

  public String getLithologyCode() {
    return lithologyCode;
  }

  public void setLithologyCode(String lithologyCode) {
    this.lithologyCode = lithologyCode;
  }

  public String getOldLithology() {
    return oldLithology;
  }

  public void setOldLithology(String oldLithology) {
    this.oldLithology = oldLithology;
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
