package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.ValidSort;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.validation.ValidBbox;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.locationtech.jts.geom.Geometry;


public class CombinedIntervalSampleSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Arrays.asList("cruise:asc", "sample:asc", "interval:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(1000)
  private int itemsPerPage = 1000;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(CombinedSampleIntervalView.class) String> order = DEFAULT_SORT;


  private List<String> cruiseContains = new ArrayList<>(0);
  private List<String> sampleContains = new ArrayList<>(0);
  private List<String> facilityCode = new ArrayList<>(0);
  private List<String> platformContains = new ArrayList<>(0);
  private List<String> deviceCode = new ArrayList<>(0);
  private List<String> date = new ArrayList<>(0);
  private List<String> storageMethodCode = new ArrayList<>(0);
  private List<String> piContains = new ArrayList<>(0);
  private List<String> provinceCode = new ArrayList<>(0);
  private List<String> igsn = new ArrayList<>(0);
  private List<String> imlgs = new ArrayList<>(0);
  private List<Integer> interval = new ArrayList<>(0);
  private Boolean publish = null;
  private List<String> lithCode = new ArrayList<>(0);
  private List<String> textCode = new ArrayList<>(0);
  private List<String> ageCode = new ArrayList<>(0);
  private List<String> rockLithCode = new ArrayList<>(0);
  private List<String> rockMinCode = new ArrayList<>(0);
  private List<String> weathMetaCode = new ArrayList<>(0);
  private List<String> remarkCode = new ArrayList<>(0);
  private List<String> munsellCode = new ArrayList<>(0);

  private List<ApprovalState> approvalState = new ArrayList<>(0);
  @ValidBbox
  private String bbox;


  public List<String> getCruiseContains() {
    return cruiseContains;
  }

  public void setCruiseContains(List<String> cruiseContains) {
    if(cruiseContains == null) {
      cruiseContains = new ArrayList<>(0);
    }
    this.cruiseContains = cruiseContains;
  }

  public List<String> getSampleContains() {
    return sampleContains;
  }

  public void setSampleContains(List<String> sampleContains) {
    if(sampleContains == null) {
      sampleContains = new ArrayList<>(0);
    }
    this.sampleContains = sampleContains;
  }

  public List<String> getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(List<String> facilityCode) {
    if(facilityCode == null) {
      facilityCode = new ArrayList<>(0);
    }
    this.facilityCode = facilityCode;
  }

  public List<String> getPlatformContains() {
    return platformContains;
  }

  public void setPlatformContains(List<String> platformContains) {
    if(platformContains == null) {
      platformContains = new ArrayList<>(0);
    }
    this.platformContains = platformContains;
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

  public List<String> getDate() {
    return date;
  }

  public void setDate(List<String> date) {
    if(date == null) {
      date = new ArrayList<>(0);
    }
    this.date = date;
  }

  public List<String> getStorageMethodCode() {
    return storageMethodCode;
  }

  public void setStorageMethodCode(List<String> storageMethodCode) {
    if(storageMethodCode == null) {
      storageMethodCode = new ArrayList<>(0);
    }
    this.storageMethodCode = storageMethodCode;
  }

  public List<String> getPiContains() {
    return piContains;
  }

  public void setPiContains(List<String> piContains) {
    if(piContains == null) {
      piContains = new ArrayList<>(0);
    }
    this.piContains = piContains;
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

  public List<String> getIgsn() {
    return igsn;
  }

  public void setIgsn(List<String> igsn) {
    if(igsn == null) {
      igsn = new ArrayList<>(0);
    }
    this.igsn = igsn;
  }

  public List<String> getImlgs() {
    return imlgs;
  }

  public void setImlgs(List<String> imlgs) {
    if(imlgs == null) {
      imlgs = new ArrayList<>(0);
    }
    this.imlgs = imlgs;
  }

  public List<Integer> getInterval() {
    return interval;
  }

  public void setInterval(List<Integer> interval) {
    if(interval == null) {
      interval = new ArrayList<>(0);
    }
    this.interval = interval;
  }

  public Boolean getPublish() {
    return publish;
  }

  public void setPublish(Boolean publish) {
    this.publish = publish;
  }

  public List<String> getLithCode() {
    return lithCode;
  }

  public void setLithCode(List<String> lithCode) {
    if(lithCode == null) {
      lithCode = new ArrayList<>(0);
    }
    this.lithCode = lithCode;
  }

  public List<String> getTextCode() {
    return textCode;
  }

  public void setTextCode(List<String> textCode) {
    if(textCode == null) {
      textCode = new ArrayList<>(0);
    }
    this.textCode = textCode;
  }

  public List<String> getAgeCode() {
    return ageCode;
  }

  public void setAgeCode(List<String> ageCode) {
    if(ageCode == null) {
      ageCode = new ArrayList<>(0);
    }
    this.ageCode = ageCode;
  }

  public List<String> getRockLithCode() {
    return rockLithCode;
  }

  public void setRockLithCode(List<String> rockLithCode) {
    if(rockLithCode == null) {
      rockLithCode = new ArrayList<>(0);
    }
    this.rockLithCode = rockLithCode;
  }

  public List<String> getRockMinCode() {
    return rockMinCode;
  }

  public void setRockMinCode(List<String> rockMinCode) {
    if(rockMinCode == null) {
      rockMinCode = new ArrayList<>(0);
    }
    this.rockMinCode = rockMinCode;
  }

  public List<String> getWeathMetaCode() {
    return weathMetaCode;
  }

  public void setWeathMetaCode(List<String> weathMetaCode) {
    if(weathMetaCode == null) {
      weathMetaCode = new ArrayList<>(0);
    }
    this.weathMetaCode = weathMetaCode;
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

  public List<String> getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(List<String> munsellCode) {
    if(munsellCode == null) {
      munsellCode = new ArrayList<>(0);
    }
    this.munsellCode = munsellCode;
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

  public String getBbox() {
    return bbox;
  }

  public void setBbox(String bbox) {
    this.bbox = bbox;
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
    if (
        sort.stream()
            .noneMatch(s -> s.contains("imlgs"))
    ) {
      sort.add("imlgs:asc");
    }
    this.order = sort;
  }
}
