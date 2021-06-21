package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "weathering",
    "weatheringCode",
})
public class WeatheringView {


  @NotBlank
  @Size(max = 30)
  private String weathering;

  @NotBlank
  @Size(max = 1)
  private String weatheringCode;

  @Size(max = 255)
  private String sourceUri;

  public String getWeathering() {
    return weathering;
  }

  public void setWeathering(String weathering) {
    this.weathering = weathering;
  }

  public String getWeatheringCode() {
    return weatheringCode;
  }

  public void setWeatheringCode(String weatheringCode) {
    this.weatheringCode = weatheringCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
