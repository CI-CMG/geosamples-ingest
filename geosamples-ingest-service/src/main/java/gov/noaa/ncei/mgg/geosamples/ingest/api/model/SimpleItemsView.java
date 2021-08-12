package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemsView<T> {

  private List<T> items = new ArrayList<>(0);

  public List<T> getItems() {
    return items;
  }

  public void setItems(List<T> items) {
    if (items == null) {
      items = new ArrayList<>(0);
    }
    this.items = items;
  }
}
