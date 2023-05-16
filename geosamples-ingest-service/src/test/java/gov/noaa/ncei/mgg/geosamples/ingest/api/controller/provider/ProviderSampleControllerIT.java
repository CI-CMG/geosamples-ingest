package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleView;
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
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderSampleService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("it")
public class ProviderSampleControllerIT { // Tests that proper status codes are returned for various authentication scenarios. See ProviderSampleServiceIT for more detailed tests

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

  private static final ProviderSampleService service = mock(ProviderSampleService.class);

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
    public ProviderSampleService testProviderSampleService() {
      return service;
    }
  }

  @Autowired
  private GeosamplesUserRepository userRepository;

  @Autowired
  private GeosamplesRoleRepository roleRepository;

  @Autowired
  private GeosamplesAuthorityRepository authorityRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TokenGenerator tokenGenerator;

  @Autowired
  @Qualifier("searchableHashEncoder")
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreateProviderSampleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_CREATE, true);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderSampleView view = new ProviderSampleView();
    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.POST, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderSampleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.POST, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderSampleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_CREATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.POST, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderSampleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.POST, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateProviderSampleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.POST, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderSampleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, true);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderSampleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderSampleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderSampleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetProviderSampleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderSamplesCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderSamplesCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderSamplesToken() {
    String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderSamplesTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchProviderSamplesNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/approval/1", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/approval/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalToken() {
    String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/approval/1", HttpMethod.GET, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/approval/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/approval/1", HttpMethod.GET, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderSampleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_UPDATE, true);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderSampleView view = new ProviderSampleView();
    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.PUT, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderSampleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderSampleView view = new ProviderSampleView();
    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.PUT, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderSampleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderSampleView view = new ProviderSampleView();
    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.PUT, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderSampleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.PUT, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateProviderSampleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    ProviderSampleView view = new ProviderSampleView();

    HttpEntity<ProviderSampleView> entity = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.PUT, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderSampleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.DELETE, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderSampleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.DELETE, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderSampleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_SAMPLE_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.DELETE, entity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderSampleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.DELETE, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProviderSampleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/sample/1", HttpMethod.DELETE, entity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  private void cleanDB() {
    transactionTemplate.execute(status -> {
      userRepository.findById("gabby").ifPresent(userRepository::delete);
      roleRepository.getByRoleName("ROLE_USER").ifPresent(roleRepository::delete);
      return null;
    });
  }

  private @Nullable String createUserWithAuthority(@Nullable Authorities authority, boolean withToken) {
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
