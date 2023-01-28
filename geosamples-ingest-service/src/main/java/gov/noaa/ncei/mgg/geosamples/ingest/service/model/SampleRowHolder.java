package gov.noaa.ncei.mgg.geosamples.ingest.service.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UniqueIGSNs;
import java.util.List;
import javax.validation.Valid;

@UniqueIGSNs
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
