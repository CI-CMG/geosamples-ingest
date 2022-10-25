package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PLATFORM_MASTER")
public class PlatformMasterEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLATFORM_MASTER_SEQ")
  @SequenceGenerator(name = "PLATFORM_MASTER_SEQ", sequenceName = "PLATFORM_MASTER_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "PLATFORM", unique = true, nullable = false, length = 50)
  private String platform;

  // this is set by the DB
  @Column(name = "PLATFORM_NORMALIZED", unique = true, nullable = false, length = 50)
  private String platformNormalized;

  @Column(name = "MASTER_ID")
  private Integer masterId;

  @Column(name = "DATE_ADDED")
  private Instant dateAdded;

  @Column(name = "PUBLISH", length = 1)
  private String publish = "Y";

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "PREFIX", length = 30)
  private String prefix;

  @Column(name = "ICES_CODE", length = 4)
  private String icesCode;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  // no getters and setters on purpose, this needs to be here to generate a JPA query only
  @OneToMany(mappedBy = "platform")
  private List<CuratorsCruisePlatformEntity> cruisePlatforms = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Integer getMasterId() {
    return masterId;
  }

  public void setMasterId(Integer masterId) {
    this.masterId = masterId;
  }

  public Instant getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Instant dateAdded) {
    this.dateAdded = dateAdded;
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

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getIcesCode() {
    return icesCode;
  }

  public void setIcesCode(String icesPrefix) {
    this.icesCode = icesPrefix;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlatformNormalized() {
    return platformNormalized;
  }
}
