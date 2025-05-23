package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
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
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SpreadsheetValidationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class CuratorPreviewPersistenceServiceIT {

  private static MockWebServer mockCas;

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Autowired
  private TransactionTemplate txTemplate;
  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;
  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;
  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

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
  private void before() {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
      GeosamplesUserEntity martin = new GeosamplesUserEntity();
      martin.setDisplayName("Marty McPharty");
      martin.setUserName("martin");

      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_ADMIN");
      role = geosamplesRoleRepository.save(role);

      for (GeosamplesAuthorityEntity authorityEntity : geosamplesAuthorityRepository.findAll()) {
        GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
        roleAuthorityEntity.setAuthority(authorityEntity);
        role.addRoleAuthority(roleAuthorityEntity);
      }
      martin.setUserRole(role);
      geosamplesUserRepository.save(martin);
    });
  }

  @AfterEach
  private void after() {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      geosamplesUserRepository.deleteById("martin");
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
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

  private String uploadFileExpectingError(String file) throws Exception {
    LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", new ClassPathResource(file));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBearerAuth(createJwt("martin"));

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, entity, String.class);

    assertEquals(400, response.getStatusCode().value());
    return response.getBody();
  }

  private static final TypeReference<PagedItemsView<CombinedSampleIntervalView>> ITEMS_VIEW = new TypeReference<PagedItemsView<CombinedSampleIntervalView>>() {
  };

  private List<CombinedSampleIntervalView> getAll() throws Exception {

    List<CombinedSampleIntervalView> views = new ArrayList<>();

    int page = 1;
    int maxPage = 1;
    while (page <= maxPage) {

      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.setBearerAuth(createJwt("martin"));
      headers.put("page", Arrays.asList("1"));

      HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

      ResponseEntity<String> response = restTemplate.exchange("/api/v1/sample-interval", HttpMethod.GET, entity, String.class);

      assertEquals(200, response.getStatusCode().value());

      PagedItemsView<CombinedSampleIntervalView> itemsView = objectMapper.readValue(response.getBody(), ITEMS_VIEW);
      views.addAll(itemsView.getItems());

      page++;
      maxPage = itemsView.getTotalPages();
    }

    return views;

  }

  @Test
  public void testSaveHappyPath() throws Exception {

    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile("imlgs_sample_good_full.xlsm");
    List<CombinedSampleIntervalView> sampleIntervals = getAll();

    assertEquals(5, sampleIntervals.size());

    CombinedSampleIntervalView view = sampleIntervals.get(0);

    String baseShowSamplUrl = "https://maps.ngdc.noaa.gov/viewers/imlgs/samples/";
    assertEquals("AQ-01", view.getCruise());
    assertEquals("AQ-01-01", view.getSample());
    assertEquals("GEOMAR", view.getFacility());
    assertEquals("African Queen", view.getPlatform());
    assertEquals("F", view.getDevice());
    assertEquals(null, view.getShipCode());
    assertEquals("20210729", view.getBeginDate());
    assertEquals("20210730", view.getEndDate());
    assertEquals(39.68416154, view.getLat(), 0.0001);
    assertEquals(40.684161537, view.getEndLat(), 0.0001);
    assertEquals(26.97946201, view.getLon(), 0.0001);
    assertEquals(27.979462008, view.getEndLon(), 0.0001);
    assertEquals(14, view.getWaterDepth());
    assertEquals(15, view.getEndWaterDepth());
    assertEquals(536, view.getCoredLength());
    assertEquals(4, view.getCoredLengthMm());
    assertEquals(10, view.getCoredDiam());
    assertEquals(0, view.getCoredDiamMm());
    assertEquals(null, view.getPi());
    assertEquals(null, view.getProvince());
    assertEquals("Lake 4", view.getSampleLake());
    assertNotNull(view.getLastUpdate());
    assertEquals(null, view.getIgsn());
    assertEquals(null, view.getLeg());
    assertEquals("Sample comment 4", view.getSampleComments());
    assertEquals(baseShowSamplUrl + view.getImlgs(), view.getShowSampl());
    assertNotNull(view.getImlgs());
    assertEquals(1, view.getInterval());
    assertEquals(1, view.getDepthTop());
    assertEquals(5, view.getDepthTopMm());
    assertEquals(3, view.getDepthBot());
    assertEquals(5, view.getDepthBotMm());
    assertEquals("5", view.getLith1());
    assertEquals(null, view.getText1());
    assertEquals(null, view.getLith2());
    assertEquals(null, view.getText2());
    assertEquals(null, view.getComp1());
    assertEquals(null, view.getComp2());
    assertEquals(null, view.getComp3());
    assertEquals(null, view.getComp4());
    assertEquals(null, view.getComp5());
    assertEquals(null, view.getComp6());
    assertEquals(null, view.getDescription());
    assertEquals(Arrays.asList("00", "54"), view.getAges());
    assertEquals(null, view.getWeight());
    assertEquals(null, view.getRockLith());
    assertEquals(null, view.getRockMin());
    assertEquals(null, view.getWeathMeta());
    assertEquals(null, view.getRemark());
    assertEquals(null, view.getMunsellCode());
    assertEquals(null, view.getMunsell());
    assertEquals(null, view.getExhaustCode());
    assertEquals(null, view.getPhotoLink());
    assertEquals(null, view.getIntComments());
    assertEquals(null, view.getIntervalIgsn());
    assertEquals(null, view.getIntervalParentIsgn());
    assertEquals(false, view.isPublish());

    view = sampleIntervals.get(2);

    assertEquals("AQ-10", view.getCruise());
    assertEquals("AQ-001", view.getSample());
    assertEquals("GEOMAR", view.getFacility());
    assertEquals("B", view.getStorageMeth());
    assertEquals("African Queen", view.getPlatform());
    assertEquals("F", view.getDevice());
    assertEquals(null, view.getShipCode());
    assertEquals("20210727", view.getBeginDate());
    assertEquals("20210827", view.getEndDate());
    assertEquals(38.68416154, view.getLat(), 0.0001);
    assertEquals(39.684161537, view.getEndLat(), 0.0001);
    assertEquals(25.97946201, view.getLon(), 0.0001);
    assertEquals(26.979462008, view.getEndLon(), 0.0001);
    assertEquals(14, view.getWaterDepth());
    assertEquals(15, view.getEndWaterDepth());
    assertEquals(537, view.getCoredLength());
    assertEquals(6, view.getCoredLengthMm());
    assertEquals(10, view.getCoredDiam());
    assertEquals(7, view.getCoredDiamMm());
    assertEquals("Rocky", view.getPi());
    assertEquals("11", view.getProvince());
    assertEquals("Lake 1", view.getSampleLake());
    assertNotNull(view.getLastUpdate());
    assertEquals("aasw32111", view.getIgsn());
    assertEquals("AQ-LEFT-LEG", view.getLeg());
    assertEquals("Sample comment 1", view.getSampleComments());
    assertEquals(baseShowSamplUrl + view.getImlgs(), view.getShowSampl());
    assertNotNull(view.getImlgs());
    assertEquals(1, view.getInterval());
    assertEquals(11, view.getDepthTop());
    assertEquals(5, view.getDepthTopMm());
    assertEquals(13, view.getDepthBot());
    assertEquals(3, view.getDepthBotMm());
    assertEquals("K", view.getLith1());
    assertEquals("N", view.getText1());
    assertEquals("9", view.getLith2());
    assertEquals("7", view.getText2());
    assertEquals("8", view.getComp1());
    assertEquals("M", view.getComp2());
    assertEquals("N", view.getComp3());
    assertEquals("O", view.getComp4());
    assertEquals("P", view.getComp5());
    assertEquals("Q", view.getComp6());
    assertEquals("Test Description", view.getDescription());
    assertEquals(Arrays.asList("00", "58"), view.getAges());
    assertEquals(50.5, view.getWeight(), 0.01);
    assertEquals("2P", view.getRockLith());
    assertEquals("E", view.getRockMin());
    assertEquals("0", view.getWeathMeta());
    assertEquals("5", view.getRemark());
    assertEquals("5Y 3/2", view.getMunsellCode());
    assertEquals("dark olive gray", view.getMunsell());
    assertEquals("X", view.getExhaustCode());
    assertEquals(null, view.getPhotoLink());
    assertEquals("Test Comments", view.getIntComments());
    assertEquals("child1", view.getIntervalIgsn());
    assertEquals("aasw32111", view.getIntervalParentIsgn());
    assertEquals(false, view.isPublish());

  }

  @Test
  public void testSaveAlternateDateFormat() throws Exception {

    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");
    List<CombinedSampleIntervalView> sampleIntervals = getAll();

    assertEquals(5, sampleIntervals.size());

    CombinedSampleIntervalView view = sampleIntervals.get(0);

    assertEquals("AQ-01", view.getCruise());
    assertEquals("AQ-01-01", view.getSample());
    assertEquals("202107", view.getBeginDate());
    assertEquals("202107", view.getEndDate());

    view = sampleIntervals.get(2);

    assertEquals("AQ-10", view.getCruise());
    assertEquals("AQ-001", view.getSample());
    assertEquals("2021", view.getBeginDate());
    assertEquals("2021", view.getEndDate());

  }

  @Test
  public void testSaveDuplicateIntervalNumbersWithinSample() throws Exception {

    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    String responseBody = uploadFileExpectingError("imlgs_sample_non_unique_intervals.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("Duplicate interval number detected", objectMapper.readTree(responseBody).get("formErrors").get("rows[4].intervalNumber").get(0).asText());
  }

  @Test
  public void testSaveConflictingIGSNsWithinSample() throws Exception {

    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    String responseBody = uploadFileExpectingError("imlgs_sample_conflicting_igsns.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("Conflicting IGSN value specified for sample", objectMapper.readTree(responseBody).get("formErrors").get("rows[4].igsn").get(0).asText());
  }

  @Test
  public void testSavePublishedIntervalUpdate() throws Exception {

    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity entity = curatorsSampleTsqpRepository.findAll().stream().filter(e -> e.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
          () -> new RuntimeException("Sample not found")
      );
      entity.getIntervals().forEach(i -> i.setPublish(true));
      curatorsSampleTsqpRepository.save(entity);
    });

    String responseBody = uploadFileExpectingError("imlgs_sample_good_full_yyyy.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("Cannot update published interval", objectMapper.readTree(responseBody).get("formErrors").get("rows[3].intervalNumber").get(0).asText());
    assertEquals("Cannot update published interval", objectMapper.readTree(responseBody).get("formErrors").get("rows[4].intervalNumber").get(0).asText());
  }

  @Test
  public void testNewIntervalToExistingSampleNullIntervalIGSN() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");

    String responseBody = uploadFileExpectingError("imlgs_sample_new_interval_null_igsn.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("Existing sample has an IGSN defined", objectMapper.readTree(responseBody).get("formErrors").get("rows[0].igsn").get(0).asText());
  }

  @Test
  public void testNewIntervalToExistingSampleWithSampleIGSN() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity entity = curatorsSampleTsqpRepository.findAll().stream().filter(e -> e.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
          () -> new RuntimeException("Sample not found")
      );
      entity.setIgsn(null);
      curatorsSampleTsqpRepository.save(entity);
    });

    String responseBody = uploadFileExpectingError("imlgs_sample_new_interval_null_sample_igsn.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("Existing sample does not have an IGSN defined", objectMapper.readTree(responseBody).get("formErrors").get("rows[0].igsn").get(0).asText());
  }

  @Test
  public void testNewIntervalToExistingSampleIGSNsNotEqual() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");

    String responseBody = uploadFileExpectingError("imlgs_sample_new_interval_igsn_not_equal.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("IGSN does not match existing sample IGSN", objectMapper.readTree(responseBody).get("formErrors").get("rows[0].igsn").get(0).asText());
  }

  @Test
  public void testCreateNewSampleWithExistingIGSN() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile("imlgs_sample_good_full_yyyy.xlsm");

    String responseBody = uploadFileExpectingError("imlgs_sample_new_sample_existing_igsn.xlsm");
    assertEquals("Invalid Request", objectMapper.readTree(responseBody).get("flashErrors").get(0).asText());
    assertEquals("A sample with this IGSN already exists", objectMapper.readTree(responseBody).get("formErrors").get("rows[0].igsn").get(0).asText());
  }

}