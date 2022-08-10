package gov.noaa.ncei.mgg.geosamples.ingest.service;

import java.util.Objects;
import java.util.Set;

public class TokenAuthorization {

  private final String subject;
  private final Set<String> authorities;

  public TokenAuthorization(String subject, Set<String> authorities) {
    this.subject = subject;
    this.authorities = authorities;
  }

  public String getSubject() {
    return subject;
  }

  public Set<String> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenAuthorization that = (TokenAuthorization) o;
    return Objects.equals(subject, that.subject) && Objects.equals(authorities, that.authorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, authorities);
  }

  @Override
  public String toString() {
    return "TokenAuthorization{" +
        "subject='" + subject + '\'' +
        ", authorities=" + authorities +
        '}';
  }
}
