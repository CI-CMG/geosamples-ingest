package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderCruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ProviderCruiseServiceIT {

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
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private ProviderCruiseService providerCruiseService;

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
  public void testCreateProviderCruise() {
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

    ProviderCruiseView cruise = new ProviderCruiseView();
    cruise.setYear(2020);
    cruise.setCruiseName("Test Cruise");
    cruise.setPlatforms(Collections.singletonList(platformEntity.getPlatform()));
    cruise.setLegs(Collections.singletonList("TST"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("gabby");
    ProviderCruiseView created = providerCruiseService.create(cruise, authentication);

    assertEquals(cruise.getYear(), created.getYear());
    assertEquals(cruise.getCruiseName(), created.getCruiseName());
    assertEquals(cruise.getPlatforms(), created.getPlatforms());
    assertEquals(cruise.getLegs(), created.getLegs());

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruiseEntity = curatorsCruiseRepository.findById(created.getId()).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      assertEquals(created.getId(), cruiseEntity.getId());
      assertEquals(created.getCruiseName(), cruiseEntity.getCruiseName());
      assertEquals(created.getYear(), (int) cruiseEntity.getYear());

      assertEquals(1, cruiseEntity.getPlatformMappings().size());
      assertEquals(created.getPlatforms().get(0), cruiseEntity.getPlatformMappings().get(0).getPlatform().getPlatform());
      assertEquals(1, cruiseEntity.getLegs().size());
      assertEquals(created.getLegs().get(0), cruiseEntity.getLegs().get(0).getLegName());
      assertEquals(1, cruiseEntity.getFacilityMappings().size());
      assertEquals(facility.getFacilityCode(), cruiseEntity.getFacilityMappings().get(0).getFacility().getFacilityCode());
      assertEquals(ApprovalState.PENDING, cruiseEntity.getApproval().getApprovalState());
    });
  }

  @Test
  public void testCreateProviderCruiseConflict() {
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

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ProviderCruiseView view = new ProviderCruiseView();
    view.setYear((int) cruiseEntity.getYear());
    view.setCruiseName(cruiseEntity.getCruiseName());
    view.setPlatforms(Collections.singletonList(platformEntity.getPlatform()));
    view.setLegs(Collections.singletonList("TST-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    assertThrows(DataIntegrityViolationException.class, () -> providerCruiseService.create(view, authentication));
  }

  @Test
  public void testCreateProviderCruiseNoFacility() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    ProviderCruiseView cruise = new ProviderCruiseView();
    cruise.setYear(2020);
    cruise.setCruiseName("Test Cruise");
    cruise.setPlatforms(Collections.singletonList("TST"));
    cruise.setLegs(Collections.singletonList("TST"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("gabby");

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.create(cruise, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("User %s has no assigned facility", userEntity.getUserName()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderCruise() {
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

    transactionTemplate.executeWithoutResult(s -> {
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

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn("gabby");
      ProviderCruiseView view = providerCruiseService.get(cruise.getId(), authentication);

      assertEquals(cruise.getId(), view.getId());
      assertEquals((int) cruise.getYear(), view.getYear());
      assertEquals(cruise.getCruiseName(), view.getCruiseName());
      assertEquals(1, view.getPlatforms().size());
      assertEquals(cruise.getPlatformMappings().get(0).getPlatform().getPlatform(), view.getPlatforms().get(0));
      assertEquals(1, view.getLegs().size());
      assertEquals(cruise.getLegs().get(0).getLegName(), view.getLegs().get(0));
    });
  }

  @Test
  public void testGetProviderCruiseNotFound() {
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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("gabby");
    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.get(1L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderCruiseCruiseDoesNotBelongToFacility() {
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

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("gabby");

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.get(cruiseEntity.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchProviderCruisesBelongingToFacility() {
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

    transactionTemplate.executeWithoutResult(s -> {
      List<CuratorsCruiseEntity> cruises = new ArrayList<>();

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

      curatorsCruiseRepository.save(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST2");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility1);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST2");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      cruise = curatorsCruiseRepository.save(cruise);
      cruises.add(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST3");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility2);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST3");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      curatorsCruiseRepository.save(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST4");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility1);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST4");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      cruise = curatorsCruiseRepository.save(cruise);
      cruises.add(cruise);

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());

      ProviderCruiseSearchParameters searchParameters = new ProviderCruiseSearchParameters();
      searchParameters.setPage(1);
      searchParameters.setItemsPerPage(10);
      PagedItemsView<ProviderCruiseView> results = providerCruiseService.search(searchParameters, authentication);
      assertNotNull(results);
      assertEquals(2, results.getTotalItems());
      assertEquals(1, results.getPage());
      assertEquals(10, results.getItemsPerPage());
      assertEquals(1, results.getTotalPages());
      assertEquals(2, results.getItems().size());

      assertEquals(cruises.get(0).getId(), results.getItems().get(0).getId());
      assertEquals((int) cruises.get(0).getYear(), results.getItems().get(0).getYear());
      assertEquals(cruises.get(0).getCruiseName(), results.getItems().get(0).getCruiseName());
      assertEquals(1, results.getItems().get(0).getLegs().size());
      assertEquals(cruises.get(0).getLegs().get(0).getLegName(), results.getItems().get(0).getLegs().get(0));
      assertEquals(1, results.getItems().get(0).getPlatforms().size());
      assertEquals(cruises.get(0).getPlatformMappings().get(0).getPlatform().getPlatform(), results.getItems().get(0).getPlatforms().get(0));

      assertEquals(cruises.get(1).getId(), results.getItems().get(1).getId());
      assertEquals((int) cruises.get(1).getYear(), results.getItems().get(1).getYear());
      assertEquals(cruises.get(1).getCruiseName(), results.getItems().get(1).getCruiseName());
      assertEquals(1, results.getItems().get(1).getLegs().size());
      assertEquals(cruises.get(1).getLegs().get(0).getLegName(), results.getItems().get(1).getLegs().get(0));
      assertEquals(1, results.getItems().get(1).getPlatforms().size());
      assertEquals(cruises.get(1).getPlatformMappings().get(0).getPlatform().getPlatform(), results.getItems().get(1).getPlatforms().get(0));
    });
  }

  @Test
  public void testSearchProviderCruisesBelongingToFacilityNoResults() {
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

    transactionTemplate.executeWithoutResult(s -> {
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

      curatorsCruiseRepository.save(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST2");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility2);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST2");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      curatorsCruiseRepository.save(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST3");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility2);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST3");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      curatorsCruiseRepository.save(cruise);

      cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST4");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility2);
      facilityMapping.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping);

      platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      leg = new CuratorsLegEntity();
      leg.setLegName("TST4");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      curatorsCruiseRepository.save(cruise);
    });

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderCruiseSearchParameters searchParameters = new ProviderCruiseSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);
    PagedItemsView<ProviderCruiseView> results = providerCruiseService.search(searchParameters, authentication);
    assertNotNull(results);
    assertEquals(0, results.getTotalItems());
    assertEquals(1, results.getPage());
    assertEquals(10, results.getItemsPerPage());
    assertEquals(0, results.getTotalPages());
    assertEquals(0, results.getItems().size());
  }

  @Test
  public void testUpdateProviderCruise() {
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

    PlatformMasterEntity platformEntity1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity1);

    PlatformMasterEntity platformEntity2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity2);

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
      platformMapping.setPlatform(platformEntity1);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      CuratorsLegEntity leg = new CuratorsLegEntity();
      leg.setLegName("TST");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(cruiseEntity.getId());
    view.setYear((int) cruiseEntity.getYear());
    view.setCruiseName(cruiseEntity.getCruiseName() + "-NEW-NAME");
    view.setPlatforms(Collections.singletonList(platformEntity2.getPlatform()));
    view.setLegs(Collections.singletonList("TST-NEW-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderCruiseView updated = providerCruiseService.update(cruiseEntity.getId(), view, authentication);
    assertEquals(cruiseEntity.getId(), updated.getId());
    assertEquals(view.getCruiseName(), updated.getCruiseName());
    assertEquals(view.getYear(), updated.getYear());
    assertEquals(view.getPlatforms(), updated.getPlatforms());
    assertEquals(view.getLegs(), updated.getLegs());

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findById(cruiseEntity.getId()).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );
      assertEquals(view.getId(), cruise.getId());
      assertEquals(view.getYear(), (int) cruise.getYear());
      assertEquals(view.getCruiseName(), cruise.getCruiseName());
      assertEquals(1, cruise.getPlatformMappings().size());
      assertEquals(view.getPlatforms().get(0), cruise.getPlatformMappings().get(0).getPlatform().getPlatform());
      assertEquals(1, cruise.getLegs().size());
      assertEquals(view.getLegs().get(0), cruise.getLegs().get(0).getLegName());
    });
  }

  @Test
  public void testUpdateProviderCruiseCruiseApproved() {
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

    PlatformMasterEntity platformEntity1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity1);

    PlatformMasterEntity platformEntity2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity2);

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
      platformMapping.setPlatform(platformEntity1);
      platformMapping.setCruise(cruise);
      cruise.addPlatformMapping(platformMapping);

      CuratorsLegEntity leg = new CuratorsLegEntity();
      leg.setLegName("TST");
      leg.setCruise(cruise);
      leg.setPublish(true);
      cruise.addLeg(leg);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      cruise.setApproval(approval);

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(cruiseEntity.getId());
    view.setYear((int) cruiseEntity.getYear());
    view.setCruiseName(cruiseEntity.getCruiseName() + "-NEW-NAME");
    view.setPlatforms(Collections.singletonList(platformEntity2.getPlatform()));
    view.setLegs(Collections.singletonList("TST-NEW-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.update(cruiseEntity.getId(), view, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderCruiseNotFound() {
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

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(100L);
    view.setYear(2020);
    view.setCruiseName("TST-NEW-NAME");
    view.setPlatforms(Collections.singletonList("TST"));
    view.setLegs(Collections.singletonList("TST-NEW-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.update(100L, view, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderCruiseCruiseDoesNotBelongToFacility() {
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

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility1);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformEntity1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity1);

    PlatformMasterEntity platformEntity2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity2);

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
      platformMapping.setPlatform(platformEntity1);
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

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(cruiseEntity.getId());
    view.setYear((int) cruiseEntity.getYear());
    view.setCruiseName(cruiseEntity.getCruiseName() + "-NEW-NAME");
    view.setPlatforms(Collections.singletonList(platformEntity2.getPlatform()));
    view.setLegs(Collections.singletonList("TST-NEW-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.update(cruiseEntity.getId(), view, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderCruiseConflict() {
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

    CuratorsCruiseEntity cruiseEntity1 = transactionTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST-TO-CHANGE");
      cruise.setYear((short) 2021);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
      facilityMapping.setFacility(facility1);
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

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity1);

    CuratorsCruiseEntity cruiseEntity2 = transactionTemplate.execute(s -> {
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

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity2);

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(cruiseEntity1.getId());
    view.setYear((int) cruiseEntity2.getYear());
    view.setCruiseName(cruiseEntity2.getCruiseName());
    view.setPlatforms(Collections.singletonList(platformEntity.getPlatform()));
    view.setLegs(Collections.singletonList("TST-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    assertThrows(DataIntegrityViolationException.class, () -> providerCruiseService.update(cruiseEntity1.getId(), view, authentication));
  }

  @Test
  public void testUpdateProviderCruiseCannotUpdate() {
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

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby");
      user.setFacility(facility1);
      return geosamplesUserRepository.save(user) ;
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformEntity1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity1);

    PlatformMasterEntity platformEntity2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity2);

    CuratorsCruiseEntity cruiseEntity = transactionTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);
      cruise = curatorsCruiseRepository.save(cruise);

      CuratorsCruiseFacilityEntity facilityMapping1 = new CuratorsCruiseFacilityEntity();
      facilityMapping1.setFacility(facility1);
      facilityMapping1.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping1);

      CuratorsCruiseFacilityEntity facilityMapping2 = new CuratorsCruiseFacilityEntity();
      facilityMapping2.setFacility(facility2);
      facilityMapping2.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping2);

      CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
      platformMapping.setPlatform(platformEntity1);
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

    ProviderCruiseView view = new ProviderCruiseView();
    view.setId(cruiseEntity.getId());
    view.setYear((int) cruiseEntity.getYear());
    view.setCruiseName(cruiseEntity.getCruiseName() + "-NEW-NAME");
    view.setPlatforms(Collections.singletonList(platformEntity2.getPlatform()));
    view.setLegs(Collections.singletonList("TST-NEW-LEG"));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.update(cruiseEntity.getId(), view, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderCruise() {
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

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setCruiseName("TST");
      cruise.setYear((short) 2020);
      cruise.setPublish(true);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);

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

      Authentication authentication = mock(Authentication.class);
      when(authentication.getName()).thenReturn(userEntity.getUserName());

      ProviderCruiseView view = providerCruiseService.delete(cruise.getId(), authentication);
      assertEquals(cruise.getId(), view.getId());
      assertEquals((int) cruise.getYear(), view.getYear());
      assertEquals(cruise.getCruiseName(), view.getCruiseName());
      assertEquals(1, view.getPlatforms().size());
      assertEquals(cruise.getPlatformMappings().get(0).getPlatform().getPlatform(), view.getPlatforms().get(0));
      assertEquals(1, view.getLegs().size());
      assertEquals(cruise.getLegs().get(0).getLegName(), view.getLegs().get(0));

      assertEquals(0, curatorsCruiseRepository.count());
    });
  }

  @Test
  public void testDeleteProviderCruiseNotFound() {
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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.delete(100L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderCruiseCruiseDoesNotBelongToFacility() {
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

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.delete(cruiseEntity.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderCruiseCannotDelete() {
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

      CuratorsCruiseFacilityEntity facilityMapping1 = new CuratorsCruiseFacilityEntity();
      facilityMapping1.setFacility(facility1);
      facilityMapping1.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping1);

      CuratorsCruiseFacilityEntity facilityMapping2 = new CuratorsCruiseFacilityEntity();
      facilityMapping2.setFacility(facility2);
      facilityMapping2.setCruise(cruise);
      cruise.addFacilityMapping(facilityMapping2);

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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerCruiseService.delete(cruiseEntity.getId(), authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(status -> {
      curatorsCruiseRepository.deleteAll();
      platformMasterRepository.findByPlatformNormalized("TST").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST2").ifPresent(platformMasterRepository::delete);
      geosamplesUserRepository.findById("gabby").ifPresent(geosamplesUserRepository::delete);
      geosamplesUserRepository.findById("martin").ifPresent(geosamplesUserRepository::delete);
      curatorsFacilityRepository.findByFacilityCode("TST").ifPresent(curatorsFacilityRepository::delete);
      curatorsFacilityRepository.findByFacilityCode("TS2").ifPresent(curatorsFacilityRepository::delete);
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
    });
  }

}
