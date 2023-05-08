package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CruiseSearchParameters extends ProviderCruiseSearchParameters {

  private static final List<String> DEFAULT_SORT = Arrays.asList("cruiseName:asc", "year:asc");

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(CruiseView.class) String> order = DEFAULT_SORT;

  private List<String> facilityCodeEquals = new ArrayList<>(0);

  public List<String> getFacilityCodeEquals() {
    return facilityCodeEquals;
  }

  public void setFacilityCodeEquals(List<String> facilityCodeEquals) {
    if (facilityCodeEquals == null) {
      facilityCodeEquals = new ArrayList<>(0);
    }
    this.facilityCodeEquals = facilityCodeEquals;
  }

  @Override
  public List<String> getOrder() {
    return order;
  }

  @Override
  public void setOrder(List<String> sort) {
    if (sort == null || sort.isEmpty()) {
      sort = DEFAULT_SORT;
    }
    this.order = sort;
  }
}
