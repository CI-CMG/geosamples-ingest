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

public class ProvinceSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("province:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(ProvinceView.class) String> order = DEFAULT_SORT;

  private List<String> province = new ArrayList<>(0);
  private List<String> provinceCode = new ArrayList<>(0);
  private List<String> provinceComment = new ArrayList<>(0);


  public List<String> getProvince() {
    return province;
  }

  public void setProvince(List<String> province) {
    if(province == null) {
      province = new ArrayList<>(0);
    }
    this.province = province;
  }

  public List<String> getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(List<String> provinceCode) {
    if(provinceCode == null) {
      provinceCode = new ArrayList<>(0);
    }
    this.provinceCode = provinceCode;
  }


  public List<String> getProvinceComment() {
    return provinceComment;
  }

  public void setProvinceComment(List<String> provinceComment) {
    if(provinceComment == null) {
      provinceComment = new ArrayList<>(0);
    }
    this.provinceComment = provinceComment;
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
