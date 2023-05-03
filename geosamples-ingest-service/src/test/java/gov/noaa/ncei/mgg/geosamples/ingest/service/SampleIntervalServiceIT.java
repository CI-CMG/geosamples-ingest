package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class SampleIntervalServiceIT {

  private static MockWebServer mockCas;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TransactionTemplate txTemplate;

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;

  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private SampleIntervalService sampleIntervalService;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @BeforeAll
  public static void setUpAll() throws IOException {
    mockCas = new MockWebServer();
    mockCas.start(20158);

    mockCas.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
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

  @BeforeEach
  public void before() {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
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
  }

  @AfterEach
  public void after() {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesUserRepository.deleteById("martin");
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
    });
  }

  @Test
  public void testSearchByFacility() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full.xlsm");

    CombinedIntervalSampleSearchParameters searchParameters = new CombinedIntervalSampleSearchParameters();
    searchParameters.setFacilityCode(Collections.singletonList("GEOMAR"));
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    PagedItemsView<CombinedSampleIntervalView> result = sampleIntervalService.search(searchParameters);
    assertEquals(5, result.getTotalItems());
    assertEquals(1, result.getTotalPages());
    assertEquals(5, result.getItems().size());
    assertEquals("AQ-01-01", result.getItems().get(0).getSample());
    assertEquals("AQ-01-01", result.getItems().get(1).getSample());
    assertEquals("AQ-001", result.getItems().get(2).getSample());
    assertEquals("AQ-002", result.getItems().get(3).getSample());
    assertEquals("AQ-003", result.getItems().get(4).getSample());
  }

  @Test
  public void testSearchByGeologicAgeCode() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile("imlgs_sample_good_full.xlsm");

    CombinedIntervalSampleSearchParameters searchParameters = new CombinedIntervalSampleSearchParameters();
    searchParameters.setAgeCode(Collections.singletonList("58"));

    PagedItemsView<CombinedSampleIntervalView> pagedItemsView = sampleIntervalService.search(searchParameters);
    assertEquals(3, pagedItemsView.getTotalItems());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());

    txTemplate.executeWithoutResult(s -> {
      List<CuratorsIntervalEntity> expectedIntervals = curatorsIntervalRepository.findAll().stream()
          .filter(i ->
              i.getAges().stream()
                  .map(CuratorsAgeEntity::getAgeCode)
                  .collect(Collectors.toList())
                  .contains("58")
          ).sorted(Comparator.comparing(CuratorsIntervalEntity::getInterval))
          .collect(Collectors.toList());

      List<CombinedSampleIntervalView> intervals = pagedItemsView.getItems().stream()
          .sorted(Comparator.comparing(CombinedSampleIntervalView::getInterval)).collect(Collectors.toList());

      assertEquals(expectedIntervals.size(), intervals.size());
      for (int i = 0; i < expectedIntervals.size(); i++) {
        CuratorsIntervalEntity expectedInterval = expectedIntervals.get(i);
        CombinedSampleIntervalView interval = intervals.get(i);
        assertEquals(expectedInterval.getSample().getSample(), interval.getSample());
        assertEquals(expectedInterval.getInterval(), interval.getInterval());
        assertEquals(2, interval.getAges().size());

        Set<String> expectedAges = new HashSet<>();
        expectedAges.add("58");
        expectedAges.add("00");

        assertEquals(expectedAges, new HashSet<>(interval.getAges()));
        assertEquals(
            expectedInterval.getAges().stream()
                .map(CuratorsAgeEntity::getAgeCode)
                .collect(Collectors.toSet()),
            new HashSet<>(interval.getAges())
        );
      }
    });
  }

  private void createCruise(String cruiseName, Integer year, List<String> facilityCodes, List<String> platforms, List<String> legs) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(createJwt("martin"));

    CruiseView cruiseView = new CruiseView();
    cruiseView.setCruiseName(cruiseName);
    cruiseView.setYear(year);
    cruiseView.setPublish(true);
    cruiseView.setFacilityCodes(facilityCodes);
    cruiseView.setPlatforms(platforms);
    cruiseView.setLegs(legs);


    HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(cruiseView), headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/cruise", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

  private static String createJwt(String subject) throws IOException, JoseException {
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

  private void uploadFile(String file) throws Exception {
    LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", new ClassPathResource(file));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBearerAuth(createJwt("martin"));

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

}
