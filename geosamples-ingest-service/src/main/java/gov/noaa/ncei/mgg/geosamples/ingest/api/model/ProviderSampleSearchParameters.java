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
import org.locationtech.jts.geom.Geometry;

public class ProviderSampleSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("platform:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 100;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(ProviderSampleView.class) String> order = DEFAULT_SORT;


  private List<String> imlgs = new ArrayList<>(0);
  private List<String> cruise = new ArrayList<>(0);
  private List<String> sample = new ArrayList<>(0);
  private List<String> platform = new ArrayList<>(0);
  private List<String> deviceCode = new ArrayList<>(0);
  private List<String> igsn = new ArrayList<>(0);

  private List<Boolean> publish = new ArrayList<>(0);

  private List<ApprovalState> approvalState = new ArrayList<>(0);
  private Geometry area;

  public List<String> getImlgs() {
    return imlgs;
  }

  public void setImlgs(List<String> imlgs) {
    if(imlgs == null) {
      imlgs = new ArrayList<>(0);
    }
    this.imlgs = imlgs;
  }

  public List<String> getCruise() {
    return cruise;
  }

  public void setCruise(List<String> cruise) {
    if(cruise == null) {
      cruise = new ArrayList<>(0);
    }
    this.cruise = cruise;
  }

  public List<String> getSample() {
    return sample;
  }

  public void setSample(List<String> sample) {
    if(sample == null) {
      sample = new ArrayList<>(0);
    }
    this.sample = sample;
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

  public List<String> getDeviceCode() {
    return deviceCode;
  }

  public void setDeviceCode(List<String> deviceCode) {
    if(deviceCode == null) {
      deviceCode = new ArrayList<>(0);
    }
    this.deviceCode = deviceCode;
  }

  public List<String> getIgsn() {
    return igsn;
  }

  public void setIgsn(List<String> igsn) {
    if(igsn == null) {
      igsn = new ArrayList<>(0);
    }
    this.igsn = igsn;
  }

  public List<ApprovalState> getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(List<ApprovalState> approvalState) {
    if(approvalState == null) {
      approvalState = new ArrayList<>(0);
    }
    this.approvalState = approvalState;
  }

  public List<Boolean> getPublish() {
    return publish;
  }

  public void setPublish(List<Boolean> publish) {
    if(publish == null) {
      publish = new ArrayList<>(0);
    }
    this.publish = publish;
  }

  public Geometry getArea() {
    return area;
  }

  public void setArea(Geometry area) {
    this.area = area;
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
