package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_CRUISE_PLATFORM")
public class CuratorsCruisePlatformEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_CRUISE_PLATFORM_SEQ")
  @SequenceGenerator(name = "CURATORS_CRUISE_PLATFORM_SEQ", sequenceName = "CURATORS_CRUISE_PLATFORM_SEQ", allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CRUISE_ID", nullable = false)
  private CuratorsCruiseEntity cruise;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PLATFORM_ID", nullable = false)
  private PlatformMasterEntity platform;

  @Column(name = "PUBLISH", nullable = false, length = 1)
  private String publish = "Y";

  @OneToMany(mappedBy = "cruisePlatform", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CuratorsCruiseLinksEntity> cruiseLinks = new ArrayList<>();

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "cruisePlatform")
  private List<CuratorsSampleTsqpEntity> samples = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CuratorsCruiseEntity getCruise() {
    return cruise;
  }

  public void setCruise(CuratorsCruiseEntity cruise) {
    this.cruise = cruise;
  }

  public PlatformMasterEntity getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformMasterEntity platform) {
    this.platform = platform;
  }

  public boolean isPublish() {
    return publish.equals("Y");
  }

  public void setPublish(boolean publish) {
    this.publish = publish ? "Y" : "N";
  }

  public void addCruiseLink(CuratorsCruiseLinksEntity link) {
    EntityUtil.addAndParent(this, cruiseLinks, link, this::removeCruiseLink, link::setCruisePlatform);
  }

  public void removeCruiseLink(CuratorsCruiseLinksEntity link) {
    EntityUtil.removeAndOrphan(cruiseLinks, link, link::setCruisePlatform);
  }

  public void clearCruiseLinks() {
    EntityUtil.clearAndOrphan(cruiseLinks, CuratorsCruiseLinksEntity::setCruisePlatform);
  }

  public List<CuratorsCruiseLinksEntity> getCruiseLinks() {
    return Collections.unmodifiableList(cruiseLinks);
  }
}
