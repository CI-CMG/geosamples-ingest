package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "age",
    "ageCode",
})
public class AgeView {


  @NotBlank
  @Size(max = 20)
  private String age;

  @NotBlank
  @Size(max = 2)
  private String ageCode;

  @Size(max = 255)
  private String sourceUri;

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getAgeCode() {
    return ageCode;
  }

  public void setAgeCode(String ageCode) {
    this.ageCode = ageCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
