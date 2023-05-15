package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderSampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruisePlatformRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
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
import java.util.List;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ProviderSampleServiceIT {

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
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;

  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

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

  @Autowired
  private ProviderSampleService providerSampleService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private CuratorsDeviceRepository curatorsDeviceRepository;

  @Autowired
  private CuratorsCruisePlatformRepository cruisePlatformRepository;

  @Test
  public void testCreateProviderSample() {
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

    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    CuratorsCruiseEntity cruiseEntity = transactionTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      CuratorsLegEntity leg = new CuratorsLegEntity();
      leg.setLegName("TST");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    CuratorsDeviceEntity deviceEntity = transactionTemplate.execute(s -> {
      CuratorsDeviceEntity device = new CuratorsDeviceEntity();
      device.setDevice("TST");
      device.setDeviceCode("TS");
      return curatorsDeviceRepository.save(device);
    });
    assertNotNull(deviceEntity);

    ProviderSampleView view = new ProviderSampleView();
    view.setCruise(cruiseEntity.getCruiseName());
    view.setSample("TST0001");
    view.setPlatform(platformEntity.getPlatform());
    view.setDeviceCode(deviceEntity.getDeviceCode());
    view.setBeginDate("20200101");
    view.setEndDate("20200101");
    view.setLat(1.);
    view.setEndLat(1.);
    view.setLon(1.);
    view.setEndLon(1.);
    view.setLatLonOrig("TST");
    view.setWaterDepth(1);
    view.setEndWaterDepth(1);
    view.setStorageMethCode("A");
    view.setCoredLength(1.);
    view.setCoredDiam(1.);
    view.setPi("TST");
    view.setProvinceCode("01");
    view.setLake("TST");
    view.setOtherLink("TST");
    view.setIgsn("TST");
    view.setLeg("TST");
    view.setSampleComments("TST");
    view.setShowSampl("TST");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ProviderSampleView created = providerSampleService.create(view, authentication);
    assertNotNull(created.getImlgs());
    assertEquals(view.getCruise(), created.getCruise());
    assertEquals(view.getSample(), created.getSample());
    assertEquals(view.getPlatform(), created.getPlatform());
    assertEquals(view.getDeviceCode(), created.getDeviceCode());
    assertEquals(view.getBeginDate(), created.getBeginDate());
    assertEquals(view.getEndDate(), created.getEndDate());
    assertEquals(view.getLat(), created.getLat());
    assertEquals(view.getEndLat(), created.getEndLat());
    assertEquals(view.getLon(), created.getLon());
    assertEquals(view.getEndLon(), created.getEndLon());
    assertEquals("D", created.getLatLonOrig());
    assertEquals(view.getWaterDepth(), created.getWaterDepth());
    assertEquals(view.getEndWaterDepth(), created.getEndWaterDepth());
    assertEquals(view.getStorageMethCode(), created.getStorageMethCode());
    assertEquals(view.getCoredLength(), created.getCoredLength());
    assertEquals(view.getCoredDiam(), created.getCoredDiam());
    assertEquals(view.getPi(), created.getPi());
    assertEquals(view.getProvinceCode(), created.getProvinceCode());
    assertEquals(view.getLake(), created.getLake());
    assertEquals(view.getOtherLink(), created.getOtherLink());
    assertEquals(view.getIgsn(), created.getIgsn());
    assertEquals(view.getLeg(), created.getLeg());
    assertEquals(view.getSampleComments(), created.getSampleComments());
    assertEquals(view.getShowSampl(), created.getShowSampl());

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findByImlgs(created.getImlgs()).orElseThrow(
          () -> new RuntimeException("Sample not found")
      );
      assertEquals(sample.getImlgs(), created.getImlgs());
      assertEquals(sample.getCruise().getCruiseName(), created.getCruise());
      assertEquals(sample.getSample(), created.getSample());
      assertEquals(sample.getCruisePlatform().getPlatform().getPlatform(), created.getPlatform());
      assertEquals(sample.getDevice().getDeviceCode(), created.getDeviceCode());
      assertEquals(sample.getBeginDate(), created.getBeginDate());
      assertEquals(sample.getEndDate(), created.getEndDate());
      assertEquals(sample.getLat(), created.getLat());
      assertEquals(sample.getEndLat(), created.getEndLat());
      assertEquals(sample.getLon(), created.getLon());
      assertEquals(sample.getEndLon(), created.getEndLon());
      assertEquals(sample.getLatLonOrig(), "D");
      assertEquals(sample.getWaterDepth(), created.getWaterDepth());
      assertEquals(sample.getEndWaterDepth(), created.getEndWaterDepth());
      assertEquals(sample.getStorageMeth().getStorageMethCode(), created.getStorageMethCode());
      assertEquals((int) sample.getCoredLength(), created.getCoredLength());
      assertEquals((int) sample.getCoredDiam(), created.getCoredDiam());
      assertEquals(sample.getPi(), created.getPi());
      assertEquals(sample.getProvince().getProvinceCode(), created.getProvinceCode());
      assertEquals(sample.getLake(), created.getLake());
      assertEquals(sample.getOtherLink(), created.getOtherLink());
      assertEquals(sample.getIgsn(), created.getIgsn());
      assertEquals(sample.getLeg().getLegName(), created.getLeg());
      assertEquals(sample.getSampleComments(), created.getSampleComments());
      assertEquals(sample.getShowSampl(), created.getShowSampl());
      assertEquals(ApprovalState.PENDING, sample.getApproval().getApprovalState());
      assertFalse(sample.isPublish());
    });
  }

  @Test
  public void testCreateProviderSampleConflict() {
    CuratorsFacilityEntity facility1 = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility1);

    CuratorsFacilityEntity facility2 = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TS2");
      f.setFacility("Test Facility 2");
      f.setInstCode("TS2");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility2);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility1);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    CuratorsCruiseEntity cruiseEntity = transactionTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility2);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      CuratorsLegEntity leg = new CuratorsLegEntity();
      leg.setLegName("TST");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      cruise = curatorsCruiseRepository.save(cruise);
      return cruise;
    });
    assertNotNull(cruiseEntity);

    CuratorsDeviceEntity deviceEntity = transactionTemplate.execute(s -> {
      CuratorsDeviceEntity device = new CuratorsDeviceEntity();
      device.setDevice("TST");
      device.setDeviceCode("TS");
      return curatorsDeviceRepository.save(device);
    });
    assertNotNull(deviceEntity);

    ProviderSampleView view = new ProviderSampleView();
    view.setCruise(cruiseEntity.getCruiseName());
    view.setSample("TST0001");
    view.setPlatform(platformEntity.getPlatform());
    view.setDeviceCode(deviceEntity.getDeviceCode());
    view.setBeginDate("20200101");
    view.setEndDate("20200101");
    view.setLat(1.);
    view.setEndLat(1.);
    view.setLon(1.);
    view.setEndLon(1.);
    view.setLatLonOrig("TST");
    view.setWaterDepth(1);
    view.setEndWaterDepth(1);
    view.setStorageMethCode("A");
    view.setCoredLength(1.);
    view.setCoredDiam(1.);
    view.setPi("TST");
    view.setProvinceCode("01");
    view.setLake("TST");
    view.setOtherLink("TST");
    view.setIgsn("TST");
    view.setLeg("TST");
    view.setSampleComments("TST");
    view.setShowSampl("TST");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.create(view, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Unable to find cruise: %s", view.getCruise()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderSampleUserHasNoFacility() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      return geosamplesUserRepository.save(user);
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.get("TST", authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("User %s has no assigned facility", userEntity.getUserName()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderSample() throws Exception {
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

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(samp -> samp.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      CuratorsCruiseFacilityEntity cruiseFacility = new CuratorsCruiseFacilityEntity();
      cruiseFacility.setCruise(sample.getCruise());
      cruiseFacility.setFacility(facility);
      cruiseFacility = curatorsCruiseFacilityRepository.save(cruiseFacility);
      sample.setCruiseFacility(cruiseFacility);
      sample = curatorsSampleTsqpRepository.save(sample);

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());

      ProviderSampleView result = providerSampleService.get(sample.getImlgs(), authentication);
      assertNotNull(result);
      assertEquals(sample.getImlgs(), result.getImlgs());
      assertEquals(sample.getCruise().getCruiseName(), result.getCruise());
      assertEquals(sample.getSample(), result.getSample());
      assertEquals(sample.getCruisePlatform().getPlatform().getPlatform(), result.getPlatform());
      assertEquals(sample.getDevice().getDeviceCode(), result.getDeviceCode());
      assertEquals(sample.getBeginDate(), result.getBeginDate());
      assertEquals(sample.getEndDate(), result.getEndDate());
      assertEquals(sample.getLat(), result.getLat());
      assertEquals(sample.getEndLat(), result.getEndLat());
      assertEquals(sample.getLon(), result.getLon());
      assertEquals(sample.getEndLon(), result.getEndLon());
      assertEquals(sample.getLatLonOrig(), result.getLatLonOrig());
      assertEquals(sample.getWaterDepth(), result.getWaterDepth());
      assertEquals(sample.getEndWaterDepth(), result.getEndWaterDepth());
      assertEquals(sample.getStorageMeth().getStorageMethCode(), result.getStorageMethCode());
      assertEquals(sample.getCoredLength(), result.getCoredLength().intValue());
      assertEquals(sample.getCoredDiam(), result.getCoredDiam().intValue());
      assertEquals(sample.getPi(), result.getPi());
      if (sample.getProvince() != null) {
        assertEquals(sample.getProvince().getProvinceCode(), result.getProvinceCode());
      }
      assertEquals(sample.getLake(), result.getLake());
      assertEquals(sample.getOtherLink(), result.getOtherLink());
      assertEquals(sample.getIgsn(), result.getIgsn());
      assertEquals(sample.getSampleComments(), result.getSampleComments());
      assertEquals(sample.getShowSampl(), result.getShowSampl());
    });
  }

  @Test
  public void testGetProviderSampleNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");

      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TST");
      facility.setFacility("Test Facility");
      facility.setInstCode("TST");
      facility.setLastUpdate(Instant.now());
      facility = curatorsFacilityRepository.save(facility);
      user.setFacility(facility);

      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.get("TEST", authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderSampleSampleDoesNotBelongToProvider() throws Exception {
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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
        .filter(samp -> samp.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
            () -> new RuntimeException("Sample AQ-01-01 not found")
        );

    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.get(sample.getImlgs(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchProviderSamplesWithinUserFacility() throws Exception {
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

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample1 = curatorsSampleTsqpRepository.findAll().stream()
          .filter(samp -> samp.getSample().equals("AQ-002")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );
      CuratorsCruiseFacilityEntity cruiseFacility = new CuratorsCruiseFacilityEntity();
      cruiseFacility.setCruise(sample1.getCruise());
      cruiseFacility.setFacility(facility);
      cruiseFacility = curatorsCruiseFacilityRepository.save(cruiseFacility);
      sample1.setCruiseFacility(cruiseFacility);
      sample1 = curatorsSampleTsqpRepository.save(sample1);

      CuratorsSampleTsqpEntity sample2 = curatorsSampleTsqpRepository.findAll().stream()
          .filter(samp -> samp.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );
      cruiseFacility = new CuratorsCruiseFacilityEntity();
      cruiseFacility.setCruise(sample2.getCruise());
      cruiseFacility.setFacility(facility);
      cruiseFacility = curatorsCruiseFacilityRepository.save(cruiseFacility);
      sample2.setCruiseFacility(cruiseFacility);
      sample2 = curatorsSampleTsqpRepository.save(sample2);

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());

      ProviderSampleSearchParameters searchParameters = new ProviderSampleSearchParameters();
      searchParameters.setPage(1);
      searchParameters.setItemsPerPage(10);

      PagedItemsView<SampleView> result = providerSampleService.search(searchParameters, authentication);
      assertNotNull(result);
      assertEquals(2, result.getItems().size());
      assertEquals(2, result.getTotalItems());
      assertEquals(1, result.getTotalPages());
      assertEquals(1, result.getPage());
      assertEquals(10, result.getItemsPerPage());

      assertEquals(sample1.getImlgs(), result.getItems().get(0).getImlgs());
      assertEquals(sample1.getCruise().getCruiseName(), result.getItems().get(0).getCruise());
      assertEquals(sample1.getSample(), result.getItems().get(0).getSample());
      assertEquals(sample1.getCruisePlatform().getPlatform().getPlatform(), result.getItems().get(0).getPlatform());
      assertEquals(sample1.getDevice().getDeviceCode(), result.getItems().get(0).getDeviceCode());
      assertEquals(sample1.getBeginDate(), result.getItems().get(0).getBeginDate());
      assertEquals(sample1.getEndDate(), result.getItems().get(0).getEndDate());
      assertEquals(sample1.getLat(), result.getItems().get(0).getLat());
      assertEquals(sample1.getEndLat(), result.getItems().get(0).getEndLat());
      assertEquals(sample1.getLon(), result.getItems().get(0).getLon());
      assertEquals(sample1.getEndLon(), result.getItems().get(0).getEndLon());
      assertEquals(sample1.getLatLonOrig(), result.getItems().get(0).getLatLonOrig());
      assertEquals(sample1.getWaterDepth(), result.getItems().get(0).getWaterDepth());
      assertEquals(sample1.getEndWaterDepth(), result.getItems().get(0).getEndWaterDepth());
      assertEquals(sample1.getStorageMeth().getStorageMethCode(), result.getItems().get(0).getStorageMethCode());
      assertEquals(sample1.getCoredLength(), result.getItems().get(0).getCoredLength().intValue());
      assertEquals(sample1.getCoredDiam(), result.getItems().get(0).getCoredDiam().intValue());
      assertEquals(sample1.getPi(), result.getItems().get(0).getPi());
      if (sample1.getProvince() != null) {
        assertEquals(sample1.getProvince().getProvinceCode(), result.getItems().get(0).getProvinceCode());
      }
      assertEquals(sample1.getLake(), result.getItems().get(0).getLake());
      assertEquals(sample1.getOtherLink(), result.getItems().get(0).getOtherLink());
      assertEquals(sample1.getIgsn(), result.getItems().get(0).getIgsn());
      assertEquals(sample1.getSampleComments(), result.getItems().get(0).getSampleComments());
      assertEquals(sample1.getShowSampl(), result.getItems().get(0).getShowSampl());

      assertEquals(sample2.getImlgs(), result.getItems().get(1).getImlgs());
      assertEquals(sample2.getCruise().getCruiseName(), result.getItems().get(1).getCruise());
      assertEquals(sample2.getSample(), result.getItems().get(1).getSample());
      assertEquals(sample2.getCruisePlatform().getPlatform().getPlatform(), result.getItems().get(1).getPlatform());
      assertEquals(sample2.getDevice().getDeviceCode(), result.getItems().get(1).getDeviceCode());
      assertEquals(sample2.getBeginDate(), result.getItems().get(1).getBeginDate());
      assertEquals(sample2.getEndDate(), result.getItems().get(1).getEndDate());
      assertEquals(sample2.getLat(), result.getItems().get(1).getLat());
      assertEquals(sample2.getEndLat(), result.getItems().get(1).getEndLat());
      assertEquals(sample2.getLon(), result.getItems().get(1).getLon());
      assertEquals(sample2.getEndLon(), result.getItems().get(1).getEndLon());
      assertEquals(sample2.getLatLonOrig(), result.getItems().get(1).getLatLonOrig());
      assertEquals(sample2.getWaterDepth(), result.getItems().get(1).getWaterDepth());
      assertEquals(sample2.getEndWaterDepth(), result.getItems().get(1).getEndWaterDepth());
      assertEquals(sample2.getStorageMeth().getStorageMethCode(), result.getItems().get(1).getStorageMethCode());
      assertEquals(sample2.getCoredLength(), result.getItems().get(1).getCoredLength().intValue());
      assertEquals(sample2.getCoredDiam(), result.getItems().get(1).getCoredDiam().intValue());
      assertEquals(sample2.getPi(), result.getItems().get(1).getPi());
      if (sample2.getProvince() != null) {
        assertEquals(sample2.getProvince().getProvinceCode(), result.getItems().get(1).getProvinceCode());
      }
      assertEquals(sample2.getLake(), result.getItems().get(1).getLake());
      assertEquals(sample2.getOtherLink(), result.getItems().get(1).getOtherLink());
      assertEquals(sample2.getIgsn(), result.getItems().get(1).getIgsn());
      assertEquals(sample2.getSampleComments(), result.getItems().get(1).getSampleComments());
      assertEquals(sample2.getShowSampl(), result.getItems().get(1).getShowSampl());
    });
  }

  @Test
  public void testSearchProviderSamplesWithinUserFacilityNoResults() throws Exception {
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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderSampleSearchParameters searchParameters = new ProviderSampleSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    PagedItemsView<SampleView> result = providerSampleService.search(searchParameters, authentication);
    assertNotNull(result);
    assertEquals(0, result.getItems().size());
    assertEquals(0, result.getTotalItems());
    assertEquals(0, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
  }

  @Test
  public void testUpdateProviderSample() throws Exception {
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
      CuratorsSampleTsqpEntity sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      final boolean originalPublish = sampleEntity.isPublish();

      GeosamplesApprovalEntity approvalEntity = new GeosamplesApprovalEntity();
      approvalEntity.setApprovalState(ApprovalState.PENDING);
      sampleEntity.setApproval(approvalEntity);
      sampleEntity = curatorsSampleTsqpRepository.save(sampleEntity);

      ProviderSampleView view = new ProviderSampleView();
      view.setImlgs(sampleEntity.getImlgs());
      view.setCruise(sampleEntity.getCruise().getCruiseName());
      view.setSample(sampleEntity.getSample());
      view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sampleEntity.getDevice().getDeviceCode());
      view.setBeginDate(sampleEntity.getBeginDate());
      view.setEndDate(sampleEntity.getEndDate());
      view.setLat(sampleEntity.getLat() + 1);
      view.setEndLat(sampleEntity.getEndLat() + 1);
      view.setLon(sampleEntity.getLon() + 1);
      view.setEndLon(sampleEntity.getEndLon() + 1);
      view.setLatLonOrig(sampleEntity.getLatLonOrig());
      view.setWaterDepth(sampleEntity.getWaterDepth() + 1);
      view.setEndWaterDepth(sampleEntity.getEndWaterDepth() + 1);
      view.setStorageMethCode(sampleEntity.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sampleEntity.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sampleEntity.getCoredDiam()));
      view.setPi(sampleEntity.getPi() + "-NEW");
      view.setLake(sampleEntity.getLake() + "-NEW");
      view.setOtherLink(sampleEntity.getOtherLink());
      view.setIgsn(sampleEntity.getIgsn());
      view.setSampleComments(sampleEntity.getSampleComments());
      view.setShowSampl(sampleEntity.getShowSampl());

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());
      ProviderSampleView result = providerSampleService.update(sampleEntity.getImlgs(), view, authentication);

      assertEquals(view.getImlgs(), result.getImlgs());
      assertEquals(view.getCruise(), result.getCruise());
      assertEquals(view.getSample(), result.getSample());
      assertEquals(view.getPlatform(), result.getPlatform());
      assertEquals(view.getDeviceCode(), result.getDeviceCode());
      assertEquals(view.getBeginDate(), result.getBeginDate());
      assertEquals(view.getEndDate(), result.getEndDate());
      assertEquals(view.getLat(), result.getLat());
      assertEquals(view.getEndLat(), result.getEndLat());
      assertEquals(view.getLon(), result.getLon());
      assertEquals(view.getEndLon(), result.getEndLon());
      assertEquals(view.getLatLonOrig(), result.getLatLonOrig());
      assertEquals(view.getWaterDepth(), result.getWaterDepth());
      assertEquals(view.getEndWaterDepth(), result.getEndWaterDepth());
      assertEquals(view.getStorageMethCode(), result.getStorageMethCode());
      assertEquals(view.getCoredLength(), result.getCoredLength());
      assertEquals(view.getCoredDiam(), result.getCoredDiam());
      assertEquals(view.getPi(), result.getPi());
      assertEquals(view.getLake(), result.getLake());
      assertEquals(view.getOtherLink(), result.getOtherLink());
      assertEquals(view.getIgsn(), result.getIgsn());
      assertEquals(view.getSampleComments(), result.getSampleComments());
      assertEquals(view.getShowSampl(), result.getShowSampl());

      sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      assertEquals(result.getImlgs(), sampleEntity.getImlgs());
      assertEquals(result.getCruise(), sampleEntity.getCruise().getCruiseName());
      assertEquals(result.getSample(), sampleEntity.getSample());
      assertEquals(result.getPlatform(), sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      assertEquals(result.getDeviceCode(), sampleEntity.getDevice().getDeviceCode());
      assertEquals(result.getBeginDate(), sampleEntity.getBeginDate());
      assertEquals(result.getEndDate(), sampleEntity.getEndDate());
      assertEquals(result.getLat(), sampleEntity.getLat());
      assertEquals(result.getEndLat(), sampleEntity.getEndLat());
      assertEquals(result.getLon(), sampleEntity.getLon());
      assertEquals(result.getEndLon(), sampleEntity.getEndLon());
      assertEquals(result.getLatLonOrig(), sampleEntity.getLatLonOrig());
      assertEquals(result.getWaterDepth(), sampleEntity.getWaterDepth());
      assertEquals(result.getEndWaterDepth(), sampleEntity.getEndWaterDepth());
      assertEquals(result.getStorageMethCode(), sampleEntity.getStorageMeth().getStorageMethCode());
      assertEquals(result.getCoredLength(), Double.valueOf(sampleEntity.getCoredLength()));
      assertEquals(result.getCoredDiam(), Double.valueOf(sampleEntity.getCoredDiam()));
      assertEquals(result.getPi(), sampleEntity.getPi());
      assertEquals(result.getLake(), sampleEntity.getLake());
      assertEquals(result.getOtherLink(), sampleEntity.getOtherLink());
      assertEquals(result.getIgsn(), sampleEntity.getIgsn());
      assertEquals(result.getSampleComments(), sampleEntity.getSampleComments());
      assertEquals(result.getShowSampl(), sampleEntity.getShowSampl());
      assertEquals(originalPublish, sampleEntity.isPublish());
      assertEquals(ApprovalState.PENDING, sampleEntity.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderSampleFromRejected() throws Exception {
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
      CuratorsSampleTsqpEntity sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      final boolean originalPublish = sampleEntity.isPublish();

      GeosamplesApprovalEntity approvalEntity = new GeosamplesApprovalEntity();
      approvalEntity.setApprovalState(ApprovalState.REJECTED);
      sampleEntity.setApproval(approvalEntity);
      sampleEntity = curatorsSampleTsqpRepository.save(sampleEntity);

      ProviderSampleView view = new ProviderSampleView();
      view.setImlgs(sampleEntity.getImlgs());
      view.setCruise(sampleEntity.getCruise().getCruiseName());
      view.setSample(sampleEntity.getSample());
      view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sampleEntity.getDevice().getDeviceCode());
      view.setBeginDate(sampleEntity.getBeginDate());
      view.setEndDate(sampleEntity.getEndDate());
      view.setLat(sampleEntity.getLat() + 1);
      view.setEndLat(sampleEntity.getEndLat() + 1);
      view.setLon(sampleEntity.getLon() + 1);
      view.setEndLon(sampleEntity.getEndLon() + 1);
      view.setLatLonOrig(sampleEntity.getLatLonOrig());
      view.setWaterDepth(sampleEntity.getWaterDepth() + 1);
      view.setEndWaterDepth(sampleEntity.getEndWaterDepth() + 1);
      view.setStorageMethCode(sampleEntity.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sampleEntity.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sampleEntity.getCoredDiam()));
      view.setPi(sampleEntity.getPi() + "-NEW");
      view.setLake(sampleEntity.getLake() + "-NEW");
      view.setOtherLink(sampleEntity.getOtherLink());
      view.setIgsn(sampleEntity.getIgsn());
      view.setSampleComments(sampleEntity.getSampleComments());
      view.setShowSampl(sampleEntity.getShowSampl());

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());
      ProviderSampleView result = providerSampleService.update(sampleEntity.getImlgs(), view, authentication);

      assertEquals(view.getImlgs(), result.getImlgs());
      assertEquals(view.getCruise(), result.getCruise());
      assertEquals(view.getSample(), result.getSample());
      assertEquals(view.getPlatform(), result.getPlatform());
      assertEquals(view.getDeviceCode(), result.getDeviceCode());
      assertEquals(view.getBeginDate(), result.getBeginDate());
      assertEquals(view.getEndDate(), result.getEndDate());
      assertEquals(view.getLat(), result.getLat());
      assertEquals(view.getEndLat(), result.getEndLat());
      assertEquals(view.getLon(), result.getLon());
      assertEquals(view.getEndLon(), result.getEndLon());
      assertEquals(view.getLatLonOrig(), result.getLatLonOrig());
      assertEquals(view.getWaterDepth(), result.getWaterDepth());
      assertEquals(view.getEndWaterDepth(), result.getEndWaterDepth());
      assertEquals(view.getStorageMethCode(), result.getStorageMethCode());
      assertEquals(view.getCoredLength(), result.getCoredLength());
      assertEquals(view.getCoredDiam(), result.getCoredDiam());
      assertEquals(view.getPi(), result.getPi());
      assertEquals(view.getLake(), result.getLake());
      assertEquals(view.getOtherLink(), result.getOtherLink());
      assertEquals(view.getIgsn(), result.getIgsn());
      assertEquals(view.getSampleComments(), result.getSampleComments());
      assertEquals(view.getShowSampl(), result.getShowSampl());

      sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      assertEquals(result.getImlgs(), sampleEntity.getImlgs());
      assertEquals(result.getCruise(), sampleEntity.getCruise().getCruiseName());
      assertEquals(result.getSample(), sampleEntity.getSample());
      assertEquals(result.getPlatform(), sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      assertEquals(result.getDeviceCode(), sampleEntity.getDevice().getDeviceCode());
      assertEquals(result.getBeginDate(), sampleEntity.getBeginDate());
      assertEquals(result.getEndDate(), sampleEntity.getEndDate());
      assertEquals(result.getLat(), sampleEntity.getLat());
      assertEquals(result.getEndLat(), sampleEntity.getEndLat());
      assertEquals(result.getLon(), sampleEntity.getLon());
      assertEquals(result.getEndLon(), sampleEntity.getEndLon());
      assertEquals(result.getLatLonOrig(), sampleEntity.getLatLonOrig());
      assertEquals(result.getWaterDepth(), sampleEntity.getWaterDepth());
      assertEquals(result.getEndWaterDepth(), sampleEntity.getEndWaterDepth());
      assertEquals(result.getStorageMethCode(), sampleEntity.getStorageMeth().getStorageMethCode());
      assertEquals(result.getCoredLength(), Double.valueOf(sampleEntity.getCoredLength()));
      assertEquals(result.getCoredDiam(), Double.valueOf(sampleEntity.getCoredDiam()));
      assertEquals(result.getPi(), sampleEntity.getPi());
      assertEquals(result.getLake(), sampleEntity.getLake());
      assertEquals(result.getOtherLink(), sampleEntity.getOtherLink());
      assertEquals(result.getIgsn(), sampleEntity.getIgsn());
      assertEquals(result.getSampleComments(), sampleEntity.getSampleComments());
      assertEquals(result.getShowSampl(), sampleEntity.getShowSampl());
      assertEquals(originalPublish, sampleEntity.isPublish());
      assertEquals(ApprovalState.PENDING, sampleEntity.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderSampleSampleApproved() throws Exception {
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

    ProviderSampleView providerSampleView = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      GeosamplesApprovalEntity approvalEntity = new GeosamplesApprovalEntity();
      approvalEntity.setApprovalState(ApprovalState.APPROVED);
      sampleEntity.setApproval(approvalEntity);
      sampleEntity = curatorsSampleTsqpRepository.save(sampleEntity);

      ProviderSampleView view = new ProviderSampleView();
      view.setImlgs(sampleEntity.getImlgs());
      view.setCruise(sampleEntity.getCruise().getCruiseName());
      view.setSample(sampleEntity.getSample());
      view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sampleEntity.getDevice().getDeviceCode());
      view.setBeginDate(sampleEntity.getBeginDate());
      view.setEndDate(sampleEntity.getEndDate());
      view.setLat(sampleEntity.getLat() + 1);
      view.setEndLat(sampleEntity.getEndLat() + 1);
      view.setLon(sampleEntity.getLon() + 1);
      view.setEndLon(sampleEntity.getEndLon() + 1);
      view.setLatLonOrig(sampleEntity.getLatLonOrig());
      view.setWaterDepth(sampleEntity.getWaterDepth() + 1);
      view.setEndWaterDepth(sampleEntity.getEndWaterDepth() + 1);
      view.setStorageMethCode(sampleEntity.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sampleEntity.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sampleEntity.getCoredDiam()));
      view.setPi(sampleEntity.getPi() + "-NEW");
      view.setLake(sampleEntity.getLake() + "-NEW");
      view.setOtherLink(sampleEntity.getOtherLink());
      view.setIgsn(sampleEntity.getIgsn());
      view.setSampleComments(sampleEntity.getSampleComments());
      view.setShowSampl(sampleEntity.getShowSampl());

      return view;
    });
    assertNotNull(providerSampleView);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.update(providerSampleView.getImlgs(), providerSampleView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderSampleNotFound() {
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

    ProviderSampleView view = new ProviderSampleView();
    view.setImlgs("TST");
    view.setCruise("TST");
    view.setSample("TST0001");
    view.setPlatform("TST");
    view.setDeviceCode("TST");
    view.setBeginDate("20200101");
    view.setEndDate("20200101");
    view.setLat(1.);
    view.setEndLat(1.);
    view.setLon(1.);
    view.setEndLon(1.);
    view.setLatLonOrig("TST");
    view.setWaterDepth(1);
    view.setEndWaterDepth(1);
    view.setStorageMethCode("A");
    view.setCoredLength(1.);
    view.setCoredDiam(1.);
    view.setPi("TST");
    view.setProvinceCode("01");
    view.setLake("TST");
    view.setOtherLink("TST");
    view.setIgsn("TST");
    view.setLeg("TST");
    view.setSampleComments("TST");
    view.setShowSampl("TST");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.update(view.getImlgs(), view, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderSampleSampleDoesNotBelongToProvider() throws Exception {
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

    ProviderSampleView sampleView = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      ProviderSampleView view = new ProviderSampleView();
      view.setImlgs(sampleEntity.getImlgs());
      view.setCruise(sampleEntity.getCruise().getCruiseName());
      view.setSample(sampleEntity.getSample());
      view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sampleEntity.getDevice().getDeviceCode());
      view.setBeginDate(sampleEntity.getBeginDate());
      view.setEndDate(sampleEntity.getEndDate());
      view.setLat(sampleEntity.getLat() + 1);
      view.setEndLat(sampleEntity.getEndLat() + 1);
      view.setLon(sampleEntity.getLon() + 1);
      view.setEndLon(sampleEntity.getEndLon() + 1);
      view.setLatLonOrig(sampleEntity.getLatLonOrig());
      view.setWaterDepth(sampleEntity.getWaterDepth() + 1);
      view.setEndWaterDepth(sampleEntity.getEndWaterDepth() + 1);
      view.setStorageMethCode(sampleEntity.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sampleEntity.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sampleEntity.getCoredDiam()));
      view.setPi(sampleEntity.getPi() + "-NEW");
      view.setLake(sampleEntity.getLake() + "-NEW");
      view.setOtherLink(sampleEntity.getOtherLink());
      view.setIgsn(sampleEntity.getIgsn());
      view.setSampleComments(sampleEntity.getSampleComments());
      view.setShowSampl(sampleEntity.getShowSampl());
      return view;
    });
    assertNotNull(sampleView);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.update(sampleView.getImlgs(), sampleView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderSampleConflict() throws Exception {
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

    CuratorsFacilityEntity facility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity f = new CuratorsFacilityEntity();
      f.setFacilityCode("TST");
      f.setFacility("Test Facility");
      f.setInstCode("TST");
      f.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(f);
    });
    assertNotNull(facility);

    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    CuratorsCruiseEntity cruiseEntity = transactionTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      CuratorsLegEntity leg = new CuratorsLegEntity();
      leg.setLegName("TST");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      cruise = curatorsCruiseRepository.save(cruise);
      return cruise;
    });
    assertNotNull(cruiseEntity);

    ProviderSampleView providerSampleView = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sampleEntity.setApproval(approval);
      sampleEntity = curatorsSampleTsqpRepository.save(sampleEntity);

      ProviderSampleView view = new ProviderSampleView();
      view.setImlgs(sampleEntity.getImlgs());
      view.setCruise(cruiseEntity.getCruiseName());
      view.setSample(sampleEntity.getSample());
      view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sampleEntity.getDevice().getDeviceCode());
      view.setBeginDate(sampleEntity.getBeginDate());
      view.setEndDate(sampleEntity.getEndDate());
      view.setLat(sampleEntity.getLat() + 1);
      view.setEndLat(sampleEntity.getEndLat() + 1);
      view.setLon(sampleEntity.getLon() + 1);
      view.setEndLon(sampleEntity.getEndLon() + 1);
      view.setLatLonOrig(sampleEntity.getLatLonOrig());
      view.setWaterDepth(sampleEntity.getWaterDepth() + 1);
      view.setEndWaterDepth(sampleEntity.getEndWaterDepth() + 1);
      view.setStorageMethCode(sampleEntity.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sampleEntity.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sampleEntity.getCoredDiam()));
      view.setPi(sampleEntity.getPi() + "-NEW");
      view.setLake(sampleEntity.getLake() + "-NEW");
      view.setOtherLink(sampleEntity.getOtherLink());
      view.setIgsn(sampleEntity.getIgsn());
      view.setSampleComments(sampleEntity.getSampleComments());
      view.setShowSampl(sampleEntity.getShowSampl());
      return view;
    });
    assertNotNull(providerSampleView);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.update(providerSampleView.getImlgs(), providerSampleView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Unable to find cruise: %s", providerSampleView.getCruise()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteSample() throws Exception {
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

   final String sampleIMLGS = transactionTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(samp -> samp.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sample.setApproval(approval);
      sample = curatorsSampleTsqpRepository.save(sample);

      CuratorsCruiseFacilityEntity cruiseFacility = new CuratorsCruiseFacilityEntity();
      cruiseFacility.setCruise(sample.getCruise());
      cruiseFacility.setFacility(facility);
      cruiseFacility = curatorsCruiseFacilityRepository.save(cruiseFacility);
      sample.setCruiseFacility(cruiseFacility);
      sample = curatorsSampleTsqpRepository.save(sample);

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());

      ProviderSampleView result = providerSampleService.delete(sample.getImlgs(), authentication);
      assertNotNull(result);
      assertEquals(sample.getImlgs(), result.getImlgs());
      assertEquals(sample.getCruise().getCruiseName(), result.getCruise());
      assertEquals(sample.getSample(), result.getSample());
      assertEquals(sample.getCruisePlatform().getPlatform().getPlatform(), result.getPlatform());
      assertEquals(sample.getDevice().getDeviceCode(), result.getDeviceCode());
      assertEquals(sample.getBeginDate(), result.getBeginDate());
      assertEquals(sample.getEndDate(), result.getEndDate());
      assertEquals(sample.getLat(), result.getLat());
      assertEquals(sample.getEndLat(), result.getEndLat());
      assertEquals(sample.getLon(), result.getLon());
      assertEquals(sample.getEndLon(), result.getEndLon());
      assertEquals(sample.getLatLonOrig(), result.getLatLonOrig());
      assertEquals(sample.getWaterDepth(), result.getWaterDepth());
      assertEquals(sample.getEndWaterDepth(), result.getEndWaterDepth());
      assertEquals(sample.getStorageMeth().getStorageMethCode(), result.getStorageMethCode());
      assertEquals(sample.getCoredLength(), result.getCoredLength().intValue());
      assertEquals(sample.getCoredDiam(), result.getCoredDiam().intValue());
      assertEquals(sample.getPi(), result.getPi());
      if (sample.getProvince() != null) {
        assertEquals(sample.getProvince().getProvinceCode(), result.getProvinceCode());
      }
      assertEquals(sample.getLake(), result.getLake());
      assertEquals(sample.getOtherLink(), result.getOtherLink());
      assertEquals(sample.getIgsn(), result.getIgsn());
      assertEquals(sample.getSampleComments(), result.getSampleComments());
      assertEquals(sample.getShowSampl(), result.getShowSampl());

      return result.getImlgs();
    });
    assertNotNull(sampleIMLGS);
    assertFalse(curatorsSampleTsqpRepository.existsById(sampleIMLGS));
  }

  @Test
  public void testDeleteSampleNotFound() throws Exception {
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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerSampleService.delete("DOES_NOT_EXIST", authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteSampleSampleDoesNotBelongToProvider() throws Exception {
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

    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
        .filter(samp -> samp.getSample().equals("AQ-01-01")).findFirst().orElseThrow(
            () -> new RuntimeException("Sample AQ-01-01 not found")
        );

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () ->providerSampleService.delete(sample.getImlgs(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  private void createSamples() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();
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
