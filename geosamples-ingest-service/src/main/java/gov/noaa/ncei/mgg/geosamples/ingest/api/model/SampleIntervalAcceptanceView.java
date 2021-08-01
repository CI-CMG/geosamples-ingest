package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SampleIntervalAcceptanceView {

  @Valid
  private List<@Valid @NotNull IntervalIdView> intervals = new ArrayList<>(0);

  public List<IntervalIdView> getIntervals() {
    return intervals;
  }

  public void setIntervals(List<IntervalIdView> intervals) {
    if (intervals == null) {
      intervals = new ArrayList<>(0);
    }
    this.intervals = intervals;
  }
}
