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

public class MunsellSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("munsell:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(MunsellView.class) String> order = DEFAULT_SORT;

  private List<String> munsell = new ArrayList<>(0);
  private List<String> munsellCode = new ArrayList<>(0);


  public List<String> getMunsell() {
    return munsell;
  }

  public void setMunsell(List<String> munsell) {
    if(munsell == null) {
      munsell = new ArrayList<>(0);
    }
    this.munsell = munsell;
  }

  public List<String> getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(List<String> munsellCode) {
    if(munsellCode == null) {
      munsellCode = new ArrayList<>(0);
    }
    this.munsellCode = munsellCode;
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
