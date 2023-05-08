package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Sortable({
    "id",
    "year",
    "cruiseName",
    "publish"
})
public class CruiseView extends ProviderCruiseView {
  private List<String> facilityCodes = new ArrayList<>(0);

  public List<String> getFacilityCodes() {
    return facilityCodes;
  }

  public void setFacilityCodes(List<String> facilityCodes) {
    if (facilityCodes == null) {
      facilityCodes = new ArrayList<>(0);
    }
    this.facilityCodes = facilityCodes.stream().map(v -> v.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
  }
}
