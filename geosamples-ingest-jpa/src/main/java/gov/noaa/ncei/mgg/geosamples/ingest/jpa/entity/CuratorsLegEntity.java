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
@Table(name = "CURATORS_LEG")
public class CuratorsLegEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 0)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURATORS_LEG_SEQ")
  @SequenceGenerator(name = "CURATORS_LEG_SEQ", sequenceName = "CURATORS_LEG_SEQ", allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CRUISE_ID", nullable = false)
  private CuratorsCruiseEntity cruise;

  @Column(name = "LEG_NAME", length = 30)
  private String legName;

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
    CuratorsLegEntity that = (CuratorsLegEntity) o;
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

  public CuratorsCruiseEntity getCruise() {
    return cruise;
  }

  public void setCruise(CuratorsCruiseEntity cruise) {
    this.cruise = cruise;
  }

  public String getLegName() {
    return legName;
  }

  public void setLegName(String legName) {
    this.legName = legName;
  }

  public String getPublish() {
    return publish;
  }

  public void setPublish(String publish) {
    this.publish = publish;
  }
}
