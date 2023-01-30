package gov.noaa.ncei.mgg.geosamples.ingest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.OctJwkGenerator;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class JwksGenTest {

  private final int number = 1;
  private final int size = 2048;
  private final String alg = AlgorithmIdentifiers.RSA_USING_SHA256;
  private final String use = "sig";

  private static String genSecret(int keySize) {
    OctetSequenceJsonWebKey octetKey = OctJwkGenerator.generateJwk(keySize);
    Map<String, Object> params = octetKey.toParams(JsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC);
    return params.get("k").toString();
  }

  @Test
  public void testCreateJwks() throws Exception {
    RsaJsonWebKey[] keys = new RsaJsonWebKey[number];
    for (int i = 0; i < number; i++) {
      RsaJsonWebKey rsaJwk = RsaJwkGenerator.generateJwk(size);
      if (alg != null && !alg.isEmpty()) {
        rsaJwk.setAlgorithm(alg);
      }
      if (use != null && !use.isEmpty()) {
        rsaJwk.setUse(use);
      }
      rsaJwk.setKeyId(genSecret(32));
      keys[i] = rsaJwk;
    }
    JsonWebKeySet jwks = new JsonWebKeySet(keys);
    String jwksJson = jwks.toJson(OutputControlLevel.INCLUDE_PRIVATE);
    Files.write(Paths.get("src/test/resources/jwks.json"), jwksJson.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testCreateJwt() throws Exception {
    System.out.println(createJwt("foo"));
  }

  public static String createJwt(String subject) throws IOException, JoseException {
    JwtClaims claims = new JwtClaims();
    claims.setIssuer("http://localhost:20158");  // who creates the token and signs it
//    claims.setAudience("Audience"); // to whom the token is intended to be sent
//    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
//    claims.setGeneratedJwtId(); // a unique identifier for the token
//    claims.setIssuedAtToNow();  // when the token was issued/created (now)
//    claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
    claims.setSubject(subject); // the subject/principal is whom the token is about
//    claims.setStringListClaim("groups", Arrays.stream(ServiceAuthorities.values()).map(ServiceAuthorities::toString).collect(Collectors.toList()));
    JsonWebSignature jws = new JsonWebSignature();
    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());

    JsonWebKeySet jwks = new JsonWebKeySet(FileUtils.readFileToString(new File("src/test/resources/jwks.json"), StandardCharsets.UTF_8));

    RsaJsonWebKey jwk = (RsaJsonWebKey) jwks.getJsonWebKeys().get(0);

    // The JWT is signed using the private key
    jws.setKey(jwk.getPrivateKey());
    // Set the Key ID (kid) header because it's just the polite thing to do.
    // We only have one key in this example but a using a Key ID helps
    // facilitate a smooth key rollover process
    jws.setKeyIdHeaderValue(jwk.getKeyId());
    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
    // representation, which is a string consisting of three dot ('.') separated
    // base64url-encoded parts in the form Header.Payload.Signature
    // If you wanted to encrypt it, you can simply set this jwt as the payload
    // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
    return jws.getCompactSerialization();
  }

}

