package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_CRUISE_LINKS")
public class CuratorsCruiseLinksEntity {

  @Column(name = "DATALINK", nullable = false, length = 500)
  private String datalink;

  @Column(name = "LINK_LEVEL", nullable = false, length = 30)
  private String linkLevel;

  @Column(name = "LINK_SOURCE", nullable = false, length = 30)
  private String linkSource;

  @Column(name = "LINK_TYPE", nullable = false, length = 30)
  private String linkType;

  @Column(name = "PUBLISH", nullable = false, length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_CRUISE_LINKS_SEQ")
  @SequenceGenerator(name = "CURATORS_CRUISE_LINKS_SEQ", sequenceName = "CURATORS_CRUISE_LINKS_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "CRUISE_ID", nullable = false)
  private Long cruiseId;

  @Column(name = "LEG_ID")
  private Long legId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsCruiseLinksEntity that = (CuratorsCruiseLinksEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return 1;
  }

  public String getDatalink() {
    return datalink;
  }

  public void setDatalink(String datalink) {
    this.datalink = datalink;
  }

  public String getLinkLevel() {
    return linkLevel;
  }

  public void setLinkLevel(String linkLevel) {
    this.linkLevel = linkLevel;
  }

  public String getLinkSource() {
    return linkSource;
  }

  public void setLinkSource(String linkSource) {
    this.linkSource = linkSource;
  }

  public String getLinkType() {
    return linkType;
  }

  public void setLinkType(String linkType) {
    this.linkType = linkType;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(Long cruiseId) {
    this.cruiseId = cruiseId;
  }

  public Long getLegId() {
    return legId;
  }

  public void setLegId(Long legId) {
    this.legId = legId;
  }
}
