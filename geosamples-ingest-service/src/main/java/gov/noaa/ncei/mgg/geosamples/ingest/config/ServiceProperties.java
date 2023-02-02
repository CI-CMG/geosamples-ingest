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
  private String remoteJwtIssuer;
  @NotBlank
  private String authServiceUrl;
  @NotBlank
  private String clientId;
  @NotBlank
  private String showSampleBaseUrl;
  @NotBlank
  private String version;
  @NotNull
  private Integer tokenExpirationSeconds;
  @NotBlank
  private String localJwkSetUri;
  @NotBlank
  private String remoteJwkSetUri;

  public String getShowSampleBaseUrl() {
    return showSampleBaseUrl;
  }

  public void setShowSampleBaseUrl(String showSampleBaseUrl) {
    this.showSampleBaseUrl = showSampleBaseUrl;
  }

  public String getRemoteJwtIssuer() {
    return remoteJwtIssuer;
  }

  public void setRemoteJwtIssuer(String remoteJwtIssuer) {
    this.remoteJwtIssuer = remoteJwtIssuer;
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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Integer getTokenExpirationSeconds() {
    return tokenExpirationSeconds;
  }

  public void setTokenExpirationSeconds(Integer tokenExpirationSeconds) {
    this.tokenExpirationSeconds = tokenExpirationSeconds;
  }

  public String getLocalJwkSetUri() {
    return localJwkSetUri;
  }

  public void setLocalJwkSetUri(String localJwkSetUri) {
    this.localJwkSetUri = localJwkSetUri;
  }

  public String getRemoteJwkSetUri() {
    return remoteJwkSetUri;
  }

  public void setRemoteJwkSetUri(String remoteJwkSetUri) {
    this.remoteJwkSetUri = remoteJwkSetUri;
  }
}
