package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class ApiProviderAuthenticationFilterFactory {

  private static final NoOpAuthenticationSuccessHandler SUCCESS_HANDLER = new NoOpAuthenticationSuccessHandler();

  private static class NoOpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      //no-op
    }
  }

  private static class CsbProviderAuthenticationConverter implements AuthenticationConverter {

    private final BearerTokenResolver bearerTokenResolver;

    private CsbProviderAuthenticationConverter(BearerTokenResolver bearerTokenResolver) {
      this.bearerTokenResolver = bearerTokenResolver;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
      Authentication authentication = null;
      final String headerToken = bearerTokenResolver.resolve(request);
      if (headerToken != null) {
        authentication = new ApiProviderAuthenticationToken(headerToken);
      }
      return authentication;
    }
  }

  public static AuthenticationFilter build(BearerTokenResolver bearerTokenResolver, AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, new CsbProviderAuthenticationConverter(
        bearerTokenResolver));
    authenticationFilter.setSuccessHandler(SUCCESS_HANDLER);
    authenticationFilter.setFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
    return authenticationFilter;
  }

}
