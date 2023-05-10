package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class IntervalServiceIT {

  private static MockWebServer mockCas;

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
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TransactionTemplate txTemplate;

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;

  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private IntervalService intervalService;

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

      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_ADMIN");
      role = geosamplesRoleRepository.save(role);
      for (GeosamplesAuthorityEntity authority : geosamplesAuthorityRepository.findAll()) {
        GeosamplesRoleAuthorityEntity roleAuthority = new GeosamplesRoleAuthorityEntity();
        roleAuthority.setAuthority(authority);
        role.addRoleAuthority(roleAuthority);
      }
      martin.setUserRole(role);
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
  public void testReviewInterval() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-001 not found")
          );

      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval 1 not found")
      );
      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);

      interval.setApproval(approval);
      interval = curatorsIntervalRepository.save(interval);

      ApprovalView approvalView = new ApprovalView();
      approvalView.setApprovalState(ApprovalState.APPROVED);
      approvalView.setComment("Looks good to me");

      IntervalView result = intervalService.updateApproval(approvalView, interval.getId());
      assertEquals(ApprovalState.APPROVED, result.getApprovalState());

      interval = curatorsIntervalRepository.findById(interval.getId()).orElseThrow(
          () -> new RuntimeException("Interval not found")
      );
      assertEquals(ApprovalState.APPROVED, interval.getApproval().getApprovalState());
      assertEquals("Looks good to me", interval.getApproval().getComment());
    });
  }

  @Test
  public void testReviewIntervalRevokeApproval() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-001 not found")
          );

      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval 1 not found")
      );
      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);

      interval.setApproval(approval);
      interval.setPublish(true);
      interval = curatorsIntervalRepository.save(interval);

      ApprovalView approvalView = new ApprovalView();
      approvalView.setApprovalState(ApprovalState.REJECTED);
      approvalView.setComment("There's a problem here");

      IntervalView result = intervalService.updateApproval(approvalView, interval.getId());
      assertEquals(ApprovalState.REJECTED, result.getApprovalState());

      interval = curatorsIntervalRepository.findById(interval.getId()).orElseThrow(
          () -> new RuntimeException("Interval not found")
      );
      assertEquals(ApprovalState.REJECTED, interval.getApproval().getApprovalState());
      assertEquals("There's a problem here", interval.getApproval().getComment());
      assertFalse(interval.isPublish());
    });
  }

  @Test
  public void testReviewIntervalChangeToPending() {
    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);

    ApiException exception = assertThrows(ApiException.class, () -> intervalService.updateApproval(approvalView, 1L));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFlashErrors().size());
    assertEquals(1, exception.getApiError().getFormErrors().size());
    assertEquals("approvalState", exception.getApiError().getFormErrors().keySet().stream().findFirst().orElse(null));
    assertEquals(1, exception.getApiError().getFormErrors().get("approvalState").size());
    assertEquals("Cannot update approval state to: PENDING", exception.getApiError().getFormErrors().get("approvalState").get(0));
  }

  @Test
  public void testUpdatePublishStatusIntervalNotApproved() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    IntervalView intervalView = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-003"))
          .findFirst()
          .orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      CuratorsIntervalEntity interval = sample.getIntervals().stream()
          .filter(i -> i.getInterval().equals(3))
          .findFirst()
          .orElseThrow(
              () -> new RuntimeException("Interval not found")
          );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      interval.setApproval(approval);
      interval = curatorsIntervalRepository.save(interval);

      IntervalView view = new IntervalView();
      view.setId(interval.getId());
      view.setPublish(true);
      view.setInterval(interval.getInterval());
      view.setDepthTop(0.);
      view.setDepthBot(0.);
      view.setDhCoreId(interval.getDhCoreId());
      view.setDhCoreLength(1.0);
      view.setDhCoreInterval(interval.getDhCoreInterval());
      view.setdTopInDhCore(1.);
      view.setdBotInDhCore(1.);
      view.setDescription(interval.getDescription());
      view.setAgeCodes(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view.setAbsoluteAgeTop(interval.getAbsoluteAgeTop());
      view.setAbsoluteAgeBot(interval.getAbsoluteAgeBot());
      view.setWeight(interval.getWeight());
      view.setExhausted(true);
      view.setPhotoLink(interval.getPhotoLink());
      view.setLake(interval.getLake());
      view.setUnitNumber(interval.getUnitNumber());
      view.setIntComments(interval.getIntComments());
      view.setDhDevice(interval.getDhDevice());
      view.setCdTop(1.0);
      view.setCdBot(1.0);
      view.setIgsn(interval.getIgsn());
      view.setImlgs(interval.getSample().getImlgs());
      return view;
    });
    assertNotNull(intervalView);

    ApiException exception = assertThrows(ApiException.class, () -> intervalService.update(intervalView, intervalView.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Interval %s (%s) is not approved", intervalView.getInterval(), intervalView.getImlgs()), exception.getApiError().getFlashErrors().get(0));
  }

  private void createCruise(String cruiseName, List<String> facilityCodes, List<String> platforms, List<String> legs) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(createJwt());

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

  private static String createJwt() throws IOException, JoseException {
    JwtClaims claims = new JwtClaims();
    claims.setIssuer("http://localhost:20158");
    claims.setSubject("martin");
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(claims.toJson());
    JsonWebKeySet jwks = new JsonWebKeySet(FileUtils.readFileToString(Paths.get("src/test/resources/jwks.json").toFile(), StandardCharsets.UTF_8));
    RsaJsonWebKey jwk = (RsaJsonWebKey) jwks.getJsonWebKeys().get(0);
    jws.setKey(jwk.getPrivateKey());
    jws.setKeyIdHeaderValue(jwk.getKeyId());
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    return jws.getCompactSerialization();
  }

  private void uploadFile() throws Exception {
    LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", new ClassPathResource("imlgs_sample_good_full.xlsm"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBearerAuth(createJwt());

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

}
