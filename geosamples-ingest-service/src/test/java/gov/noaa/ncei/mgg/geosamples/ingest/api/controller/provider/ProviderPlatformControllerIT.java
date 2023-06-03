 package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
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
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderPlatformService;
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
public class ProviderPlatformControllerIT {

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

  private static final ProviderPlatformService service = mock(ProviderPlatformService.class);

  @TestConfiguration
  static class TestConfig {
    @Primary
    @Bean
    public ProviderPlatformService testProviderPlatformService() {
      return service;
    }
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

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreatePlatformCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_CREATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderPlatformView view = new ProviderPlatformView();
    view.setPlatform("platform");

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.POST, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreatePlatformCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderPlatformView view = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreatePlatformToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_CREATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderPlatformView view = new ProviderPlatformView();
    view.setPlatform("platform");

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.POST, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreatePlatformTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderPlatformView view = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreatePlatformNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    ProviderPlatformView view = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(view, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetPlatformCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetPlatformCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetPlatformToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetPlatformTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetPlatformNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchPlatformCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchPlatformCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchPlatformToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchPlatformTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchPlatformNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchUnapprovedPlatformsCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/unapproved", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchUnapprovedPlatformsCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/unapproved", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchUnapprovedPlatformsToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/unapproved", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchUnapprovedPlatformsTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/unapproved", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchUnapprovedPlatformsNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/unapproved", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/approval/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/approval/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/approval/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/approval/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetApprovalNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/approval/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdatePlatformCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderPlatformView platform = new ProviderPlatformView();
    platform.setPlatform("platform");

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(platform, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.PUT, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdatePlatformCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderPlatformView platform = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(platform, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdatePlatformToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderPlatformView platform = new ProviderPlatformView();
    platform.setPlatform("platform");

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(platform, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.PUT, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdatePlatformTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderPlatformView platform = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(platform, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdatePlatformNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    ProviderPlatformView platform = new ProviderPlatformView();

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(platform, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeletePlatformCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.DELETE, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeletePlatformCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.DELETE, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeletePlatformToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_PLATFORM_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.DELETE, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeletePlatformTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.DELETE, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeletePlatformNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<ProviderPlatformView> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/platform/1", HttpMethod.DELETE, request, String.class);
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
