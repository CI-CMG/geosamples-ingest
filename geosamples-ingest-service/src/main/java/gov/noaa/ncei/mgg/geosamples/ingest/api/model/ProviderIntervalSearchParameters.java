package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProviderIntervalSearchParameters implements PagingAndSortingParameters {
  private static final List<String> DEFAULT_SORT = Collections.singletonList("interval:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 100;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(IntervalView.class) String> order = DEFAULT_SORT;


  private List<Integer> interval = new ArrayList<>(0);
  private List<String> imlgs = new ArrayList<>(0);

  private List<Boolean> publish = new ArrayList<>(0);

  private List<ApprovalState> approvalState = new ArrayList<>(0);

  public List<String> getImlgs() {
    return imlgs;
  }

  public void setImlgs(List<String> imlgs) {
    if (imlgs == null) {
      imlgs = new ArrayList<>(0);
    }
    this.imlgs = imlgs;
  }

  public List<Integer> getInterval() {
    return interval;
  }

  public void setInterval(List<Integer> interval) {
    if (interval == null) {
      interval = new ArrayList<>(0);
    }
    this.interval = interval;
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

  public List<Boolean> getPublish() {
    return publish;
  }

  public void setPublish(List<Boolean> publish) {
    if (publish == null) {
      publish = new ArrayList<>(0);
    }
    this.publish = publish;
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
