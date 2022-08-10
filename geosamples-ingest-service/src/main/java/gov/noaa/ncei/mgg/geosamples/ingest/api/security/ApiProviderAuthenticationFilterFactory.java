package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class ApiProviderAuthenticationFilterFactory {

  private static final Pattern BEARER_PATTERN = Pattern.compile("Bearer (\\S+)");
  private static final NoOpAuthenticationSuccessHandler SUCCESS_HANDLER = new NoOpAuthenticationSuccessHandler();
  private static final CsbProviderAuthenticationConverter CONVERTER = new CsbProviderAuthenticationConverter();

  private static class NoOpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      //no-op
    }
  }

  private static class CsbProviderAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
      Authentication authentication = null;
      String headerValue = request.getHeader("X-API-TOKEN");
      if (StringUtils.isNotBlank(headerValue)) {
        headerValue = headerValue.trim();
        Matcher matcher = BEARER_PATTERN.matcher(headerValue);
        if (matcher.matches()) {
          String token = matcher.group(1);
          authentication = new ApiProviderAuthenticationToken(token);
        }
      }

      return authentication;
    }
  }

  public static AuthenticationFilter build(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, CONVERTER);
    authenticationFilter.setSuccessHandler(SUCCESS_HANDLER);
    authenticationFilter.setFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
    return authenticationFilter;
  }

}
