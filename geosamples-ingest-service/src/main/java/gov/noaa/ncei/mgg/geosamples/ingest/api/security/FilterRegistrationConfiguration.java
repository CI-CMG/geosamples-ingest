package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FilterRegistrationConfiguration {

  @Bean
  public FilterRegistrationBean<JwtUrlFilter> jwtUrlFilterFilterRegistration(JwtUrlFilter filter) {
    FilterRegistrationBean<JwtUrlFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(true);
    return registration;
  }

}
