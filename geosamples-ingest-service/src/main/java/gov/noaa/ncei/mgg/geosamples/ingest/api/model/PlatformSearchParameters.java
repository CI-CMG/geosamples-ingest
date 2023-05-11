package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlatformSearchParameters extends ProviderPlatformSearchParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("platform:asc");

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(PlatformView.class) String> order = DEFAULT_SORT;

  private List<String> createdBy = new ArrayList<>(0);

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

  public List<String> getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(List<String> createdBy) {
    if (createdBy == null) {
      createdBy = new ArrayList<>(0);
    }
    this.createdBy = createdBy;
  }
}
