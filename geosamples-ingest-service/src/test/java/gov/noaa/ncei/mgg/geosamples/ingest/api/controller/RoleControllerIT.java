package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.RoleService;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenGenerator;
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
public class RoleControllerIT {

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

  private static final RoleService roleService = mock(RoleService.class);

  @TestConfiguration
  static class TestConfig {
    @Primary
    @Bean
    public RoleService testRoleService() {
      return roleService;
    }
  }

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TransactionTemplate transactionTemplate;

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
  private GeosamplesUserRepository userRepository;

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreateRoleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_USER_ROLE_CREATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    RoleView roleView = new RoleView();
    roleView.setRoleName("ROLE_TEST");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.POST, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateRoleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
    roleEntity.setRoleName("ROLE_TEST");

    HttpEntity<GeosamplesRoleEntity> request = new HttpEntity<>(roleEntity, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateRoleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_USER_ROLE_CREATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    RoleView roleView = new RoleView();
    roleView.setRoleName("ROLE_TEST");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.POST, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testCreateRoleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
    roleEntity.setRoleName("ROLE_TEST");

    HttpEntity<GeosamplesRoleEntity> request = new HttpEntity<>(roleEntity, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testCreateRoleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
    roleEntity.setRoleName("ROLE_TEST");

    HttpEntity<GeosamplesRoleEntity> request = new HttpEntity<>(roleEntity, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.POST, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetRoleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_USER_ROLE_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetRoleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetRoleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_USER_ROLE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testGetRoleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testGetRoleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchRoleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_USER_ROLE_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchRoleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchRoleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_USER_ROLE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.GET, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testSearchRoleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testSearchRoleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role", HttpMethod.GET, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateRoleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_USER_ROLE_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    RoleView roleView = new RoleView();
    roleView.setId(1L);
    roleView.setVersion(0L);
    roleView.setRoleName("ROLE_USER_ROLE_UPDATE");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.PUT, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateRoleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    RoleView roleView = new RoleView();
    roleView.setId(1L);
    roleView.setVersion(0L);
    roleView.setRoleName("ROLE_USER_ROLE_UPDATE");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateRoleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_USER_ROLE_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    RoleView roleView = new RoleView();
    roleView.setId(1L);
    roleView.setVersion(0L);
    roleView.setRoleName("ROLE_USER_ROLE_UPDATE");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.PUT, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateRoleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    RoleView roleView = new RoleView();
    roleView.setId(1L);
    roleView.setVersion(0L);
    roleView.setRoleName("ROLE_USER_ROLE_UPDATE");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testUpdateRoleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    RoleView roleView = new RoleView();
    roleView.setId(1L);
    roleView.setVersion(0L);
    roleView.setRoleName("ROLE_USER_ROLE_UPDATE");

    HttpEntity<RoleView> request = new HttpEntity<>(roleView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.PUT, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteRoleCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_USER_ROLE_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.DELETE, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteRoleCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.DELETE, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteRoleToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_USER_ROLE_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.DELETE, request, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteRoleTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.DELETE, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteRoleNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/role/1", HttpMethod.DELETE, request, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }

  private void cleanDB() {
    transactionTemplate.execute(status -> {
      userRepository.findById("martin").ifPresent(userRepository::delete);
      roleRepository.getByRoleName("ROLE_ADMIN").ifPresent(roleRepository::delete);
      return null;
    });
  }

  private @Nullable
  String createUserWithAuthority(@Nullable Authorities authority, boolean withToken) {
    return transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("martin");
      user.setDisplayName("Martin");

      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_ADMIN");
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
