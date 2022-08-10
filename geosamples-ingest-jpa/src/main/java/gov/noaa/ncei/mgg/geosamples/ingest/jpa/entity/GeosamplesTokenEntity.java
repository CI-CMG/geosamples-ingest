package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import edu.colorado.cires.cmg.jpa.model.EntityWithId;
import edu.colorado.cires.cmg.jpa.util.EntityUtil;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GEOSAMPLES_USER_TOKEN")
public class GeosamplesTokenEntity implements EntityWithId<Long> {

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOSAMPLES_USER_TOKEN_SEQ")
  @SequenceGenerator(name = "GEOSAMPLES_USER_TOKEN_SEQ", sequenceName = "GEOSAMPLES_USER_TOKEN_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "TOKEN_ALIAS", nullable = false, length = 200)
  private String alias;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
  @JoinColumn(name = "USER_ID", nullable = false)
  private GeosamplesUserEntity user;

  @Column(name = "TOKEN_HASH", nullable = false, length = 1000, unique = true)
  private String tokenHash;

  public String getTokenHash() {
    return tokenHash;
  }

  public void setTokenHash(String tokenHash) {
    this.tokenHash = tokenHash;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public GeosamplesUserEntity getUser() {
    return user;
  }

  public void setUser(GeosamplesUserEntity user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    return EntityUtil.equals(this, o);
  }

  @Override
  public int hashCode() {
    return EntityUtil.hashCodeGeneratedId();
  }

}
