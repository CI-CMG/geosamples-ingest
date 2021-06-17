package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CURATORS_INTERVAL")
@IdClass(IntervalPk.class)
public class CuratorsIntervalEntity extends IntervalBase<CuratorsSampleTsqpEntity> {


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IMLGS", nullable = false, insertable = false, updatable = false)
  private CuratorsSampleTsqpEntity parentEntity;

  @Override
  public CuratorsSampleTsqpEntity getParentEntity() {
    return parentEntity;
  }

  @Override
  protected void setParentEntityInternal(CuratorsSampleTsqpEntity parentEntity) {
    this.parentEntity = parentEntity;
  }
}
