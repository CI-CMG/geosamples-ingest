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

public class UserSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("userName:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 50;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(UserView.class) String> order = DEFAULT_SORT;

  private List<String> userNameContains = new ArrayList<>(0);
  private List<String> userNameEquals = new ArrayList<>(0);
  private List<String> displayNameContains = new ArrayList<>(0);
  private List<String> facilityCode = new ArrayList<>(0);
  private List<String> role = new ArrayList<>(0);

  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if (facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
  }

  public List<String> getUserNameContains() {
    return userNameContains;
  }

  public void setUserNameContains(List<String> userNameContains) {
    if (userNameContains == null) {
      userNameContains = new ArrayList<>(0);
    }
    this.userNameContains = userNameContains;
  }

  public List<String> getUserNameEquals() {
    return userNameEquals;
  }

  public void setUserNameEquals(List<String> userNameEquals) {
    if (userNameEquals == null) {
      userNameEquals = new ArrayList<>(0);
    }
    this.userNameEquals = userNameEquals;
  }

  public List<String> getDisplayNameContains() {
    return displayNameContains;
  }

  public void setDisplayNameContains(List<String> displayNameContains) {
    if (displayNameContains == null) {
      displayNameContains = new ArrayList<>(0);
    }
    this.displayNameContains = displayNameContains;
  }

  public List<String> getRole() {
    return role;
  }

  public void setRole(List<String> role) {
    if (role == null) {
      role = new ArrayList<>(0);
    }
    this.role = role;
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
