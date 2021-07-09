package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	MUNSELL_CODE VARCHAR2(10) not null
		constraint CURATORS_MUN_CODE_PK
			primary key,
	MUNSELL VARCHAR2(30),
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1)
 */

@Entity
@Table(name = "CURATORS_MUNSELL")
public class CuratorsMunsellEntity {


  @Id
  @Column(name = "MUNSELL_CODE", nullable = false, length = 10)
  private String munsellCode;

  @Column(name = "MUNSELL", length = 30)
  private String munsell;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsMunsellEntity that = (CuratorsMunsellEntity) o;
    return Objects.equals(munsellCode, that.munsellCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(munsellCode);
  }

  public String getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(String munsellCode) {
    this.munsellCode = munsellCode;
  }

  public String getMunsell() {
    return munsell;
  }

  public void setMunsell(String munsell) {
    this.munsell = munsell;
  }

  public String getPublish() {
    return publish;
  }

  public void setPublish(String publish) {
    this.publish = publish;
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

}
