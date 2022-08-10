package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

  private static final SecureRandom RANDOM = new SecureRandom();

  private final ServiceProperties serviceProperties;

  @Autowired
  public TokenGenerator(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  public String generateToken() {
    byte[] bytes = new byte[serviceProperties.getTokenSizeBytes()];
    RANDOM.nextBytes(bytes);
    return new String(Base64.getUrlEncoder().withoutPadding().encode(bytes), StandardCharsets.US_ASCII);
  }

}
