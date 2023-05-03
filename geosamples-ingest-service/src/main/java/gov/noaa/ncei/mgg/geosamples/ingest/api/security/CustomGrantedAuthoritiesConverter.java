package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import gov.noaa.ncei.mgg.geosamples.ingest.service.UserService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private final UserService userService;

  @Autowired
  public CustomGrantedAuthoritiesConverter(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    return userService.getUserAuthorities(jwt.getSubject()).stream()
        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }


}
