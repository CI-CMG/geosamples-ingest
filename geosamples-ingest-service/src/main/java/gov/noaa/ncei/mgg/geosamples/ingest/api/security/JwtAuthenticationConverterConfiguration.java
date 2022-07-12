package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverterConfiguration {

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter(CustomGrantedAuthoritiesConverter converter) {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtAuthenticationConverter;
  }

}
