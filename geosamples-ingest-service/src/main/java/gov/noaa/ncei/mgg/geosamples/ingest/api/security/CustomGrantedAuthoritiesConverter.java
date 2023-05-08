package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import gov.noaa.ncei.mgg.geosamples.ingest.service.UserService;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomGrantedAuthoritiesConverter.class);

  private final UserService userService;

  @Autowired
  public CustomGrantedAuthoritiesConverter(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    List<String> authorities = userService.getUserAuthorities(jwt.getSubject());
    LOGGER.debug("Authenticated {} with roles {}", jwt.getSubject(), authorities);
    return authorities.stream()
        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }


}
