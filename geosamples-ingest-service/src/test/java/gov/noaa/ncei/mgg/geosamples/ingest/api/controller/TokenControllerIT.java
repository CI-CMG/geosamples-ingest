package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TokenGenerateModel;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenService;
import gov.noaa.ncei.mgg.geosamples.ingest.service.UserService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("it")
public class TokenControllerIT {

  private static MockWebServer mockCas;

  @BeforeAll
  public static void setUpAll() throws IOException {
    mockCas = new MockWebServer();
    mockCas.start(20158);

    mockCas.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        switch (request.getPath()) {
          case "/jwks":
            try {
              return new MockResponse()
                  .setResponseCode(200)
                  .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                  .setBody(FileUtils.readFileToString(new File("src/test/resources/jwks.json"), StandardCharsets.UTF_8));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
        }
        return new MockResponse().setResponseCode(404);
      }
    });

  }

  @AfterAll
  public static void tearDownAll() throws IOException {
    mockCas.shutdown();
  }

  @Autowired
  private TestRestTemplate restTemplate;

  private static final UserService mockUserService = mock(UserService.class);
  private static final TokenService mockTokenService = mock(TokenService.class);

  @TestConfiguration
  public static class TestConfig {
    @Primary
    @Bean
    public UserService mockUserService() {
      return mockUserService;
    }

    @Primary
    @Bean
    public TokenService mockTokenService() {
      return mockTokenService;
    }
  }

  @Test
  public void testGenerateJwt() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(eq("fred"))).thenReturn(
        Collections.singletonList(Authorities.ROLE_AUTHENTICATED_USER.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/jwt")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGenerateJwtUnauthorized() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(any())).thenReturn(
        Collections.singletonList(Authorities.ROLE_DATA_MANAGER_READ.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/jwt")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGenerateJWtNoAuth() {
    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/jwt")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGenerateToken() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(any())).thenReturn(
        Collections.singletonList(Authorities.ROLE_AUTHENTICATED_USER.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/generate")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));

    TokenGenerateModel model = new TokenGenerateModel();
    model.setAlias("test");
    HttpEntity<TokenGenerateModel> httpEntity = new HttpEntity<>(model, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGenerateTokenUnauthorized() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(any())).thenReturn(
        Collections.singletonList(Authorities.ROLE_DATA_MANAGER_CREATE.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/generate")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGenerateTokenNoAuth() {
    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/generate")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteToken() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(any())).thenReturn(
        Collections.singletonList(Authorities.ROLE_AUTHENTICATED_USER.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/delete")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));

    TokenGenerateModel model = new TokenGenerateModel();
    model.setAlias("test");
    HttpEntity<TokenGenerateModel> httpEntity = new HttpEntity<>(model, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteTokenUnauthorized() throws JoseException, IOException {
    UserView fred = new UserView();
    fred.setUserName("fred");
    fred.setDisplayName("Fred");

    when(mockUserService.getUserAuthorities(any())).thenReturn(
        Collections.singletonList(Authorities.ROLE_DATA_MANAGER_CREATE.toString())
    );

    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/delete")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("fred"));

    TokenGenerateModel model = new TokenGenerateModel();
    model.setAlias("test");
    HttpEntity<TokenGenerateModel> httpEntity = new HttpEntity<>(model, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteTokenNoAuth() {
    URI uri = UriComponentsBuilder.fromPath("/api/v1/user-token/delete")
        .build().encode().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    TokenGenerateModel model = new TokenGenerateModel();
    model.setAlias("test");
    HttpEntity<TokenGenerateModel> httpEntity = new HttpEntity<>(model, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        uri,
        HttpMethod.POST,
        httpEntity,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
  }

}
