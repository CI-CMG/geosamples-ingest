package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SampleSearchParameters extends ProviderSampleSearchParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("platform:asc");

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(SampleView.class) String> order = DEFAULT_SORT;

  private List<String> facilityCode = new ArrayList<>(0);

  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if(facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
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
