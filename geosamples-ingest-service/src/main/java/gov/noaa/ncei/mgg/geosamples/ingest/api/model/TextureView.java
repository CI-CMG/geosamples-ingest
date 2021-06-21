package gov.noaa.ncei.mgg.geosamples.ingest.api.model;


import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Sortable({
    "texture",
    "textureCode",
})
public class TextureView {


  @NotBlank
  @Size(max = 40)
  private String texture;

  @NotBlank
  @Size(max = 1)
  private String textureCode;

  @Size(max = 255)
  private String sourceUri;

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }

  public String getTextureCode() {
    return textureCode;
  }

  public void setTextureCode(String textureCode) {
    this.textureCode = textureCode;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
