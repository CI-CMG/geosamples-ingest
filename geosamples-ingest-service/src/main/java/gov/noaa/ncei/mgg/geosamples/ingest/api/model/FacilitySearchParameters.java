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

public class FacilitySearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("facility:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(UserView.class) String> order = DEFAULT_SORT;

  private List<String> facilityCode = new ArrayList<>(0);
  private List<String> instCode = new ArrayList<>(0);
  private List<String> facility = new ArrayList<>(0);
  private List<String> facilityComment = new ArrayList<>(0);

  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if (facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
  }

  public List<String> getInstCode() {
    return instCode;
  }

  public void setInstCode(List<String> instCode) {
    if (instCode == null) {
      instCode = new ArrayList<>(0);
    }
    this.instCode = instCode;
  }

  public List<String> getFacility() {
    return facility;
  }

  public void setFacility(List<String> facility) {
    if (facility == null) {
      facility = new ArrayList<>(0);
    }
    this.facility = facility;
  }

  public List<String> getFacilityComment() {
    return facilityComment;
  }

  public void setFacilityComment(List<String> facilityComment) {
    if (facilityComment == null) {
      facilityComment = new ArrayList<>(0);
    }
    this.facilityComment = facilityComment;
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
