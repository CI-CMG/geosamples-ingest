package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_CRUISE")
public class CuratorsCruiseEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_CRUISE_SEQ")
  @SequenceGenerator(name = "CURATORS_CRUISE_SEQ", sequenceName = "CURATORS_CRUISE_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "CRUISE_NAME", length = 30)
  private String cruiseName;

  @Column(name = "PUBLISH", nullable = false, length = 1)
  private String publish = "Y";

  @Column(name = "YEAR", nullable = false)
  private Short year;

  @OneToMany(mappedBy = "cruise", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CuratorsCruisePlatformEntity> platformMappings = new ArrayList<>();

  @OneToMany(mappedBy = "cruise", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CuratorsCruiseFacilityEntity> facilityMappings = new ArrayList<>();

  @OneToMany(mappedBy = "cruise", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CuratorsLegEntity> legs = new ArrayList<>();

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "cruise")
  private List<CuratorsSampleTsqpEntity> samples = new ArrayList<>();

  public void addPlatformMapping(CuratorsCruisePlatformEntity platformMapping) {
    EntityUtil.addAndParent(this, platformMappings, platformMapping, this::removePlatformMapping, platformMapping::setCruise);
  }

  public void removePlatformMapping(CuratorsCruisePlatformEntity platformMapping) {
    EntityUtil.removeAndOrphan(platformMappings, platformMapping, platformMapping::setCruise);
  }

  public void clearPlatformMappings() {
    EntityUtil.clearAndOrphan(platformMappings, CuratorsCruisePlatformEntity::setCruise);
  }

  public List<CuratorsCruisePlatformEntity> getPlatformMappings() {
    return Collections.unmodifiableList(platformMappings);
  }

  public void addFacilityMapping(CuratorsCruiseFacilityEntity facilityMapping) {
    EntityUtil.addAndParent(this, facilityMappings, facilityMapping, this::removeFacilityMapping, facilityMapping::setCruise);
  }

  public void removeFacilityMapping(CuratorsCruiseFacilityEntity facilityMapping) {
    EntityUtil.removeAndOrphan(facilityMappings, facilityMapping, facilityMapping::setCruise);
  }

  public void clearFacilityMappings() {
    EntityUtil.clearAndOrphan(facilityMappings, CuratorsCruiseFacilityEntity::setCruise);
  }

  public List<CuratorsCruiseFacilityEntity> getFacilityMappings() {
    return Collections.unmodifiableList(facilityMappings);
  }

  public void addLeg(CuratorsLegEntity leg) {
    EntityUtil.addAndParent(this, legs, leg, this::removeLeg, leg::setCruise);
  }

  public void removeLeg(CuratorsLegEntity leg) {
    EntityUtil.removeAndOrphan(legs, leg, leg::setCruise);
  }

  public void clearLegs() {
    EntityUtil.clearAndOrphan(legs, CuratorsLegEntity::setCruise);
  }

  public List<CuratorsLegEntity> getLegs() {
    return Collections.unmodifiableList(legs);
  }

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

  public String getCruiseName() {
    return cruiseName;
  }

  public void setCruiseName(String cruiseName) {
    this.cruiseName = cruiseName;
  }

  public Short getYear() {
    return year;
  }

  public void setYear(Short year) {
    this.year = year;
  }

  public boolean isPublish() {
    return publish.equals("Y");
  }

  public void setPublish(boolean publish) {
    this.publish = publish ? "Y" : "N";
  }
}
