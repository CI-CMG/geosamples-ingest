package gov.noaa.ncei.mgg.geosamples.ingest.config;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("geosamples")
public class ServiceProperties {


  @NotNull
  @Min(16)
  private Integer tokenSizeBytes;
  @NotBlank
  private String jwtIssuer;
  @NotBlank
  private String authServiceUrl;
  @NotBlank
  private String clientId;
  @NotBlank
  private String showSampleBaseUrl;

  public String getShowSampleBaseUrl() {
    return showSampleBaseUrl;
  }

  public void setShowSampleBaseUrl(String showSampleBaseUrl) {
    this.showSampleBaseUrl = showSampleBaseUrl;
  }

  public String getJwtIssuer() {
    return jwtIssuer;
  }

  public void setJwtIssuer(String jwtIssuer) {
    this.jwtIssuer = jwtIssuer;
  }

  public String getAuthServiceUrl() {
    return authServiceUrl;
  }

  public void setAuthServiceUrl(String authServiceUrl) {
    this.authServiceUrl = authServiceUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public Integer getTokenSizeBytes() {
    return tokenSizeBytes;
  }

  public void setTokenSizeBytes(Integer tokenSizeBytes) {
    this.tokenSizeBytes = tokenSizeBytes;
  }
}
