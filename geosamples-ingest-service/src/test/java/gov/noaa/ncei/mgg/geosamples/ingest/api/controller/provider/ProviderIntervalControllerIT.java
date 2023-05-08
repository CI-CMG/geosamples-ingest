package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenGenerator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderIntervalService;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("it")
public class ProviderIntervalControllerIT { // Tests that proper status codes are returned for various authentication scenarios. See ProviderIntervalServiceIT for more detailed tests

  private static MockWebServer mockCas;

  @BeforeAll
  public static void setUpAll() throws IOException {
    mockCas = new MockWebServer();
    mockCas.start(20158);

    mockCas.setDispatcher(new Dispatcher() {
      @NotNull
      @Override
      public MockResponse dispatch(@NotNull RecordedRequest request) {
        if ("/jwks".equals(request.getPath())) {
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

  private static final ProviderIntervalService service = mock(ProviderIntervalService.class);

  static {
    when(service.create(any(), any())).thenReturn(null);
    when(service.get(any(), any())).thenReturn(null);
    when(service.search(any(), any())).thenReturn(null);
    when(service.update(any(), any(), any())).thenReturn(null);
    when(service.delete(any(), any())).thenReturn(null);
  }

  @TestConfiguration
  static class TestConfig {
    @Primary
    @Bean
    public ProviderIntervalService testProviderIntervalService() {
      return service;
    }
  }

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private GeosamplesUserRepository userRepository;

  @Autowired
  private GeosamplesRoleRepository roleRepository;

  @Autowired
  private GeosamplesAuthorityRepository authorityRepository;

  @Autowired
  private TokenGenerator tokenGenerator;

  @Autowired
  @Qualifier("searchableHashEncoder")
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testCreateProviderIntervalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_CREATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.POST, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderIntervalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderIntervalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_CREATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.POST, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderIntervalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderIntervalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderIntervalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderIntervalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderIntervalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderIntervalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderIntervalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderIntervalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderIntervalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderIntervalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderIntervalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderIntervalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderIntervalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderIntervalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderIntervalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderIntervalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderIntervalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    IntervalView intervalView = new IntervalView();

    HttpEntity<IntervalView> httpEntity = new HttpEntity<>(intervalView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderIntervalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderIntervalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderIntervalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_INTERVAL_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderIntervalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderIntervalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/interval/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  private void cleanDB() {
    transactionTemplate.execute(status -> {
      userRepository.findById("gabby").ifPresent(userRepository::delete);
      roleRepository.getByRoleName("ROLE_USER").ifPresent(roleRepository::delete);
      return null;
    });
  }

  private @Nullable
  String createUserWithAuthority(@Nullable Authorities authority, boolean withToken) {
    return transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");

      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      role = roleRepository.save(role);
      if (authority != null) {
        GeosamplesAuthorityEntity authorityEntity = authorityRepository.findById(authority.toString()).orElseThrow(
            () -> new RuntimeException("Authority not found")
        );
        GeosamplesRoleAuthorityEntity roleAuthority = new GeosamplesRoleAuthorityEntity();
        roleAuthority.setRole(role);
        roleAuthority.setAuthority(authorityEntity);
        role.addRoleAuthority(roleAuthority);
      }
      user.setUserRole(role);

      String tokenValue = null;
      if (withToken) {
        GeosamplesTokenEntity token = new GeosamplesTokenEntity();
        tokenValue = tokenGenerator.generateToken();
        token.setTokenHash(passwordEncoder.encode(tokenValue));
        token.setAlias("TEST");
        user.addToken(token);
      }

      userRepository.save(user);
      return tokenValue;
    });
  }
}
