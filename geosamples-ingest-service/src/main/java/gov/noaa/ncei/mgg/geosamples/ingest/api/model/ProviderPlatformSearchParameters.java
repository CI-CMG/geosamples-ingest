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

public class ProviderPlatformSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("platform:asc");

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(ProviderPlatformView.class) String> order = DEFAULT_SORT;

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  private List<String> platform = new ArrayList<>(0);
  private List<Integer> masterId = new ArrayList<>(0);
  private List<String> icesCode = new ArrayList<>(0);
  private List<Long> id = new ArrayList<>(0);

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

  public List<String> getPlatform() {
    return platform;
  }

  public void setPlatform(List<String> platform) {
    if(platform == null) {
      platform = new ArrayList<>(0);
    }
    this.platform = platform;
  }

  public List<Integer> getMasterId() {
    return masterId;
  }

  public void setMasterId(List<Integer> masterId) {
    if(masterId == null) {
      masterId = new ArrayList<>(0);
    }
    this.masterId = masterId;
  }

  public List<String> getIcesCode() {
    return icesCode;
  }

  public void setIcesCode(List<String> icesCode) {
    if(icesCode == null) {
      icesCode = new ArrayList<>(0);
    }
    this.icesCode = icesCode;
  }

  public List<Long> getId() {
    return id;
  }

  public void setId(List<Long> id) {
    if(id == null) {
      id = new ArrayList<>(0);
    }
    this.id = id;
  }

  @Override
  public void setOrder(List<String> sort) {
    if (sort == null || sort.isEmpty()) {
      sort = DEFAULT_SORT;
    }
    this.order = sort;
  }

}
