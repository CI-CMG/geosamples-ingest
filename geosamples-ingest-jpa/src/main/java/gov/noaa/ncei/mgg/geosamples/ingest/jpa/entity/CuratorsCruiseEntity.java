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

@Entity
@Table(name = "CURATORS_CRUISE")
public class CuratorsCruiseEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_CRUISE_SEQ")
  @SequenceGenerator(name = "CURATORS_CRUISE_SEQ", sequenceName = "CURATORS_CRUISE_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "CRUISE_NAME", length = 30)
  private String cruiseName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PLATFORM_ID", nullable = false)
  private PlatformMasterEntity platform;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FACILITY_ID", nullable = false)
  private CuratorsFacilityEntity facility;

  @Column(name = "PUBLISH", nullable = false, length = 1)
  private String publish;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsCruiseEntity that = (CuratorsCruiseEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return 1;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCruiseName() {
    return cruiseName;
  }

  public void setCruiseName(String cruiseName) {
    this.cruiseName = cruiseName;
  }

  public PlatformMasterEntity getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformMasterEntity platform) {
    this.platform = platform;
  }

  public CuratorsFacilityEntity getFacility() {
    return facility;
  }

  public void setFacility(CuratorsFacilityEntity facility) {
    this.facility = facility;
  }

  public String getPublish() {
    return publish;
  }

  public void setPublish(String publish) {
    this.publish = publish;
  }
}
