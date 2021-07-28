package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class CombinedIntervalSampleSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Arrays.asList("cruise:asc", "sample:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(CombinedSampleIntervalView.class) String> order = DEFAULT_SORT;


  private List<String> cruise = new ArrayList<>(0);
  private List<String> facilityCode = new ArrayList<>(0);
  private List<String> platform = new ArrayList<>(0);


  public List<String> getCruise() {
    return cruise;
  }

  public void setCruise(List<String> cruise) {
    if(cruise == null) {
      cruise = new ArrayList<>(0);
    }
    this.cruise = cruise;
  }


  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if(facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
  }

  public List<String> getPlatform() {
    return platform;
  }

  public void setPlatform(List<String> platform) {
    if(platform == null) {
      platform = new ArrayList<>(0);
    }
    this.platform = platform;
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
  public void setItemsPerPage(int itemsPerPage) {
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
