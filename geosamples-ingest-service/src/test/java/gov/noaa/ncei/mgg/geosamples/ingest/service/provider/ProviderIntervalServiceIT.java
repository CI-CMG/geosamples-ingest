package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruisePlatformRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsMunsellRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
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
import org.jetbrains.annotations.NotNull;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ProviderIntervalServiceIT {

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
  private TransactionTemplate transactionTemplate;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  private ProviderIntervalService providerIntervalService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;

  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  private CuratorsCruisePlatformRepository cruisePlatformRepository;

  @Autowired
  private CuratorsDeviceRepository curatorsDeviceRepository;

  @Autowired
  private CuratorsLithologyRepository curatorsLithologyRepository;

  @Autowired
  private CuratorsTextureRepository curatorsTextureRepository;

  @Autowired
  private CuratorsRockLithRepository curatorsRockLithRepository;

  @Autowired
  private CuratorsRockMinRepository curatorsRockMinRepository;

  @Autowired
  private CuratorsWeathMetaRepository curatorsWeathMetaRepository;

  @Autowired
  private CuratorsRemarkRepository curatorsRemarkRepository;

  @Autowired
  private CuratorsMunsellRepository curatorsMunsellRepository;

  @Autowired
  private CuratorsCruiseFacilityRepository curatorsCruiseFacilityRepository;

  @BeforeEach
  public void beforeEach() {
    cleanDB();
    transactionTemplate.executeWithoutResult(s -> {
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
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreateProviderIntervalNoImlgs() {
    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    IntervalView intervalView = new IntervalView();

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.create(intervalView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFlashErrors().size());
    assertEquals(1, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFormErrors().get("imlgs").size());
    assertEquals("Missing IMLGS", exception.getApiError().getFormErrors().get("imlgs").get(0));
  }

  @Test
  public void testCreateProviderInterval() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
          () -> new RuntimeException("GEOMAR facility not found")
        )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
        .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
        .findFirst().orElseThrow(
            () -> new RuntimeException("Sample AQ-01-01 not found")
        );

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setInterval(3);
    intervalView.setDepthTop(0.0);
    intervalView.setDepthBot(1.0);
    intervalView.setLithCode1("A");
    intervalView.setTextCode1("3");
    intervalView.setLithCode2("B");
    intervalView.setTextCode2("7");
    intervalView.setCompCode1("C");
    intervalView.setCompCode2("D");
    intervalView.setCompCode3("E");
    intervalView.setCompCode4("F");
    intervalView.setCompCode5("G");
    intervalView.setCompCode6("H");
    intervalView.setDescription("Test interval");
    intervalView.setAgeCodes(Collections.singletonList("00"));
    intervalView.setWeight(1.0);
    intervalView.setRockLithCode("6C");
    intervalView.setRockMinCode("A");
    intervalView.setWeathMetaCode("0");
    intervalView.setRemarkCode("0");
    intervalView.setMunsellCode("10R 6/1");
    intervalView.setExhausted(true);
    intervalView.setPhotoLink("http://example.com");
    intervalView.setIntComments("TST");
    intervalView.setIgsn("TST");
    intervalView.setImlgs(sample.getImlgs());

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderIntervalView result = providerIntervalService.create(intervalView, authentication);
    assertEquals(intervalView.getInterval(), result.getInterval());
    assertEquals(intervalView.getDepthTop(), result.getDepthTop());
    assertEquals(intervalView.getDepthBot(), result.getDepthBot());
    assertEquals(intervalView.getLithCode1(), result.getLithCode1());
    assertEquals(intervalView.getTextCode1(), result.getTextCode1());
    assertEquals(intervalView.getLithCode2(), result.getLithCode2());
    assertEquals(intervalView.getTextCode2(), result.getTextCode2());
    assertEquals(intervalView.getCompCode1(), result.getCompCode1());
    assertEquals(intervalView.getCompCode2(), result.getCompCode2());
    assertEquals(intervalView.getCompCode3(), result.getCompCode3());
    assertEquals(intervalView.getCompCode4(), result.getCompCode4());
    assertEquals(intervalView.getCompCode5(), result.getCompCode5());
    assertEquals(intervalView.getCompCode6(), result.getCompCode6());
    assertEquals(intervalView.getDescription(), result.getDescription());
    assertEquals(intervalView.getAgeCodes(), result.getAgeCodes());
    assertEquals(intervalView.getWeight(), result.getWeight());
    assertEquals(intervalView.getRockLithCode(), result.getRockLithCode());
    assertEquals(intervalView.getRockMinCode(), result.getRockMinCode());
    assertEquals(intervalView.getWeathMetaCode(), result.getWeathMetaCode());
    assertEquals(intervalView.getRemarkCode(), result.getRemarkCode());
    assertEquals(intervalView.getMunsellCode(), result.getMunsellCode());
    assertEquals(intervalView.getExhausted(), result.getExhausted());
    assertEquals(intervalView.getPhotoLink(), result.getPhotoLink());
    assertEquals(intervalView.getIntComments(), result.getIntComments());
    assertEquals(intervalView.getIgsn(), result.getIgsn());
    assertEquals(intervalView.getImlgs(), result.getImlgs());

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsIntervalEntity interval = curatorsIntervalRepository.getReferenceById(result.getId());
      assertEquals(ApprovalState.PENDING, interval.getApproval().getApprovalState());
      assertFalse(interval.isPublish());
    });
  }

  @Test
  public void testCreateProviderIntervalConflict() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
        .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
        .findFirst().orElseThrow(
            () -> new RuntimeException("Sample AQ-01-01 not found")
        );

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setInterval(3);
    intervalView.setDepthTop(0.0);
    intervalView.setDepthBot(1.0);
    intervalView.setLithCode1("A");
    intervalView.setTextCode1("3");
    intervalView.setLithCode2("B");
    intervalView.setTextCode2("7");
    intervalView.setCompCode1("C");
    intervalView.setCompCode2("D");
    intervalView.setCompCode3("E");
    intervalView.setCompCode4("F");
    intervalView.setCompCode5("G");
    intervalView.setCompCode6("H");
    intervalView.setDescription("Test interval");
    intervalView.setAgeCodes(Collections.singletonList("00"));
    intervalView.setWeight(1.0);
    intervalView.setRockLithCode("6C");
    intervalView.setRockMinCode("A");
    intervalView.setWeathMetaCode("0");
    intervalView.setRemarkCode("0");
    intervalView.setMunsellCode("10R 6/1");
    intervalView.setExhausted(true);
    intervalView.setPhotoLink("http://example.com");
    intervalView.setIntComments("TST");
    intervalView.setIgsn("TST");
    intervalView.setImlgs(sample.getImlgs());

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.create(intervalView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testCreateProviderIntervalProviderHasNoFacility() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setInterval(3);
    intervalView.setDepthTop(0.0);
    intervalView.setDepthBot(1.0);
    intervalView.setLithCode1("A");
    intervalView.setTextCode1("3");
    intervalView.setLithCode2("B");
    intervalView.setTextCode2("7");
    intervalView.setCompCode1("C");
    intervalView.setCompCode2("D");
    intervalView.setCompCode3("E");
    intervalView.setCompCode4("F");
    intervalView.setCompCode5("G");
    intervalView.setCompCode6("H");
    intervalView.setDescription("Test interval");
    intervalView.setAgeCodes(Collections.singletonList("00"));
    intervalView.setWeight(1.0);
    intervalView.setRockLithCode("6C");
    intervalView.setRockMinCode("A");
    intervalView.setWeathMetaCode("0");
    intervalView.setRemarkCode("0");
    intervalView.setMunsellCode("10R 6/1");
    intervalView.setExhausted(true);
    intervalView.setPhotoLink("http://example.com");
    intervalView.setIntComments("TST");
    intervalView.setIgsn("TST");
    intervalView.setImlgs("TEST");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.create(intervalView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("User %s has no assigned facility", userEntity.getUserName()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApproval() throws Exception {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
          () -> new RuntimeException("Facility not found")
        )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    createSamples();

    Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .findFirst().orElseThrow(() -> new RuntimeException("Sample not found"));
      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval not found")
        );
      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      approval.setComment("Looks good to me");
      interval.setApproval(approval);

      return curatorsIntervalRepository.save(interval).getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApprovalView approvalView = providerIntervalService.getApproval(intervalId, authentication);
    assertEquals(ApprovalState.APPROVED, approvalView.getApprovalState());
    assertEquals("Looks good to me", approvalView.getComment());
  }

  @Test
  public void testGetApprovalNotFound() throws Exception {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    createSamples();

    Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .findFirst().orElseThrow(() -> new RuntimeException("Sample not found"));
      return curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval not found")
      ).getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.getApproval(intervalId, authentication));
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Approval does not exist", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApprovalIntervalDoesNotBelongToUser() throws Exception {
    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    createSamples();

    Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .findFirst().orElseThrow(() -> new RuntimeException("Sample not found"));

      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      approval.setComment("Looks good to me");
      interval.setApproval(approval);

      return curatorsIntervalRepository.save(interval).getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.getApproval(intervalId, authentication));
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderInterval() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
          () -> new RuntimeException("Facility not found")
        )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst()
          .orElseThrow(
            () -> new RuntimeException("Sample not found")
          );
      CuratorsIntervalEntity interval = curatorsIntervalRepository.findBySampleAndInterval(sample, 1).orElseThrow(
          () -> new RuntimeException("Interval not found")
        );

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());
      ProviderIntervalView result = providerIntervalService.get(interval.getId(), authentication);
      assertEquals(interval.getId(), result.getId());
      assertEquals(interval.getInterval(), result.getInterval());
      assertEquals((double) interval.getDepthTop() + 0.5, result.getDepthTop());
      assertEquals((double) interval.getDepthBot() + 0.5, result.getDepthBot());
      assertEquals(interval.getLith1().getLithologyCode(), result.getLithCode1());
      assertEquals(interval.getText1().getTextureCode(), result.getTextCode1());
      assertEquals(interval.getLith2().getLithologyCode(), result.getLithCode2());
      assertEquals(interval.getText2().getTextureCode(), result.getTextCode2());
      assertEquals(interval.getComp1().getLithologyCode(), result.getCompCode1());
      assertEquals(interval.getComp2().getLithologyCode(), result.getCompCode2());
      assertEquals(interval.getComp3().getLithologyCode(), result.getCompCode3());
      assertEquals(interval.getComp4().getLithologyCode(), result.getCompCode4());
      assertEquals(interval.getComp5().getLithologyCode(), result.getCompCode5());
      assertEquals(interval.getComp6().getLithologyCode(), result.getCompCode6());
      assertEquals(interval.getDescription(), result.getDescription());
      assertEquals(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()), result.getAgeCodes());
      assertEquals(interval.getWeight(), result.getWeight());
      assertEquals(interval.getRockLith().getRockLithCode(), result.getRockLithCode());
      assertEquals(interval.getRockMin().getRockMinCode(), result.getRockMinCode());
      assertEquals(interval.getWeathMeta().getWeathMetaCode(), result.getWeathMetaCode());
      assertEquals(interval.getRemark().getRemarkCode(), result.getRemarkCode());
      assertEquals(interval.getMunsellCode(), result.getMunsellCode());
      assertEquals(interval.getExhaustCode().equals("X"), result.getExhausted());
      assertEquals(interval.getPhotoLink(), result.getPhotoLink());
      assertEquals(interval.getIntComments(), result.getIntComments());
      assertEquals(interval.getIgsn(), result.getIgsn());
      assertEquals(interval.getSample().getImlgs(), result.getImlgs());
    });
  }

  @Test
  public void testGetProviderIntervalNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.get(100L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderIntervalIntervalDoesNotBelongToProvider() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    Long intervalId = transactionTemplate.execute(s -> {
      CuratorsIntervalEntity interval = curatorsIntervalRepository.findAll().stream()
          .filter(i -> i.getSample().getSample().equals("AQ-01-01") && i.getInterval().equals(1))
          .findFirst()
          .orElseThrow(
              () -> new RuntimeException("Interval not found")
          );
      return interval.getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.get(intervalId, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchProviderIntervalsBelongingToProvider() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalSearchParameters searchParameters = new ProviderIntervalSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    PagedItemsView<IntervalView> result = providerIntervalService.search(searchParameters, authentication);

    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
    assertEquals(1, result.getTotalPages());
    assertEquals(5, result.getTotalItems());
    assertEquals(5, result.getItems().size());

    transactionTemplate.executeWithoutResult(s -> {
      List<ProviderIntervalView> items = result.getItems().stream().sorted(Comparator.comparing(ProviderIntervalView::getId)).collect(Collectors.toList());
      List<CuratorsIntervalEntity> entities = curatorsIntervalRepository.findAll().stream().sorted(Comparator.comparing(CuratorsIntervalEntity::getId)).collect(Collectors.toList());
      assertEquals(entities.size(), items.size());

      for (int i = 0; i < items.size(); i++) {
        CuratorsIntervalEntity interval = entities.get(i);
        ProviderIntervalView view = items.get(i);

        assertEquals(interval.getId(), view.getId());
        assertEquals(interval.getInterval(), view.getInterval());
        if (interval.getDepthTop() != null) {
          assertEquals(interval.getDepthTop(), (int) Math.floor(view.getDepthTop()));
        }
        if (interval.getDepthBot() != null) {
          assertEquals(interval.getDepthBot(), (int) Math.floor(view.getDepthBot()));
        }
        if (interval.getLith1() != null) {
          assertEquals(interval.getLith1().getLithologyCode(), view.getLithCode1());
        }
        if (interval.getText1() != null) {
          assertEquals(interval.getText1().getTextureCode(), view.getTextCode1());
        }
        if (interval.getLith2() != null) {
          assertEquals(interval.getLith2().getLithologyCode(), view.getLithCode2());
        }
        if (interval.getText2() != null) {
          assertEquals(interval.getText2().getTextureCode(), view.getTextCode2());
        }
        if (interval.getComp1() != null) {
          assertEquals(interval.getComp1().getLithologyCode(), view.getCompCode1());
        }
        if (interval.getComp2() != null) {
          assertEquals(interval.getComp2().getLithologyCode(), view.getCompCode2());
        }
        if (interval.getComp3() != null) {
          assertEquals(interval.getComp3().getLithologyCode(), view.getCompCode3());
        }
        if (interval.getComp4() != null) {
          assertEquals(interval.getComp4().getLithologyCode(), view.getCompCode4());
        }
        if (interval.getComp5() != null) {
          assertEquals(interval.getComp5().getLithologyCode(), view.getCompCode5());
        }
        if (interval.getComp6() != null) {
          assertEquals(interval.getComp6().getLithologyCode(), view.getCompCode6());
        }
        assertEquals(interval.getDescription(), view.getDescription());
        assertEquals(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()), view.getAgeCodes());
        assertEquals(interval.getWeight(), view.getWeight());
        if (interval.getRockLith() != null) {
          assertEquals(interval.getRockLith().getRockLithCode(), view.getRockLithCode());
        }
        if (interval.getRockMin() != null) {
          assertEquals(interval.getRockMin().getRockMinCode(), view.getRockMinCode());
        }
        if (interval.getWeathMeta() != null) {
          assertEquals(interval.getWeathMeta().getWeathMetaCode(), view.getWeathMetaCode());
        }
        if (interval.getRemark() != null) {
          assertEquals(interval.getRemark().getRemarkCode(), view.getRemarkCode());
        }
        assertEquals(interval.getMunsellCode(), view.getMunsellCode());
        if (interval.getExhaustCode() != null) {
          assertEquals(interval.getExhaustCode().equals("X"), view.getExhausted());
        }
        assertEquals(interval.getPhotoLink(), view.getPhotoLink());
        assertEquals(interval.getIntComments(), view.getIntComments());
        assertEquals(interval.getIgsn(), view.getIgsn());
        assertEquals(interval.getSample().getImlgs(), view.getImlgs());
      }
    });
  }

  @Test
  public void testSearchProviderIntervalsNoResult() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalSearchParameters searchParameters = new ProviderIntervalSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    PagedItemsView<IntervalView> result = providerIntervalService.search(searchParameters, authentication);
    assertEquals(0, result.getItems().size());
    assertEquals(0, result.getTotalPages());
    assertEquals(0, result.getTotalItems());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
  }

  @Test
  public void testUpdateProviderInterval() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    transactionTemplate.executeWithoutResult(s -> {
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
      
      final boolean originalPublish = interval.isPublish();

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      interval.setApproval(approval);
      interval = curatorsIntervalRepository.save(interval);

      ProviderIntervalView view = new IntervalView();
      view.setId(interval.getId());
      view.setInterval(interval.getInterval());
      view.setDepthTop(0.);
      view.setDepthBot(0.);
      view.setLithCode1(curatorsLithologyRepository.findByLithologyCode("A").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setLithCode2(curatorsLithologyRepository.findByLithologyCode("B").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setTextCode1(curatorsTextureRepository.findByTextureCode("3").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setTextCode2(curatorsTextureRepository.findByTextureCode("7").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setCompCode1(curatorsLithologyRepository.findByLithologyCode("X").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode2(curatorsLithologyRepository.findByLithologyCode("Y").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode3(curatorsLithologyRepository.findByLithologyCode("C").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode4(curatorsLithologyRepository.findByLithologyCode("D").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode5(curatorsLithologyRepository.findByLithologyCode("E").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode6(curatorsLithologyRepository.findByLithologyCode("F").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setDescription(interval.getDescription());
      view.setAgeCodes(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view.setWeight(interval.getWeight());
      view.setRockLithCode(curatorsRockLithRepository.findByRockLithCode("6C").orElseThrow(
          () -> new RuntimeException("Rock Lithology not found")
      ).getRockLithCode());
      view.setRockMinCode(curatorsRockMinRepository.findByRockMinCode("A").orElseThrow(
          () -> new RuntimeException("Rock Mineralogy not found")
      ).getRockMinCode());
      view.setWeathMetaCode(curatorsWeathMetaRepository.findByWeathMetaCode("0").orElseThrow(
          () -> new RuntimeException("Weathering Metamorphism not found")
      ).getWeathMetaCode());
      view.setRemarkCode(curatorsRemarkRepository.findByRemarkCode("0").orElseThrow(
          () -> new RuntimeException("Remark not found")
      ).getRemarkCode());
      view.setMunsellCode(curatorsMunsellRepository.findById("10R 6/1").orElseThrow(
          () -> new RuntimeException("Munsell not found")
      ).getMunsellCode());
      view.setExhausted(true);
      view.setPhotoLink(interval.getPhotoLink());
      view.setIntComments(interval.getIntComments());
      view.setIgsn(interval.getIgsn());
      view.setImlgs(interval.getSample().getImlgs());

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());
      ProviderIntervalView result = providerIntervalService.update(interval.getId(), view, authentication);

      assertEquals(view.getId(), result.getId());
      assertEquals(view.getInterval(), result.getInterval());
      assertEquals(view.getDepthTop(), result.getDepthTop());
      assertEquals(view.getDepthBot(), result.getDepthBot());
      assertEquals(view.getLithCode1(), result.getLithCode1());
      assertEquals(view.getLithCode2(), result.getLithCode2());
      assertEquals(view.getTextCode1(), result.getTextCode1());
      assertEquals(view.getTextCode2(), result.getTextCode2());
      assertEquals(view.getCompCode1(), result.getCompCode1());
      assertEquals(view.getCompCode2(), result.getCompCode2());
      assertEquals(view.getCompCode3(), result.getCompCode3());
      assertEquals(view.getCompCode4(), result.getCompCode4());
      assertEquals(view.getCompCode5(), result.getCompCode5());
      assertEquals(view.getCompCode6(), result.getCompCode6());
      assertEquals(view.getDescription(), result.getDescription());
      assertEquals(view.getAgeCodes(), result.getAgeCodes());
      assertEquals(view.getWeight(), result.getWeight());
      assertEquals(view.getRockLithCode(), result.getRockLithCode());
      assertEquals(view.getRockMinCode(), result.getRockMinCode());
      assertEquals(view.getWeathMetaCode(), result.getWeathMetaCode());
      assertEquals(view.getRemarkCode(), result.getRemarkCode());
      assertEquals(view.getMunsellCode(), result.getMunsellCode());
      assertEquals(view.getExhausted(), result.getExhausted());
      assertEquals(view.getPhotoLink(), result.getPhotoLink());
      assertEquals(view.getIntComments(), result.getIntComments());
      assertEquals(view.getIgsn(), result.getIgsn());
      assertEquals(view.getImlgs(), result.getImlgs());

      interval = curatorsIntervalRepository.findById(interval.getId()).orElseThrow(
          () -> new RuntimeException("Interval not found")
      );

      assertEquals(result.getId(), interval.getId());
      assertEquals(result.getInterval(), interval.getInterval());
      assertEquals((int) Math.floor(result.getDepthTop()), interval.getDepthTop());
      assertEquals((int) Math.floor(result.getDepthBot()), interval.getDepthBot());
      assertEquals(result.getLithCode1(), interval.getLith1().getLithologyCode());
      assertEquals(result.getLithCode2(), interval.getLith2().getLithologyCode());
      assertEquals(result.getTextCode1(), interval.getText1().getTextureCode());
      assertEquals(result.getTextCode2(), interval.getText2().getTextureCode());
      assertEquals(result.getCompCode1(), interval.getComp1().getLithologyCode());
      assertEquals(result.getCompCode2(), interval.getComp2().getLithologyCode());
      assertEquals(result.getCompCode3(), interval.getComp3().getLithologyCode());
      assertEquals(result.getCompCode4(), interval.getComp4().getLithologyCode());
      assertEquals(result.getCompCode5(), interval.getComp5().getLithologyCode());
      assertEquals(result.getCompCode6(), interval.getComp6().getLithologyCode());
      assertEquals(result.getDescription(), interval.getDescription());
      assertEquals(result.getAgeCodes(), interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      assertEquals(result.getWeight(), interval.getWeight());
      assertEquals(result.getRockLithCode(), interval.getRockLith().getRockLithCode());
      assertEquals(result.getRockMinCode(), interval.getRockMin().getRockMinCode());
      assertEquals(result.getWeathMetaCode(), interval.getWeathMeta().getWeathMetaCode());
      assertEquals(result.getRemarkCode(), interval.getRemark().getRemarkCode());
      assertEquals(result.getMunsellCode(), interval.getMunsellCode());
      assertEquals(result.getExhausted(), interval.getExhaustCode().equals("X"));
      assertEquals(result.getPhotoLink(), interval.getPhotoLink());
      assertEquals(result.getIntComments(), interval.getIntComments());
      assertEquals(result.getIgsn(), interval.getIgsn());
      assertEquals(result.getImlgs(), interval.getSample().getImlgs());
      assertEquals(originalPublish, interval.isPublish());
      assertEquals(ApprovalState.PENDING, interval.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderIntervalFromRejected() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    transactionTemplate.executeWithoutResult(s -> {
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

      final boolean originalPublish = interval.isPublish();

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.REJECTED);
      interval.setApproval(approval);
      interval = curatorsIntervalRepository.save(interval);

      ProviderIntervalView view = new IntervalView();
      view.setId(interval.getId());
      view.setInterval(interval.getInterval());
      view.setDepthTop(0.);
      view.setDepthBot(0.);
      view.setLithCode1(curatorsLithologyRepository.findByLithologyCode("A").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setLithCode2(curatorsLithologyRepository.findByLithologyCode("B").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setTextCode1(curatorsTextureRepository.findByTextureCode("3").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setTextCode2(curatorsTextureRepository.findByTextureCode("7").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setCompCode1(curatorsLithologyRepository.findByLithologyCode("X").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode2(curatorsLithologyRepository.findByLithologyCode("Y").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode3(curatorsLithologyRepository.findByLithologyCode("C").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode4(curatorsLithologyRepository.findByLithologyCode("D").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode5(curatorsLithologyRepository.findByLithologyCode("E").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode6(curatorsLithologyRepository.findByLithologyCode("F").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setDescription(interval.getDescription());
      view.setAgeCodes(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view.setWeight(interval.getWeight());
      view.setRockLithCode(curatorsRockLithRepository.findByRockLithCode("6C").orElseThrow(
          () -> new RuntimeException("Rock Lithology not found")
      ).getRockLithCode());
      view.setRockMinCode(curatorsRockMinRepository.findByRockMinCode("A").orElseThrow(
          () -> new RuntimeException("Rock Mineralogy not found")
      ).getRockMinCode());
      view.setWeathMetaCode(curatorsWeathMetaRepository.findByWeathMetaCode("0").orElseThrow(
          () -> new RuntimeException("Weathering Metamorphism not found")
      ).getWeathMetaCode());
      view.setRemarkCode(curatorsRemarkRepository.findByRemarkCode("0").orElseThrow(
          () -> new RuntimeException("Remark not found")
      ).getRemarkCode());
      view.setMunsellCode(curatorsMunsellRepository.findById("10R 6/1").orElseThrow(
          () -> new RuntimeException("Munsell not found")
      ).getMunsellCode());
      view.setExhausted(true);
      view.setPhotoLink(interval.getPhotoLink());
      view.setIntComments(interval.getIntComments());
      view.setIgsn(interval.getIgsn());
      view.setImlgs(interval.getSample().getImlgs());

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());
      ProviderIntervalView result = providerIntervalService.update(interval.getId(), view, authentication);

      assertEquals(view.getId(), result.getId());
      assertEquals(view.getInterval(), result.getInterval());
      assertEquals(view.getDepthTop(), result.getDepthTop());
      assertEquals(view.getDepthBot(), result.getDepthBot());
      assertEquals(view.getLithCode1(), result.getLithCode1());
      assertEquals(view.getLithCode2(), result.getLithCode2());
      assertEquals(view.getTextCode1(), result.getTextCode1());
      assertEquals(view.getTextCode2(), result.getTextCode2());
      assertEquals(view.getCompCode1(), result.getCompCode1());
      assertEquals(view.getCompCode2(), result.getCompCode2());
      assertEquals(view.getCompCode3(), result.getCompCode3());
      assertEquals(view.getCompCode4(), result.getCompCode4());
      assertEquals(view.getCompCode5(), result.getCompCode5());
      assertEquals(view.getCompCode6(), result.getCompCode6());
      assertEquals(view.getDescription(), result.getDescription());
      assertEquals(view.getAgeCodes(), result.getAgeCodes());
      assertEquals(view.getWeight(), result.getWeight());
      assertEquals(view.getRockLithCode(), result.getRockLithCode());
      assertEquals(view.getRockMinCode(), result.getRockMinCode());
      assertEquals(view.getWeathMetaCode(), result.getWeathMetaCode());
      assertEquals(view.getRemarkCode(), result.getRemarkCode());
      assertEquals(view.getMunsellCode(), result.getMunsellCode());
      assertEquals(view.getExhausted(), result.getExhausted());
      assertEquals(view.getPhotoLink(), result.getPhotoLink());
      assertEquals(view.getIntComments(), result.getIntComments());
      assertEquals(view.getIgsn(), result.getIgsn());
      assertEquals(view.getImlgs(), result.getImlgs());

      interval = curatorsIntervalRepository.findById(interval.getId()).orElseThrow(
          () -> new RuntimeException("Interval not found")
      );

      assertEquals(result.getId(), interval.getId());
      assertEquals(result.getInterval(), interval.getInterval());
      assertEquals((int) Math.floor(result.getDepthTop()), interval.getDepthTop());
      assertEquals((int) Math.floor(result.getDepthBot()), interval.getDepthBot());
      assertEquals(result.getLithCode1(), interval.getLith1().getLithologyCode());
      assertEquals(result.getLithCode2(), interval.getLith2().getLithologyCode());
      assertEquals(result.getTextCode1(), interval.getText1().getTextureCode());
      assertEquals(result.getTextCode2(), interval.getText2().getTextureCode());
      assertEquals(result.getCompCode1(), interval.getComp1().getLithologyCode());
      assertEquals(result.getCompCode2(), interval.getComp2().getLithologyCode());
      assertEquals(result.getCompCode3(), interval.getComp3().getLithologyCode());
      assertEquals(result.getCompCode4(), interval.getComp4().getLithologyCode());
      assertEquals(result.getCompCode5(), interval.getComp5().getLithologyCode());
      assertEquals(result.getCompCode6(), interval.getComp6().getLithologyCode());
      assertEquals(result.getDescription(), interval.getDescription());
      assertEquals(result.getAgeCodes(), interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      assertEquals(result.getWeight(), interval.getWeight());
      assertEquals(result.getRockLithCode(), interval.getRockLith().getRockLithCode());
      assertEquals(result.getRockMinCode(), interval.getRockMin().getRockMinCode());
      assertEquals(result.getWeathMetaCode(), interval.getWeathMeta().getWeathMetaCode());
      assertEquals(result.getRemarkCode(), interval.getRemark().getRemarkCode());
      assertEquals(result.getMunsellCode(), interval.getMunsellCode());
      assertEquals(result.getExhausted(), interval.getExhaustCode().equals("X"));
      assertEquals(result.getPhotoLink(), interval.getPhotoLink());
      assertEquals(result.getIntComments(), interval.getIntComments());
      assertEquals(result.getIgsn(), interval.getIgsn());
      assertEquals(result.getImlgs(), interval.getSample().getImlgs());
      assertEquals(originalPublish, interval.isPublish());
      assertEquals(ApprovalState.PENDING, interval.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderIntervalIntervalApproved() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalView intervalView = transactionTemplate.execute(s -> {
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
      approval.setApprovalState(ApprovalState.APPROVED);
      interval.setApproval(approval);
      interval = curatorsIntervalRepository.save(interval);

      ProviderIntervalView view = new ProviderIntervalView();
      view.setId(interval.getId());
      view.setInterval(interval.getInterval());
      view.setDepthTop(0.);
      view.setDepthBot(0.);
      view.setLithCode1(curatorsLithologyRepository.findByLithologyCode("A").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setLithCode2(curatorsLithologyRepository.findByLithologyCode("B").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setTextCode1(curatorsTextureRepository.findByTextureCode("3").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setTextCode2(curatorsTextureRepository.findByTextureCode("7").orElseThrow(
          () -> new RuntimeException("Texture not found")
      ).getTextureCode());
      view.setCompCode1(curatorsLithologyRepository.findByLithologyCode("X").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode2(curatorsLithologyRepository.findByLithologyCode("Y").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode3(curatorsLithologyRepository.findByLithologyCode("C").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode4(curatorsLithologyRepository.findByLithologyCode("D").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode5(curatorsLithologyRepository.findByLithologyCode("E").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setCompCode6(curatorsLithologyRepository.findByLithologyCode("F").orElseThrow(
          () -> new RuntimeException("Lithology not found")
      ).getLithologyCode());
      view.setDescription(interval.getDescription());
      view.setAgeCodes(interval.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view.setWeight(interval.getWeight());
      view.setRockLithCode(curatorsRockLithRepository.findByRockLithCode("6C").orElseThrow(
          () -> new RuntimeException("Rock Lithology not found")
      ).getRockLithCode());
      view.setRockMinCode(curatorsRockMinRepository.findByRockMinCode("A").orElseThrow(
          () -> new RuntimeException("Rock Mineralogy not found")
      ).getRockMinCode());
      view.setWeathMetaCode(curatorsWeathMetaRepository.findByWeathMetaCode("0").orElseThrow(
          () -> new RuntimeException("Weathering Metamorphism not found")
      ).getWeathMetaCode());
      view.setRemarkCode(curatorsRemarkRepository.findByRemarkCode("0").orElseThrow(
          () -> new RuntimeException("Remark not found")
      ).getRemarkCode());
      view.setMunsellCode(curatorsMunsellRepository.findById("10R 6/1").orElseThrow(
          () -> new RuntimeException("Munsell not found")
      ).getMunsellCode());
      view.setExhausted(true);
      view.setPhotoLink(interval.getPhotoLink());
      view.setIntComments(interval.getIntComments());
      view.setIgsn(interval.getIgsn());
      view.setImlgs(interval.getSample().getImlgs());

      return view;
    });
    assertNotNull(intervalView);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.update(intervalView.getId(), intervalView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Cannot edit approved interval: %s (%s)", intervalView.getInterval(), intervalView.getImlgs()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderIntervalNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setImlgs("IMLGS");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.update(100L, intervalView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderIntervalIntervalDoesNotBelongToProvider() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setImlgs("IMLGS");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-003"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      return sample.getIntervals().get(0).getId();
    });
    assertNotNull(intervalId);

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.update(intervalId, intervalView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderIntervalConflict() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    final String imlgs = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-003"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      CuratorsCruiseFacilityEntity cruiseFacility = new CuratorsCruiseFacilityEntity();
      cruiseFacility.setCruise(sample.getCruise());
      cruiseFacility.setFacility(facility);
      cruiseFacility = curatorsCruiseFacilityRepository.save(cruiseFacility);
      sample.setCruiseFacility(cruiseFacility);

      sample = curatorsSampleTsqpRepository.save(sample);
      return sample.getImlgs();
    });
    assertNotNull(imlgs);

    final Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-002"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      CuratorsIntervalEntity interval = sample.getIntervals().get(0);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      interval.setApproval(approval);

      interval = curatorsIntervalRepository.save(interval);

      return interval.getId();
    });
    assertNotNull(intervalId);

    ProviderIntervalView intervalView = new ProviderIntervalView();
    intervalView.setId(intervalId);
    intervalView.setImlgs(imlgs);
    intervalView.setInterval(2);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.update(intervalId, intervalView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderInterval() throws Exception {
    createSamples();

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    final Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-002"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      assertEquals(1, sample.getIntervals().size());
      CuratorsIntervalEntity interval = sample.getIntervals().get(0);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      interval.setApproval(approval);

      interval = curatorsIntervalRepository.save(interval);

      return interval.getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    providerIntervalService.delete(intervalId, authentication);
    assertFalse(curatorsIntervalRepository.existsById(intervalId));
  }

  @Test
  public void testDeleteProviderIntervalNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(curatorsFacilityRepository.findByFacilityCode("GEOMAR").orElseThrow(
              () -> new RuntimeException("Facility not found")
          )
      );
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.delete(999999L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderIntervalIntervalDoesNotBelongToProvider() throws Exception {
    createSamples();

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    final Long intervalId = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-002"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample not found")
          );

      return sample.getIntervals().get(0).getId();
    });
    assertNotNull(intervalId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerIntervalService.delete(intervalId, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
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

  private void createSamples() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(status -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      cruisePlatformRepository.deleteAll();
      curatorsDeviceRepository.findByDeviceCode("TS").ifPresent(curatorsDeviceRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST").ifPresent(platformMasterRepository::delete);
      geosamplesUserRepository.findById("gabby").ifPresent(geosamplesUserRepository::delete);
      geosamplesUserRepository.findById("martin").ifPresent(geosamplesUserRepository::delete);
      curatorsFacilityRepository.findByFacilityCode("TST").ifPresent(curatorsFacilityRepository::delete);
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
    });
  }

}
