package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Sortable({
    "id",
    "year",
    "cruiseName",
    "publish"
})
public class ProviderCruiseWriteView {
  private Long id;

  @NotNull
  @Min(1900)
  private Integer year;

  @Size(max = 30)
  private String cruiseName;

  private List<String> platforms = new ArrayList<>(0);
  private List<String> legs = new ArrayList<>(0);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getCruiseName() {
    return cruiseName;
  }

  public void setCruiseName(String cruiseName) {
    if (cruiseName != null) {
      cruiseName = cruiseName.trim().toUpperCase(Locale.ENGLISH);
    }
    this.cruiseName = cruiseName;
  }

  public List<String> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<String> platforms) {
    if (platforms == null) {
      platforms = new ArrayList<>(0);
    }
    this.platforms = platforms.stream().map(v -> v.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
  }

  public List<String> getLegs() {
    return legs;
  }

  public void setLegs(List<String> legs) {
    if (legs == null) {
      legs = new ArrayList<>(0);
    }
    this.legs = legs.stream().map(v -> v.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
  }
}
