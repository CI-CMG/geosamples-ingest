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
}
