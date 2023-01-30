package gov.noaa.ncei.mgg.geosamples.ingest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableWebSecurity
@EnableTransactionManagement
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private static final int number = 1;
  private static final int size = 2048;
  private static final String alg = AlgorithmIdentifiers.RSA_USING_SHA256;
  private static final String use = "sig";

  private static String jwksJson = null;

  private static String genSecret(int keySize) {
    OctetSequenceJsonWebKey octetKey = OctJwkGenerator.generateJwk(keySize);
    Map<String, Object> params = octetKey.toParams(JsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC);
    return params.get("k").toString();
  }

  private static void createJwks(File svcHome) throws JoseException, IOException {
    Path jwksPath = svcHome.toPath().resolve("config/jwks.json");
    Path parent = jwksPath.getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
//    System.setProperty("spring.security.oauth2.resourceserver.jwt.public-key-location", jwksPath.toAbsolutePath().normalize().toString());
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

  public static void main(String[] args) {
    File svcHome = new ApplicationHome().getDir();
    String path = svcHome.getAbsolutePath();
    try {
      createJwks(svcHome);
    } catch (JoseException | IOException e) {
      throw new IllegalStateException("Unable to set up JWKS", e);
    }
    System.setProperty("svc.home", path);
    SpringApplication.run(Application.class, args);
  }

}
