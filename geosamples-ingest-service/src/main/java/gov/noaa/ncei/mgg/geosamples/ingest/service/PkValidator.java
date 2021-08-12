package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import java.util.Set;

public class PkValidator {

  private final int index;
  private final String imlgs;
  private final String tempImlgs;
  private final int interval;
  private final boolean addedPk;
  private final IntervalPk pk;
  private final IntervalPk tempPk;

  public PkValidator(int index, String imlgs, int interval, Set<IntervalPk> pks) {
    this.index = index;
    this.imlgs = imlgs.replaceAll("^temp_", "");
    tempImlgs = "temp_" + this.imlgs;
    this.interval = interval;
    pk = new IntervalPk();
    pk.setImlgs(this.imlgs);
    pk.setInterval(this.interval);
    tempPk = new IntervalPk();
    tempPk.setImlgs(this.tempImlgs);
    tempPk.setInterval(this.interval);
    addedPk = pks.add(pk);
  }

  public int getIndex() {
    return index;
  }

  public String getImlgs() {
    return imlgs;
  }

  public String getTempImlgs() {
    return tempImlgs;
  }

  public int getInterval() {
    return interval;
  }

  public boolean isAddedPk() {
    return addedPk;
  }

  public IntervalPk getPk() {
    return pk;
  }

  public IntervalPk getTempPk() {
    return tempPk;
  }
}
