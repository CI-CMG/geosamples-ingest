package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import java.util.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.jupiter.api.Test;

public class TokenGeneratorTest {

  private final TokenGenerator tokenGenerator = new TokenGenerator(mockServiceProperties());
  private final ObjectMapper objectMapper = new ObjectMapper();
  private static final int TOKEN_SIZE_BYTES = 64;

  @Test
  public void testGenerateJwt() throws JsonProcessingException {
    final String USERNAME = "AUTH-SUBJECT-USERNAME";
    String jwt = tokenGenerator.generateJwt(USERNAME);

    String[] components = jwt.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();

    String header = new String(decoder.decode(components[0]));
    String payload = new String(decoder.decode(components[1]));
    JsonNode headerNode = objectMapper.readTree(header);
    JsonNode payloadNode = objectMapper.readTree(payload);

    assertEquals(headerNode.get("alg").asText(), AlgorithmIdentifiers.RSA_USING_SHA256);
    assertEquals(payloadNode.get("sub").asText(), USERNAME);
    assertNotNull(payloadNode.get("exp").asText());
  }

  @Test
  public void testGenerateToken() {
    String token = tokenGenerator.generateToken();
    assertNotNull(token);
    byte[] bytes = Base64.getUrlDecoder().decode(token);
    assertEquals(TOKEN_SIZE_BYTES, bytes.length);
  }

  private static ServiceProperties mockServiceProperties() {
    final String TEST = "test";

    ServiceProperties properties = new ServiceProperties();
    properties.setAuthServiceUrl(TEST);
    properties.setRemoteJwtIssuer(TEST);
    properties.setClientId(TEST);
    properties.setShowSampleBaseUrl(TEST);
    properties.setTokenSizeBytes(TOKEN_SIZE_BYTES);
    properties.setLocalJwkSetUri("src/test/resources/jwks.json");
    properties.setTokenExpirationSeconds(10);
    return properties;
  }

}
