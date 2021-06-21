package gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging;

import java.util.List;

public interface PagingAndSortingParameters {


  int getPage();

  void setPage(int page);

  int getItemsPerPage();

  void setItemsPerPage(int resultsPerPage);

  List<String> getOrder();

  void setOrder(List<String> sort);
}
