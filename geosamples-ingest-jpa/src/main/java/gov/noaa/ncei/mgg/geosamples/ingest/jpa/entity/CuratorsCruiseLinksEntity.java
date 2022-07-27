package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
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

@Entity
@Table(name = "CURATORS_CRUISE_LINKS")
public class CuratorsCruiseLinksEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_CRUISE_LINKS_SEQ")
  @SequenceGenerator(name = "CURATORS_CRUISE_LINKS_SEQ", sequenceName = "CURATORS_CRUISE_LINKS_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "DATALINK", nullable = false, length = 500)
  private String datalink;

  @Column(name = "LINK_LEVEL", nullable = false, length = 30)
  private String linkLevel;

  @Column(name = "LINK_SOURCE", nullable = false, length = 30)
  private String linkSource;

  @Column(name = "LINK_TYPE", nullable = false, length = 30)
  private String linkType;

  @Column(name = "PUBLISH", nullable = false, length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CRUISE_PLATFORM_ID", nullable = false)
  private CuratorsCruisePlatformEntity cruisePlatform;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LEG_ID", nullable = false)
  private CuratorsLegEntity leg;

  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
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

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CuratorsCruisePlatformEntity getCruisePlatform() {
    return cruisePlatform;
  }

  public void setCruisePlatform(CuratorsCruisePlatformEntity cruisePlatform) {
    this.cruisePlatform = cruisePlatform;
  }

  public CuratorsLegEntity getLeg() {
    return leg;
  }

  public void setLeg(CuratorsLegEntity leg) {
    this.leg = leg;
  }
}
