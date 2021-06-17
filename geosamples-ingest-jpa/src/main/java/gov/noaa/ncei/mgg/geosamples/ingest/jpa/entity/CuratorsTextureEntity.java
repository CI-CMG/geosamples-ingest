package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
	TEXTURE_CODE VARCHAR2(1),
	TEXTURE VARCHAR2(40) not null
		constraint CURATORS_TEXT_PK
			primary key,
	PUBLISH VARCHAR2(1) default 'Y',
	PREVIOUS_STATE VARCHAR2(1),
	SOURCE_URI VARCHAR2(255)
 */

@Entity
@Table(name = "CURATORS_TEXTURE")
public class CuratorsTextureEntity {


  @Id
  @Column(name = "TEXTURE", nullable = false, length = 40)
  private String texture;

  @Column(name = "TEXTURE_CODE", length = 1)
  private String textureCode;

  @Column(name = "PUBLISH", length = 1)
  private String publish;

  @Column(name = "PREVIOUS_STATE", length = 1)
  private String previousState;

  @Column(name = "SOURCE_URI", length = 255)
  private String sourceUri;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuratorsTextureEntity that = (CuratorsTextureEntity) o;
    return Objects.equals(texture, that.texture);
  }

  @Override
  public int hashCode() {
    return Objects.hash(texture);
  }

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

  public String getPublish() {
    return publish;
  }

  public void setPublish(String publish) {
    this.publish = publish;
  }

  public String getPreviousState() {
    return previousState;
  }

  public void setPreviousState(String previousState) {
    this.previousState = previousState;
  }

  public String getSourceUri() {
    return sourceUri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }
}
