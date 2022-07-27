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

public class SampleLinksSearchParameters implements PagingAndSortingParameters {

  private static final List<String> DEFAULT_SORT = Collections.singletonList("imlgs:asc");

  @Min(1)
  private int page = 1;

  @Min(1)
  @Max(200)
  private int itemsPerPage = 100;

  @NotNull
  @Size(min = 1)
  private List<@ValidSort(SampleLinksView.class) String> order = DEFAULT_SORT;

  private List<Long> id = new ArrayList<>(0);
  private List<String> imlgs = new ArrayList<>(0);
  private List<String> dataLink = new ArrayList<>(0);
  private List<String> linkLevel = new ArrayList<>(0);
  private List<String> linkSource = new ArrayList<>(0);
  private List<String> linkType = new ArrayList<>(0);


  public List<Long> getId() {
    return id;
  }

  public void setId(List<Long> id) {
    if(id == null) {
      id = new ArrayList<>(0);
    }
    this.id = id;
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

  public List<String> getDataLink() {
    return dataLink;
  }

  public void setDataLink(List<String> dataLink) {
    if(dataLink == null) {
      dataLink = new ArrayList<>(0);
    }
    this.dataLink = dataLink;
  }

  public List<String> getLinkLevel() {
    return linkLevel;
  }

  public void setLinkLevel(List<String> linkLevel) {
    if(linkLevel == null) {
      linkLevel = new ArrayList<>(0);
    }
    this.linkLevel = linkLevel;
  }

  public List<String> getLinkSource() {
    return linkSource;
  }

  public void setLinkSource(List<String> linkSource) {
    if(linkSource == null) {
      linkSource = new ArrayList<>(0);
    }
    this.linkSource = linkSource;
  }

  public List<String> getLinkType() {
    return linkType;
  }

  public void setLinkType(List<String> linkType) {
    if(linkType == null) {
      linkType = new ArrayList<>(0);
    }
    this.linkType = linkType;
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
