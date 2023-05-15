package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProviderCruiseSearchParameters implements PagingAndSortingParameters {
  private static final List<String> DEFAULT_SORT = Arrays.asList("cruiseName:asc", "year:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(ProviderCruiseWriteView.class) String> order = DEFAULT_SORT;

  private List<String> cruiseNameContains = new ArrayList<>(0);
  private List<String> cruiseNameEquals = new ArrayList<>(0);
  private List<Long> year = new ArrayList<>(0);
  private List<Boolean> publish = new ArrayList<>(0);
  private List<String> platformEquals = new ArrayList<>(0);

  private List<ApprovalState> approvalState = new ArrayList<>(0);
  private List<Long> id = new ArrayList<>(0);

  public List<String> getCruiseNameContains() {
    return cruiseNameContains;
  }

  public void setCruiseNameContains(List<String> cruiseNameContains) {
    if (cruiseNameContains == null) {
      cruiseNameContains = new ArrayList<>(0);
    }
    this.cruiseNameContains = cruiseNameContains;
  }

  public List<String> getCruiseNameEquals() {
    return cruiseNameEquals;
  }

  public void setCruiseNameEquals(List<String> cruiseNameEquals) {
    if (cruiseNameEquals == null) {
      cruiseNameEquals = new ArrayList<>(0);
    }
    this.cruiseNameEquals = cruiseNameEquals;
  }

  public List<Long> getYear() {
    return year;
  }

  public void setYear(List<Long> year) {
    if (year == null) {
      year = new ArrayList<>(0);
    }
    this.year = year;
  }

  public List<Boolean> getPublish() {
    return publish;
  }

  public void setPublish(List<Boolean> publish) {
    if (publish == null) {
      publish = new ArrayList<>(0);
    }
    this.publish = publish;
  }

  public List<String> getPlatformEquals() {
    return platformEquals;
  }

  public void setPlatformEquals(List<String> platformEquals) {
    if (platformEquals == null) {
      platformEquals = new ArrayList<>(0);
    }
    this.platformEquals = platformEquals;
  }

  public List<Long> getId() {
    return id;
  }

  public void setId(List<Long> id) {
    if (id == null) {
      id = new ArrayList<>(0);
    }
    this.id = id;
  }

  public List<ApprovalState> getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(List<ApprovalState> approvalState) {
    if (approvalState == null) {
      approvalState = new ArrayList<>(0);
    }
    this.approvalState = approvalState;
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
