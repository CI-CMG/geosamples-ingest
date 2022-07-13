package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

public class DescriptorView {

  private String value;
  private String text;

  public DescriptorView(String value, String text) {
    this.value = value;
    this.text = text;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
