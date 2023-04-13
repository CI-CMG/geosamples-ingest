package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

  private static final SecureRandom RANDOM = new SecureRandom();

  private final ServiceProperties serviceProperties;
  private final JwksInitializer jwksInitializer;

  @Autowired
  public TokenGenerator(ServiceProperties serviceProperties, JwksInitializer jwksInitializer) {
    this.serviceProperties = serviceProperties;
    this.jwksInitializer = jwksInitializer;
  }

  public String generateToken() {
    byte[] bytes = new byte[serviceProperties.getTokenSizeBytes()];
    RANDOM.nextBytes(bytes);
    return new String(Base64.getUrlEncoder().withoutPadding().encode(bytes), StandardCharsets.US_ASCII);
  }

  public String generateJwt(String subject) {
    try {
      JwtClaims claims = new JwtClaims();
      claims.setSubject(subject);
      NumericDate expirationTime = NumericDate.now();
      expirationTime.addSeconds(serviceProperties.getTokenExpirationSeconds());
      claims.setExpirationTime(expirationTime);
      JsonWebSignature jws = new JsonWebSignature();
      jws.setPayload(claims.toJson());
      JsonWebKeySet jwks = new JsonWebKeySet(jwksInitializer.getJwksJson());
      RsaJsonWebKey jwk = (RsaJsonWebKey) jwks.getJsonWebKeys().get(0);
      jws.setKey(jwk.getPrivateKey());
      jws.setKeyIdHeaderValue(jwk.getKeyId());
      jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
      return jws.getCompactSerialization();
    } catch (JoseException e) {
      throw new IllegalArgumentException("Failed to create jwt: ", e);
    }
  }

}
