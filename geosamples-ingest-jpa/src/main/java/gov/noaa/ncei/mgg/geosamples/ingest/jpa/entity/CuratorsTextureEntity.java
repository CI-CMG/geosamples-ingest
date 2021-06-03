package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

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

  /*
	DEVICE VARCHAR2(30) not null
		constraint CURATORS_DEVICE_PK
			primary key,
   */
  @Id
  @Column(name = "TEXTURE", nullable = false, length = 40)
  private String texture;

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }
}
