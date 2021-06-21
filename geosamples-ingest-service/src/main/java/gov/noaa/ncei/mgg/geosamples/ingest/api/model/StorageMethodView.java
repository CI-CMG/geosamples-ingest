package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "storageMethod",
    "storageMethodCode",
})
public class StorageMethodView {


  @NotBlank
  @Size(max = 35)
  private String storageMethod;

  @NotBlank
  @Size(max = 1)
  private String storageMethodCode;

  @Size(max = 255)
  private String sourceUri;

  public String getStorageMethod() {
    return storageMethod;
  }

  public void setStorageMethod(String storageMethod) {
    this.storageMethod = storageMethod;
  }

  public String getStorageMethodCode() {
    return storageMethodCode;
  }

  public void setStorageMethodCode(String storageMethodCode) {
    this.storageMethodCode = storageMethodCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
