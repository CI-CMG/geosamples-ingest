package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.ExcelInputService;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenGenerator;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class SampleIntervalControllerIT {

  private static MockWebServer mockCas;

  public static String createJwt(String subject) throws IOException, JoseException {
    JwtClaims claims = new JwtClaims();
    claims.setIssuer("http://localhost:20158");
    claims.setSubject(subject);
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(claims.toJson());
    JsonWebKeySet jwks = new JsonWebKeySet(FileUtils.readFileToString(Paths.get("src/test/resources/jwks.json").toFile(), StandardCharsets.UTF_8));
    RsaJsonWebKey jwk = (RsaJsonWebKey) jwks.getJsonWebKeys().get(0);
    jws.setKey(jwk.getPrivateKey());
    jws.setKeyIdHeaderValue(jwk.getKeyId());
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    return jws.getCompactSerialization();
  }

  @BeforeAll
  public static void setUpAll() throws IOException {
    mockCas = new MockWebServer();
    mockCas.start(20158);

    mockCas.setDispatcher(new Dispatcher() {
      @Override
      @NonNull
      public MockResponse dispatch(@NonNull RecordedRequest request) {
        if ("/jwks".equals(request.getPath())) {
          try {
            return new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(FileUtils.readFileToString(Paths.get("src/test/resources/jwks.json").toFile(), StandardCharsets.UTF_8));
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
  private TransactionTemplate txTemplate;
  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;
  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;
  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;
  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @BeforeEach
  public void beforeEach() throws IOException {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
      geosamplesRoleRepository.getByRoleName("CANNOT READ").ifPresent(geosamplesRoleRepository::delete);
      GeosamplesUserEntity martin = new GeosamplesUserEntity();
      martin.setDisplayName("Marty McPharty");
      martin.setUserName("martin");

      GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
      roleEntity.setRoleName("ROLE_ADMIN");
      roleEntity = geosamplesRoleRepository.save(roleEntity);

      for (GeosamplesAuthorityEntity authorityEntity : geosamplesAuthorityRepository.findAll()) {
        GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
        roleAuthorityEntity.setAuthority(authorityEntity);
        roleEntity.addRoleAuthority(roleAuthorityEntity);
      }
      martin.setUserRole(roleEntity);
      geosamplesUserRepository.save(martin);
    });
    Files.createDirectories(Paths.get("target").resolve("test"));
  }

  @AfterEach
  public void afterEach() throws IOException {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesUserRepository.deleteById("martin");
      if (geosamplesUserRepository.existsById("maurice")) {
        geosamplesUserRepository.deleteById("maurice");
      }
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
      geosamplesRoleRepository.getByRoleName("CANNOT READ").ifPresent(geosamplesRoleRepository::delete);
    });
    FileUtils.deleteDirectory(new File("target/test"));
  }

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ExcelInputService excelInputService;

  @Autowired
  private TokenGenerator tokenGenerator;

  private void createCruise(String cruiseName, List<String> facilityCodes, List<String> platforms, List<String> legs) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(createJwt("martin"));

    CruiseView cruiseView = new CruiseView();
    cruiseView.setCruiseName(cruiseName);
    cruiseView.setYear(2021);
    cruiseView.setPublish(true);
    cruiseView.setFacilityCodes(facilityCodes);
    cruiseView.setPlatforms(platforms);
    cruiseView.setLegs(legs);


    HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(cruiseView), headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/cruise", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

  private void uploadFile() throws Exception {
    LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", new ClassPathResource("imlgs_sample_good_full.xlsm"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBearerAuth(createJwt("martin"));

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  public void testExportAll() throws Exception {

    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    uploadFile();

    final String jwt = createJwt("martin");

    File outputFile = restTemplate.execute("/api/v1/sample-interval/export", HttpMethod.GET,
        response -> response.getHeaders().setBearerAuth(jwt),
        response -> {
      assertEquals(200, response.getStatusCode().value());
      File file = Files.createFile(Paths.get("target").resolve("test").resolve("output.xlsx")).toFile();
      StreamUtils.copy(response.getBody(), new FileOutputStream(file));
      return file;
    });

    List<SampleRow> expectedSampleRows;
    List<SampleRow> actualSampleRows;
    try (
        InputStream actualInputStream = new FileInputStream(outputFile);
        InputStream expectedInputStream = new FileInputStream("src/test/resources/test-export.xlsx")
    ) {
       expectedSampleRows = excelInputService.read(expectedInputStream).stream().sorted(Comparator.comparing(SampleRow::getSampleId)).collect(Collectors.toList());
       actualSampleRows = excelInputService.read(actualInputStream).stream().sorted(Comparator.comparing(SampleRow::getSampleId)).collect(Collectors.toList());
    }

    assertEquals(expectedSampleRows.size(), actualSampleRows.size());

    for (int i = 0; i < expectedSampleRows.size(); i++) {
      assertEquals(objectMapper.writeValueAsString(expectedSampleRows.get(i)), objectMapper.writeValueAsString(actualSampleRows.get(i)));
    }
  }

  @Test
  public void testExportAllAccessTokenParameter() throws Exception {

    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    uploadFile();

    final String jwt = tokenGenerator.generateJwt("martin");

    File outputFile = restTemplate.execute("/api/v1/sample-interval/export?access_token=" + jwt, HttpMethod.GET,
        null,
        response -> {
          assertEquals(200, response.getStatusCode().value());
          File file = Files.createFile(Paths.get("target").resolve("test").resolve("output.xlsx")).toFile();
          StreamUtils.copy(response.getBody(), new FileOutputStream(file));
          return file;
        });

    List<SampleRow> expectedSampleRows;
    List<SampleRow> actualSampleRows;
    try (
        InputStream actualInputStream = new FileInputStream(outputFile);
        InputStream expectedInputStream = new FileInputStream("src/test/resources/test-export.xlsx")
    ) {
      expectedSampleRows = excelInputService.read(expectedInputStream).stream().sorted(Comparator.comparing(SampleRow::getSampleId)).collect(Collectors.toList());
      actualSampleRows = excelInputService.read(actualInputStream).stream().sorted(Comparator.comparing(SampleRow::getSampleId)).collect(Collectors.toList());
    }

    assertEquals(expectedSampleRows.size(), actualSampleRows.size());

    for (int i = 0; i < expectedSampleRows.size(); i++) {
      assertEquals(objectMapper.writeValueAsString(expectedSampleRows.get(i)), objectMapper.writeValueAsString(actualSampleRows.get(i)));
    }
  }

  @Test
  public void testExportSearch() throws Exception {

    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    uploadFile();

    final String jwt = createJwt("martin");

    File outputFile = restTemplate.execute("/api/v1/sample-interval/export?cruiseContains=AQ-10", HttpMethod.GET,
        response -> response.getHeaders().setBearerAuth(jwt),
        response -> {
          assertEquals(200, response.getStatusCode().value());
          File file = Files.createFile(Paths.get("target").resolve("test").resolve("output.xlsx")).toFile();
          StreamUtils.copy(response.getBody(), new FileOutputStream(file));
          return file;
        });

    List<SampleRow> expectedSampleRows;
    List<SampleRow> actualSampleRows;
    try (
        InputStream actualInputStream = new FileInputStream(outputFile);
        InputStream expectedInputStream = new FileInputStream("src/test/resources/test-export.xlsx")
    ) {
      expectedSampleRows = excelInputService.read(expectedInputStream).stream().filter(r -> r.getCruiseId().equals("AQ-10")).collect(Collectors.toList());
      actualSampleRows = excelInputService.read(actualInputStream);
    }
    assertEquals(1, actualSampleRows.size());
    assertEquals(expectedSampleRows.size(), actualSampleRows.size());

    for (int i = 0; i < expectedSampleRows.size(); i++) {
      assertEquals(objectMapper.writeValueAsString(expectedSampleRows.get(i)), objectMapper.writeValueAsString(actualSampleRows.get(i)));
    }
  }

  @Test
  public void testExportUnauthorizedBearer() throws JoseException, IOException {
    txTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity maurice = new GeosamplesUserEntity();
      maurice.setDisplayName("Maurice Moss");
      maurice.setUserName("maurice");

      GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
      roleEntity.setRoleName("CANNOT READ");
      roleEntity = geosamplesRoleRepository.save(roleEntity);

      for (GeosamplesAuthorityEntity authorityEntity : geosamplesAuthorityRepository.findAll()) {
        if (!authorityEntity.getAuthorityName().equals(Authorities.ROLE_DATA_MANAGER_READ.toString())) {
          GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
          roleAuthorityEntity.setAuthority(authorityEntity);
          roleEntity.addRoleAuthority(roleAuthorityEntity);
        }
      }
      maurice.setUserRole(roleEntity);
      geosamplesUserRepository.save(maurice);
    });

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(createJwt("maurice"));

    ResponseEntity<String> response = restTemplate.exchange(
        "/api/v1/sample-interval/export",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        String.class
    );
    assertEquals(403, response.getStatusCodeValue());
  }

  @Test
  public void testExportUnauthorizedTokenParameter() {
    txTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity maurice = new GeosamplesUserEntity();
      maurice.setDisplayName("Maurice Moss");
      maurice.setUserName("maurice");

      GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
      roleEntity.setRoleName("CANNOT READ");
      roleEntity = geosamplesRoleRepository.save(roleEntity);

      for (GeosamplesAuthorityEntity authorityEntity : geosamplesAuthorityRepository.findAll()) {
        if (!authorityEntity.getAuthorityName().equals(Authorities.ROLE_DATA_MANAGER_READ.toString())) {
          GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
          roleAuthorityEntity.setAuthority(authorityEntity);
          roleEntity.addRoleAuthority(roleAuthorityEntity);
        }
      }
      maurice.setUserRole(roleEntity);
      geosamplesUserRepository.save(maurice);
    });

    ResponseEntity<String> response = restTemplate.exchange(
        "/api/v1/sample-interval/export?access_token=" + tokenGenerator.generateJwt("maurice"),
        HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()),
        String.class
    );
    assertEquals(403, response.getStatusCodeValue());
  }

}
