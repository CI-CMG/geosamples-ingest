package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String USER = "USER";

  private final ApiAccessDeniedHandler accessDeniedHandler;
  private final ServiceProperties serviceProperties;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public WebSecurityConfiguration(ApiAccessDeniedHandler accessDeniedHandler,
      ServiceProperties serviceProperties, PasswordEncoder passwordEncoder) {
    this.accessDeniedHandler = accessDeniedHandler;
    this.serviceProperties = serviceProperties;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //@formatter:off
    auth
      .inMemoryAuthentication()
      .withUser(serviceProperties.getUsername())
      .password(passwordEncoder.encode(serviceProperties.getPassword()))
      .roles(USER);
    //@formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
      .headers()
        .httpStrictTransportSecurity().disable()
        .and()
      .authorizeRequests()
        .antMatchers("/api/**").hasRole(USER)
        .anyRequest().permitAll()
        .and()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(accessDeniedHandler)
        .and()
      .httpBasic()
        .authenticationEntryPoint(accessDeniedHandler)
        .and()
      .formLogin().disable()
      .logout().disable()
      .csrf().disable();
    //@formatter:on
  }
}
