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
	STORAGE_METH_CODE VARCHAR2(1),
	STORAGE_METH VARCHAR2(35) not null
		constraint CURATORS_STORAGE_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "CURATORS_STORAGE_METH")
public class CuratorsStorageMethEntity {


  @Id
  @Column(name = "STORAGE_METH", nullable = false, length = 35)
  private String storageMeth;

  @Column(name = "STORAGE_METH_CODE", length = 1)
  private String storageMethCode;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "storageMeth")
  private List<CuratorsSampleTsqpEntity> samples = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsStorageMethEntity that = (CuratorsStorageMethEntity) o;
    return Objects.equals(storageMeth, that.storageMeth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storageMeth);
  }

  public String getStorageMeth() {
    return storageMeth;
  }

  public void setStorageMeth(String storageMeth) {
    this.storageMeth = storageMeth;
  }

  public String getStorageMethCode() {
    return storageMethCode;
  }

  public void setStorageMethCode(String storageMethCode) {
    this.storageMethCode = storageMethCode;
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
