package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtUrlFilter extends OncePerRequestFilter {

  private static final Pattern DOWNLOAD_PATTERN = Pattern.compile(".*/api/v1/sample-interval/export");
  private final DefaultBearerTokenResolver headerTokenResolver = new DefaultBearerTokenResolver();
  private final BearerTokenAuthenticationFilter headerTokenFilter;
  private final DefaultBearerTokenResolver parameterTokenProvider;
  private final BearerTokenAuthenticationFilter parameterTokenFilter;

  @Autowired
  public JwtUrlFilter(AuthenticationManager authenticationManager) {
    headerTokenFilter = new BearerTokenAuthenticationFilter(authenticationManager);
    headerTokenFilter.setBearerTokenResolver(headerTokenResolver);

    parameterTokenProvider = new DefaultBearerTokenResolver();
    parameterTokenProvider.setAllowUriQueryParameter(true);
    parameterTokenFilter = new BearerTokenAuthenticationFilter(authenticationManager);
    parameterTokenFilter.setBearerTokenResolver(parameterTokenProvider);
  }

  private void doStandardAuth(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    headerTokenFilter.doFilter(request, response, filterChain);
  }

  private void doDownloadAuth(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    parameterTokenFilter.doFilter(request, response, filterChain);
  }

  private boolean isDownload(HttpServletRequest request) {
    return request.getMethod().equals(HttpMethod.GET.toString()) && DOWNLOAD_PATTERN.matcher(request.getRequestURI()).matches();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String headerToken = headerTokenResolver.resolve(request);
    if (headerToken != null) {
      doStandardAuth(request, response, filterChain);
      return;
    }

    if (isDownload(request)) {
      final String parameterToken = parameterTokenProvider.resolve(request);
      if (parameterToken != null) {
        doDownloadAuth(request, response, filterChain);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !request.getRequestURI().contains("/api/v1/sample-interval/");
  }
}
