package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final ApiAccessDeniedHandler accessDeniedHandler;
  private final JwtUrlFilter jwtUrlFilter;

  @Autowired
  public ApiWebSecurityConfiguration(ApiAccessDeniedHandler accessDeniedHandler, JwtUrlFilter jwtUrlFilter) {
    this.accessDeniedHandler = accessDeniedHandler;
    this.jwtUrlFilter = jwtUrlFilter;
  }



  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
      .antMatcher("/api/**")
        .httpBasic().disable()
        .formLogin().disable()
        .addFilterBefore(jwtUrlFilter, BearerTokenAuthenticationFilter.class)
      .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/api/v1/age", "/api/v1/age/*").hasAuthority(Authorities.ROLE_AGE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/age").hasAuthority(Authorities.ROLE_AGE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/age/*").hasAuthority(Authorities.ROLE_AGE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/age/*").hasAuthority(Authorities.ROLE_AGE_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/device", "/api/v1/device/*").hasAuthority(Authorities.ROLE_DEVICE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/device").hasAuthority(Authorities.ROLE_DEVICE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/device/*").hasAuthority(Authorities.ROLE_DEVICE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/device/*").hasAuthority(Authorities.ROLE_DEVICE_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/facility", "/api/v1/facility/*").hasAuthority(Authorities.ROLE_FACILITY_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/facility").hasAuthority(Authorities.ROLE_FACILITY_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/facility/*").hasAuthority(Authorities.ROLE_FACILITY_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/facility/*").hasAuthority(Authorities.ROLE_FACILITY_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/lithology", "/api/v1/lithology/*").hasAuthority(Authorities.ROLE_LITHOLOGY_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/lithology").hasAuthority(Authorities.ROLE_LITHOLOGY_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/lithology/*").hasAuthority(Authorities.ROLE_LITHOLOGY_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/lithology/*").hasAuthority(Authorities.ROLE_LITHOLOGY_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/munsell", "/api/v1/munsell/*").hasAuthority(Authorities.ROLE_MUNSELL_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/munsell").hasAuthority(Authorities.ROLE_MUNSELL_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/munsell/*").hasAuthority(Authorities.ROLE_MUNSELL_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/munsell/*").hasAuthority(Authorities.ROLE_MUNSELL_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/platform", "/api/v1/platform/*").hasAuthority(Authorities.ROLE_PLATFORM_READ.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/platform/approval/*").hasAuthority(Authorities.ROLE_PLATFORM_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/platform").hasAuthority(Authorities.ROLE_PLATFORM_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/platform/*").hasAuthority(Authorities.ROLE_PLATFORM_UPDATE.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/platform/review/*").hasAuthority(Authorities.ROLE_PLATFORM_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/platform/*").hasAuthority(Authorities.ROLE_PLATFORM_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/province", "/api/v1/province/*").hasAuthority(Authorities.ROLE_PROVINCE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/province").hasAuthority(Authorities.ROLE_PROVINCE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/province/*").hasAuthority(Authorities.ROLE_PROVINCE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/province/*").hasAuthority(Authorities.ROLE_PROVINCE_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/remark", "/api/v1/remark/*").hasAuthority(Authorities.ROLE_REMARK_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/remark").hasAuthority(Authorities.ROLE_REMARK_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/remark/*").hasAuthority(Authorities.ROLE_REMARK_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/remark/*").hasAuthority(Authorities.ROLE_REMARK_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/rock-lithology", "/api/v1/rock-lithology/*").hasAuthority(Authorities.ROLE_ROCK_LITHOLOGY_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/rock-lithology").hasAuthority(Authorities.ROLE_ROCK_LITHOLOGY_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/rock-lithology/*").hasAuthority(Authorities.ROLE_ROCK_LITHOLOGY_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/rock-lithology/*").hasAuthority(Authorities.ROLE_ROCK_LITHOLOGY_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/rock-mineral", "/api/v1/rock-mineral/*").hasAuthority(Authorities.ROLE_ROCK_MINERAL_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/rock-mineral").hasAuthority(Authorities.ROLE_ROCK_MINERAL_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/rock-mineral/*").hasAuthority(Authorities.ROLE_ROCK_MINERAL_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/rock-mineral/*").hasAuthority(Authorities.ROLE_ROCK_MINERAL_DELETE.toString())

        //TODO support per-facility access
        .antMatchers(HttpMethod.GET, "/api/v1/sample", "/api/v1/sample/*").hasAuthority(Authorities.ROLE_SAMPLE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/sample").hasAuthority(Authorities.ROLE_SAMPLE_CREATE.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/sample").hasAuthority(Authorities.ROLE_SAMPLE_UPDATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/sample/*").hasAuthority(Authorities.ROLE_SAMPLE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/sample/*").hasAuthority(Authorities.ROLE_SAMPLE_DELETE.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/sample/review/*").hasAuthority(Authorities.ROLE_SAMPLE_UPDATE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/sample/approval/*").hasAuthority(Authorities.ROLE_SAMPLE_READ.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/storage-method", "/api/v1/storage-method/*").hasAuthority(Authorities.ROLE_STORAGE_METHOD_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/storage-method").hasAuthority(Authorities.ROLE_STORAGE_METHOD_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/storage-method/*").hasAuthority(Authorities.ROLE_STORAGE_METHOD_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/storage-method/*").hasAuthority(Authorities.ROLE_STORAGE_METHOD_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/texture", "/api/v1/texture/*").hasAuthority(Authorities.ROLE_TEXTURE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/texture").hasAuthority(Authorities.ROLE_TEXTURE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/texture/*").hasAuthority(Authorities.ROLE_TEXTURE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/texture/*").hasAuthority(Authorities.ROLE_TEXTURE_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/weathering", "/api/v1/weathering/*").hasAuthority(Authorities.ROLE_WEATHERING_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/weathering").hasAuthority(Authorities.ROLE_WEATHERING_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/weathering/*").hasAuthority(Authorities.ROLE_WEATHERING_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/weathering/*").hasAuthority(Authorities.ROLE_WEATHERING_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/me").hasAuthority(Authorities.ROLE_AUTHENTICATED_USER.toString())

        .antMatchers(HttpMethod.POST, "/api/v1/curator-data/upload").hasAuthority(Authorities.ROLE_DATA_MANAGER_CREATE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/interval", "/api/v1/interval/**").hasAuthority(Authorities.ROLE_DATA_MANAGER_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/interval").hasAuthority(Authorities.ROLE_DATA_MANAGER_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/interval/**").hasAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/interval/**").hasAuthority(Authorities.ROLE_DATA_MANAGER_DELETE.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/interval/review/*").hasAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/sample-interval").hasAuthority(Authorities.ROLE_DATA_MANAGER_READ.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/sample-interval/export").hasAuthority(Authorities.ROLE_DATA_MANAGER_READ.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/sample-interval").hasAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/sample-link", "/api/v1/sample-link/*").hasAuthority(Authorities.ROLE_SAMPLE_LINK_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/sample-link").hasAuthority(Authorities.ROLE_SAMPLE_LINK_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/sample-link/*").hasAuthority(Authorities.ROLE_SAMPLE_LINK_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/sample-link/*").hasAuthority(Authorities.ROLE_SAMPLE_LINK_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/user", "/api/v1/user/*").hasAuthority(Authorities.ROLE_USER_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/user").hasAuthority(Authorities.ROLE_USER_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/user/*").hasAuthority(Authorities.ROLE_USER_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/user/*").hasAuthority(Authorities.ROLE_USER_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/role", "/api/v1/role/*").hasAuthority(Authorities.ROLE_USER_ROLE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/role").hasAuthority(Authorities.ROLE_USER_ROLE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/role/*").hasAuthority(Authorities.ROLE_USER_ROLE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/role/*").hasAuthority(Authorities.ROLE_USER_ROLE_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/cruise", "/api/v1/cruise/*").hasAuthority(Authorities.ROLE_CRUISE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/cruise").hasAuthority(Authorities.ROLE_CRUISE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/cruise/*").hasAuthority(Authorities.ROLE_CRUISE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/cruise/*").hasAuthority(Authorities.ROLE_CRUISE_DELETE.toString())
        .antMatchers(HttpMethod.PATCH, "/api/v1/cruise/review/*").hasAuthority(Authorities.ROLE_CRUISE_UPDATE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/cruise/approval/*").hasAnyAuthority(Authorities.ROLE_CRUISE_READ.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/cruise-link", "/api/v1/cruise-link/*").hasAuthority(Authorities.ROLE_CRUISE_LINK_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/cruise-link").hasAuthority(Authorities.ROLE_CRUISE_LINK_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/cruise-link/*").hasAuthority(Authorities.ROLE_CRUISE_LINK_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/cruise-link/*").hasAuthority(Authorities.ROLE_CRUISE_LINK_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/provider/sample", "/api/v1/provider/sample/*").hasAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/provider/sample").hasAuthority(Authorities.ROLE_PROVIDER_SAMPLE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/provider/sample/*").hasAuthority(Authorities.ROLE_PROVIDER_SAMPLE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/provider/sample/*").hasAuthority(Authorities.ROLE_PROVIDER_SAMPLE_DELETE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/provider/sample/approval/*").hasAnyAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/provider/cruise", "/api/v1/provider/cruise/*").hasAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/provider/cruise").hasAuthority(Authorities.ROLE_PROVIDER_CRUISE_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/provider/cruise/*").hasAuthority(Authorities.ROLE_PROVIDER_CRUISE_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/provider/cruise/*").hasAuthority(Authorities.ROLE_PROVIDER_CRUISE_DELETE.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/provider/cruise/approval/*").hasAnyAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/provider/interval", "/api/v1/provider/interval/*").hasAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/provider/interval/approval/*").hasAnyAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/provider/interval").hasAuthority(Authorities.ROLE_PROVIDER_INTERVAL_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/provider/interval/*").hasAuthority(Authorities.ROLE_PROVIDER_INTERVAL_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/provider/interval/*").hasAuthority(Authorities.ROLE_PROVIDER_INTERVAL_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/provider/platform", "/api/v1/provider/platform/unapproved", "/api/v1/provider/platform/*").hasAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ.toString())
        .antMatchers(HttpMethod.GET, "/api/v1/provider/platform/approval/*").hasAnyAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/provider/platform").hasAuthority(Authorities.ROLE_PROVIDER_PLATFORM_CREATE.toString())
        .antMatchers(HttpMethod.PUT, "/api/v1/provider/platform/*").hasAuthority(Authorities.ROLE_PROVIDER_PLATFORM_UPDATE.toString())
        .antMatchers(HttpMethod.DELETE, "/api/v1/provider/platform/*").hasAuthority(Authorities.ROLE_PROVIDER_PLATFORM_DELETE.toString())

        .antMatchers(HttpMethod.GET, "/api/v1/descriptor/authority").hasAnyAuthority(
            Authorities.ROLE_USER_READ.toString(),
            Authorities.ROLE_USER_CREATE.toString(),
            Authorities.ROLE_USER_UPDATE.toString(),
            Authorities.ROLE_USER_DELETE.toString()
        )

        .antMatchers(HttpMethod.GET, "/api/v1/descriptor/role").hasAnyAuthority(
            Authorities.ROLE_USER_READ.toString(),
            Authorities.ROLE_USER_CREATE.toString(),
            Authorities.ROLE_USER_UPDATE.toString(),
            Authorities.ROLE_USER_DELETE.toString(),
            Authorities.ROLE_USER_ROLE_READ.toString(),
            Authorities.ROLE_USER_ROLE_CREATE.toString(),
            Authorities.ROLE_USER_ROLE_UPDATE.toString(),
            Authorities.ROLE_USER_ROLE_DELETE.toString()
        )

        .antMatchers(HttpMethod.POST, "/api/v1/user-token/*").hasAuthority(Authorities.ROLE_AUTHENTICATED_USER.toString())
        .antMatchers(HttpMethod.POST, "/api/v1/user-token/jwt").hasAuthority(Authorities.ROLE_AUTHENTICATED_USER.toString())


        .anyRequest().denyAll()
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
