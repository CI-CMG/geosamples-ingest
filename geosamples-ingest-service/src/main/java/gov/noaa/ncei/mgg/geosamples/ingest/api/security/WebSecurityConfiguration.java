package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final ApiAccessDeniedHandler accessDeniedHandler;

  @Autowired
  public WebSecurityConfiguration(ApiAccessDeniedHandler accessDeniedHandler) {
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
      .authorizeRequests()
        .anyRequest().permitAll()
        .and()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(accessDeniedHandler)
        .and()
      .httpBasic().disable()
      .formLogin().disable()
      .logout().disable()
      .csrf().disable();
    //@formatter:on
  }
}
