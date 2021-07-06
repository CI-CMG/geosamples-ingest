package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RockLithologySearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("rockLithology:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(RockLithologyView.class) String> order = DEFAULT_SORT;

  private List<String> rockLithology = new ArrayList<>(0);
  private List<String> rockLithologyCode = new ArrayList<>(0);


  public List<String> getRockLithology() {
    return rockLithology;
  }

  public void setRockLithology(List<String> rockLithology) {
    if(rockLithology == null) {
      rockLithology = new ArrayList<>(0);
    }
    this.rockLithology = rockLithology;
  }

  public List<String> getRockLithologyCode() {
    return rockLithologyCode;
  }

  public void setRockLithologyCode(List<String> rockLithologyCode) {
    if(rockLithologyCode == null) {
      rockLithologyCode = new ArrayList<>(0);
    }
    this.rockLithologyCode = rockLithologyCode;
  }

  @Override
  public int getPage() {
    return page;
  }

  @Override
  public void setPage(int page) {
    this.page = page;
  }

  @Override
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  @Override
  public void setItemsPerPage(int resultsPerPage) {
    this.itemsPerPage = itemsPerPage;
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
