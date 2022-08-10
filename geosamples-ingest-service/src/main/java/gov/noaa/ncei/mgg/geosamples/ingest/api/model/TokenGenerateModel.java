package gov.noaa.ncei.mgg.geosamples.ingest.api.model;

import javax.validation.constraints.NotBlank;

public class TokenGenerateModel {

  private Long id;

  @NotBlank
  private String alias;

  private String token;

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
