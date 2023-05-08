package gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseView;
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
import gov.noaa.ncei.mgg.geosamples.ingest.service.provider.ProviderCruiseService;
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
public class ProviderCruiseControllerIT { // Tests that proper status codes are returned for various authentication scenarios. See ProviderCruiseServiceIT for more detailed tests
  
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
  
  private static final ProviderCruiseService service = mock(ProviderCruiseService.class);
  
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
    public ProviderCruiseService testProviderCruiseService() {
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
  public void testCreateProviderCruiseCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_CREATE, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderCruiseView view = new ProviderCruiseView();
    view.setYear(2020);

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.POST, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testCreateProviderCruiseCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));
    
    ProviderCruiseView view = new ProviderCruiseView();
    
    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(view, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testCreateProviderCruiseToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_CREATE, true);
    assertNotNull(tokenValue);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);
    
    ProviderCruiseView view = new ProviderCruiseView();
    view.setYear(2020);
    
    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(view, headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.POST, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testCreateProviderCruiseTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);
    
    ProviderCruiseView view = new ProviderCruiseView();
    
    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(view, headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testCreateProviderCruiseNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    
    ProviderCruiseView view = new ProviderCruiseView();
    
    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(view, headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.POST, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testGetProviderCruiseCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));
    
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testGetProviderCruiseCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));
    
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testGetProviderCruiseToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ, true);
    assertNotNull(tokenValue);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);
    
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testGetProviderCruiseTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);
    
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testGetProviderCruiseNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testSearchProviderCruiseCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));
    
    HttpEntity<String > httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testSearchProviderCruiseCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String > httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testSearchProviderCruiseToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String > httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.GET, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testSearchProviderCruiseTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String > httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testSearchProviderCruiseNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String > httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise", HttpMethod.GET, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testUpdateProviderCruiseCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_UPDATE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderCruiseView providerCruiseView = new ProviderCruiseView();
    providerCruiseView.setYear(2020);

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(providerCruiseView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testUpdateProviderCruiseCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    ProviderCruiseView providerCruiseView = new ProviderCruiseView();

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(providerCruiseView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testUpdateProviderCruiseToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_UPDATE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderCruiseView providerCruiseView = new ProviderCruiseView();
    providerCruiseView.setYear(2020);

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(providerCruiseView, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testUpdateProviderCruiseTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    ProviderCruiseView providerCruiseView = new ProviderCruiseView();

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(providerCruiseView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testUpdateProviderCruiseNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    ProviderCruiseView providerCruiseView = new ProviderCruiseView();

    HttpEntity<ProviderCruiseView> httpEntity = new HttpEntity<>(providerCruiseView, headers);
    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.PUT, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testDeleteProviderCruiseCAS() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_DELETE, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testDeleteProviderCruiseCASUnauthorized() throws JoseException, IOException {
    createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, false);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("gabby"));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testDeleteProviderCruiseToken() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_PROVIDER_CRUISE_DELETE, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(200, response.getStatusCodeValue());
  }
  
  @Test
  public void testDeleteProviderCruiseTokenUnauthorized() {
    final String tokenValue = createUserWithAuthority(Authorities.ROLE_DATA_MANAGER_READ, true);
    assertNotNull(tokenValue);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(tokenValue);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.DELETE, httpEntity, String.class);
    assertEquals(403, response.getStatusCodeValue());
  }
  
  @Test
  public void testDeleteProviderCruiseNoAuth() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/provider/cruise/1", HttpMethod.DELETE, httpEntity, String.class);
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
