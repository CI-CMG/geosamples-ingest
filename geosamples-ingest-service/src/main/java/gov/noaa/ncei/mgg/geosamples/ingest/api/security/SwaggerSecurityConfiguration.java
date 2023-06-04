package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

@Configuration
@Order(2)
public class SwaggerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final ApiAccessDeniedHandler apiAccessDeniedHandler;

  private final JwtUrlFilter jwtUrlFilter;

  @Autowired
  public SwaggerSecurityConfiguration(ApiAccessDeniedHandler apiAccessDeniedHandler, JwtUrlFilter jwtUrlFilter) {
    this.apiAccessDeniedHandler = apiAccessDeniedHandler;
    this.jwtUrlFilter = jwtUrlFilter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .antMatcher("/docs/**")
          .httpBasic().disable()
          .formLogin().disable()
          .addFilterBefore(jwtUrlFilter, BearerTokenAuthenticationFilter.class)
      .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/docs/api/v1-admin").hasAuthority("ROLE_USER_READ")
        .antMatchers(HttpMethod.GET, "/docs/api/provider").hasAnyAuthority(
            "ROLE_PROVIDER_SAMPLE_READ", "ROLE_PROVIDER_CRUISE_READ", "ROLE_PROVIDER_INTERVAL_READ", "ROLE_PROVIDER_PLATFORM_READ"
        )
        .anyRequest().denyAll()
        .and()
      .exceptionHandling()
        .accessDeniedHandler(apiAccessDeniedHandler)
        .authenticationEntryPoint(apiAccessDeniedHandler)
        .and()
      .httpBasic().disable()
      .formLogin().disable()
      .logout().disable()
        .csrf().disable();
  }
}
