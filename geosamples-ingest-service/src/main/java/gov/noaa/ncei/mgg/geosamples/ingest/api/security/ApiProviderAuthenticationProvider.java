package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenAuthorization;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenService;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class ApiProviderAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiProviderAuthenticationProvider.class);

  private final TokenService tokenService;

  @Autowired
  public ApiProviderAuthenticationProvider(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  private static Authentication toAuthentication(TokenAuthorization tokenAuth) {
    LOGGER.debug("Authenticated {} with roles {}", tokenAuth.getSubject(), tokenAuth.getAuthorities());
    return new ApiProviderAuthenticationToken(
        tokenAuth.getSubject(),
        tokenAuth.getAuthorities().stream().map(
            SimpleGrantedAuthority::new).collect(Collectors.toList()));
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    ApiProviderAuthenticationToken preAuth = (ApiProviderAuthenticationToken) authentication;

    try {

      return tokenService.authenticate(preAuth.getToken())
          .map(ApiProviderAuthenticationProvider::toAuthentication)
          .orElseThrow(() -> new AuthenticationServiceException("Unable to authenticate token. Auth was null"));
    } catch (AuthenticationException e) {
      throw e;
    } catch (Exception e) {
      LOGGER.error("Unable to authenticate provider token: {}", preAuth.getSubject(), e);
      throw new AuthenticationServiceException("Unable to authenticate provider token", e);
    }

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (ApiProviderAuthenticationToken.class.isAssignableFrom(authentication));
  }

}
