package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManagerConfiguration {

  @Bean
  public AuthenticationManager authenticationManager(
      ServiceProperties serviceProperties,
      CustomGrantedAuthoritiesConverter customGrantedAuthoritiesConverter
  ) throws IOException, ParseException {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customGrantedAuthoritiesConverter);

    JWSKeySelector<SecurityContext> remoteJwsKeySelector = new JWSVerificationKeySelector<>(
        JWSAlgorithm.RS256,
        new RemoteJWKSet<>(new URL(serviceProperties.getRemoteJwkSetUri()))
    );
    DefaultJWTProcessor<SecurityContext> remoteJWTProcessor = new DefaultJWTProcessor<>();
    remoteJWTProcessor.setJWSKeySelector(remoteJwsKeySelector);
    NimbusJwtDecoder remoteJwtDecoder = new NimbusJwtDecoder(remoteJWTProcessor);
    remoteJwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(serviceProperties.getRemoteJwtIssuer()));

    JWSKeySelector<SecurityContext> localJwsKeySelector = new JWSVerificationKeySelector<>(
        JWSAlgorithm.RS256,
        new ImmutableJWKSet<>(JWKSet.load(new File(serviceProperties.getLocalJwkSetUri())))
    );
    DefaultJWTProcessor<SecurityContext> localJWTProcessor = new DefaultJWTProcessor<>();
    localJWTProcessor.setJWSKeySelector(localJwsKeySelector);
    NimbusJwtDecoder localJwtDecoder = new NimbusJwtDecoder(localJWTProcessor);
    localJwtDecoder.setJwtValidator(new JwtTimestampValidator());

    JwtAuthenticationProvider remoteJwtProvider = new JwtAuthenticationProvider(remoteJwtDecoder);
    remoteJwtProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
    JwtAuthenticationProvider localJwtProvider = new JwtAuthenticationProvider(localJwtDecoder);
    localJwtProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);


    return new ProviderManager(remoteJwtProvider, localJwtProvider);
  }

}
