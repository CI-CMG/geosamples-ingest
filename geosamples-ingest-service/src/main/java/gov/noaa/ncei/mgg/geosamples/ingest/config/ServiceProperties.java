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

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String showSampleBaseUrl;

  public String getShowSampleBaseUrl() {
    return showSampleBaseUrl;
  }

  public void setShowSampleBaseUrl(String showSampleBaseUrl) {
    this.showSampleBaseUrl = showSampleBaseUrl;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
