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

public class RemarkSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("remark:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(RemarkView.class) String> order = DEFAULT_SORT;

  private List<String> remark = new ArrayList<>(0);
  private List<String> remarkCode = new ArrayList<>(0);


  public List<String> getRemark() {
    return remark;
  }

  public void setRemark(List<String> remark) {
    if(remark == null) {
      remark = new ArrayList<>(0);
    }
    this.remark = remark;
  }

  public List<String> getRemarkCode() {
    return remarkCode;
  }

  public void setRemarkCode(List<String> remarkCode) {
    if(remarkCode == null) {
      remarkCode = new ArrayList<>(0);
    }
    this.remarkCode = remarkCode;
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
