package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwksInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwksInitializer.class);

  private String jwksJson;

  @Autowired
  public JwksInitializer(ServiceProperties serviceProperties) {
    try {
      createJwks(serviceProperties);
    } catch (JoseException | IOException e) {
      throw new IllegalStateException("Unable to set up JWKS", e);
    }
  }

  private static final int number = 1;
  private static final int size = 2048;
  private static final String alg = AlgorithmIdentifiers.RSA_USING_SHA256;
  private static final String use = "sig";

  private static String genSecret(int keySize) {
    OctetSequenceJsonWebKey octetKey = OctJwkGenerator.generateJwk(keySize);
    Map<String, Object> params = octetKey.toParams(JsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC);
    return params.get("k").toString();
  }

  private void createJwks(ServiceProperties serviceProperties) throws JoseException, IOException {
    Path jwksPath = Paths.get(serviceProperties.getLocalJwkSetPath());
    Path parent = jwksPath.getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
    if (!Files.exists(jwksPath)) {
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
      jwksJson = jwks.toJson(OutputControlLevel.INCLUDE_PRIVATE);
      Files.write(jwksPath, jwksJson.getBytes(StandardCharsets.UTF_8));
      LOGGER.info("Created new JWKS: {}", jwksPath);
    } else {
      LOGGER.info("Using existing JWKS: {}", jwksPath);
      jwksJson = FileUtils.readFileToString(jwksPath.toFile(), StandardCharsets.UTF_8);
    }
  }

  public String getJwksJson() {
    return jwksJson;
  }
}
