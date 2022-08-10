package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import java.util.Collection;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiProviderAuthenticationToken extends AbstractAuthenticationToken {

  private final String subject;
  private String token;

  public ApiProviderAuthenticationToken(String token) {
    super(null);
    this.subject = null;
    this.token = Objects.requireNonNull(token);
    super.setAuthenticated(false);
  }

  public ApiProviderAuthenticationToken(String subject, Collection<? extends GrantedAuthority> authorities) {
    super(Objects.requireNonNull(authorities));
    this.subject = Objects.requireNonNull(subject);
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return subject;
  }

  public String getToken() {
    return token;
  }

  public String getSubject() {
    return subject;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    if (isAuthenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    token = null;
  }
}
