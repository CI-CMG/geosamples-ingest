package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEMPQC_INTERVAL")
@IdClass(IntervalPk.class)
public class TempQcIntervalEntity extends IntervalBase<TempQcSampleEntity>{


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IMLGS", nullable = false, insertable = false, updatable = false)
  private TempQcSampleEntity parentEntity;

  @Override
  protected void setParentEntityInternal(TempQcSampleEntity parentEntity) {
      this.parentEntity = parentEntity;
  }

  @Override
  public TempQcSampleEntity getParentEntity() {
    return parentEntity;
  }

}
