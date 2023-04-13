package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import gov.noaa.ncei.mgg.geosamples.ingest.service.JwksInitializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

@Configuration
public class AuthenticationManagerConfiguration {

  @Bean
  public AuthenticationProvider localJwtProvider(
      CustomGrantedAuthoritiesConverter customGrantedAuthoritiesConverter,
      JwksInitializer jwksInitializer
  ) throws IOException, ParseException {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customGrantedAuthoritiesConverter);

    JWSKeySelector<SecurityContext> localJwsKeySelector = new JWSVerificationKeySelector<>(
        JWSAlgorithm.RS256,
        new ImmutableJWKSet<>(JWKSet.load(new ByteArrayInputStream(jwksInitializer.getJwksJson().getBytes(StandardCharsets.UTF_8))))
    );
    DefaultJWTProcessor<SecurityContext> localJWTProcessor = new DefaultJWTProcessor<>();
    localJWTProcessor.setJWSKeySelector(localJwsKeySelector);
    NimbusJwtDecoder localJwtDecoder = new NimbusJwtDecoder(localJWTProcessor);
    localJwtDecoder.setJwtValidator(new JwtTimestampValidator());

    JwtAuthenticationProvider localJwtProvider = new JwtAuthenticationProvider(localJwtDecoder);
    localJwtProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);

    return localJwtProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      @Qualifier("localJwtProvider") AuthenticationProvider localJwtProvider,
      JwtDecoder jwtDecoder) {
    return new ProviderManager(new JwtAuthenticationProvider(jwtDecoder), localJwtProvider);
  }

}
