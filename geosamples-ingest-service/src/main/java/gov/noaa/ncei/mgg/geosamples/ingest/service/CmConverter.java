package gov.noaa.ncei.mgg.geosamples.ingest.service;

public class CmConverter {

  private final Double cm;

  public CmConverter(Double cm) {
    this.cm = cm;
  }

  public Integer getCm() {
    return SampleDataUtils.truncate(cm);
  }

  public Integer getMm() {
    if (cm == null) {
      return null;
    }
    return SampleDataUtils.round((cm - getCm()) * 10D);
  }
}
