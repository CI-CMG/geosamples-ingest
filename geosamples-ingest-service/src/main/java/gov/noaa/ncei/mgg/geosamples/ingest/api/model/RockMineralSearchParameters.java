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

public class RockMineralSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("rockMineral:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(RockMineralView.class) String> order = DEFAULT_SORT;

  private List<String> rockMineral = new ArrayList<>(0);
  private List<String> rockMineralCode = new ArrayList<>(0);


  public List<String> getRockMineral() {
    return rockMineral;
  }

  public void setRockMineral(List<String> rockMineral) {
    if(rockMineral == null) {
      rockMineral = new ArrayList<>(0);
    }
    this.rockMineral = rockMineral;
  }

  public List<String> getRockMineralCode() {
    return rockMineralCode;
  }

  public void setRockMineralCode(List<String> rockMineralCode) {
    if(rockMineralCode == null) {
      rockMineralCode = new ArrayList<>(0);
    }
    this.rockMineralCode = rockMineralCode;
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
