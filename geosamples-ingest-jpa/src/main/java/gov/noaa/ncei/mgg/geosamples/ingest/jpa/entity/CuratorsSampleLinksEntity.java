package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "CURATORS_SAMPLE_LINKS")
public class CuratorsSampleLinksEntity {


  @Column(name = "DATALINK", length = 500)
  private String datalink;

  @Column(name = "LINK_LEVEL", length = 30)
  private String linkLevel;

  @Column(name = "LINK_SOURCE", length = 30)
  private String linkSource;

  @Column(name = "LINK_TYPE", length = 30)
  private String linkType;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "IMLGS", length = 15, nullable = false)
  private String imlgs;

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_SAMPLE_LINKS_SEQ")
  @SequenceGenerator(name = "CURATORS_SAMPLE_LINKS_SEQ", sequenceName = "CURATORS_SAMPLE_LINKS_SEQ", allocationSize = 1)
  private Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsSampleLinksEntity that = (CuratorsSampleLinksEntity) o;
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


  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
