package gov.noaa.ncei.mgg.geosamples.ingest.service.model;

import java.util.List;
import javax.validation.Valid;

public class SampleRowHolder {

  @Valid
  private final List< @Valid SampleRow> rows;

  public SampleRowHolder(
      @Valid List<@Valid SampleRow> rows) {
    this.rows = rows;
  }

  public List<SampleRow> getRows() {
    return rows;
  }

}
