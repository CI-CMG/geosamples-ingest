package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.JwksGenTest;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
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
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
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
import org.springframework.lang.Nullable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class CruiseServiceIT {

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
  private TransactionTemplate txTemplate;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @BeforeEach
  public void before() {
    txTemplate.executeWithoutResult(s -> {
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      curatorsCruiseFacilityRepository.deleteAll();
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
      platformMasterRepository.findByPlatformNormalized("AFRICAN QUEEN").ifPresent((p) -> {
        p.setApproval(null);
        platformMasterRepository.save(p);
      });
    });
  }

  @AfterEach
  public void after() {
    txTemplate.executeWithoutResult(s -> {
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      curatorsCruiseFacilityRepository.deleteAll();
      geosamplesUserRepository.deleteById("martin");
      geosamplesRoleRepository.getByRoleName("ROLE_ADMIN").ifPresent(geosamplesRoleRepository::delete);
      platformMasterRepository.findByPlatformNormalized("AFRICAN QUEEN").ifPresent((p) -> {
        p.setApproval(null);
        platformMasterRepository.save(p);
      });
    });
  }

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CruiseService cruiseService;
  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Autowired
  private CuratorsCruiseFacilityRepository curatorsCruiseFacilityRepository;

  @Test
  public void testReviewCruise() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    CuratorsCruiseEntity cruiseEntity = txTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);
      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.APPROVED);
    approvalView.setComment("Looks good to me");
    CruiseView result = cruiseService.updateApproval(approvalView, cruiseEntity.getId());
    assertEquals(ApprovalState.APPROVED, result.getApprovalState());

    txTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity resultCruise = curatorsCruiseRepository.findById(cruiseEntity.getId()).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      assertEquals(ApprovalState.APPROVED, resultCruise.getApproval().getApprovalState());
      assertEquals("Looks good to me", resultCruise.getApproval().getComment());
    });
  }

  @Test
  public void testReviewCruisePlatformNotApproved() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    txTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity platform = platformMasterRepository.findByPlatformNormalized("AFRICAN QUEEN").orElseThrow(
          () -> new RuntimeException("Platform not found")
      );
      GeosamplesApprovalEntity platformApproval = new GeosamplesApprovalEntity();
      platformApproval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(platformApproval);
      platformMasterRepository.save(platform);
    });

    CuratorsCruiseEntity cruiseEntity = txTemplate.execute(s -> {

      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity cruiseApproval = new GeosamplesApprovalEntity();
      cruiseApproval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(cruiseApproval);

      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.APPROVED);

    ApiException exception = assertThrows(ApiException.class, () -> cruiseService.updateApproval(approvalView, cruiseEntity.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Platform African Queen is not approved", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewCruiseRevokeApproval() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    CuratorsCruiseEntity cruiseEntity = txTemplate.execute(s -> {

      curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .forEach(smpl -> {
            smpl.setPublish(true);
            GeosamplesApprovalEntity sampleApproval = new GeosamplesApprovalEntity();
            sampleApproval.setApprovalState(ApprovalState.APPROVED);
            smpl.setApproval(sampleApproval);
            smpl = curatorsSampleTsqpRepository.save(smpl);
            smpl.getIntervals().forEach(interval -> {
              interval.setPublish(true);
              GeosamplesApprovalEntity intervalApproval = new GeosamplesApprovalEntity();
              intervalApproval.setApprovalState(ApprovalState.APPROVED);
              interval.setApproval(intervalApproval);
              curatorsIntervalRepository.save(interval);
            });
          });

      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-01", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      cruise.setApproval(approval);
      cruise.setPublish(true);
      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.REJECTED);
    approvalView.setComment("There's a problem here");
    CruiseView result = cruiseService.updateApproval(approvalView, cruiseEntity.getId());
    assertEquals(ApprovalState.REJECTED, result.getApprovalState());

    txTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity resultCruise = curatorsCruiseRepository.findById(cruiseEntity.getId()).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      assertEquals(ApprovalState.REJECTED, resultCruise.getApproval().getApprovalState());
      assertEquals("There's a problem here", resultCruise.getApproval().getComment());
      assertFalse(resultCruise.isPublish());

      curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .forEach(smpl -> {
            assertFalse(smpl.isPublish());
            assertEquals(ApprovalState.REJECTED, smpl.getApproval().getApprovalState());
            assertEquals("Cruise rejected", smpl.getApproval().getComment());
            smpl.getIntervals().forEach(interval -> {
              assertFalse(interval.isPublish());
              assertEquals(ApprovalState.REJECTED, interval.getApproval().getApprovalState());
              assertEquals("Sample rejected", interval.getApproval().getComment());
            });
          });
    });
  }

  @Test
  public void testReviewCruiseRevokeApprovalNoChildApprovals() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    CuratorsCruiseEntity cruiseEntity = txTemplate.execute(s -> {

      curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .forEach(smpl -> {
            smpl.setPublish(true);
            smpl = curatorsSampleTsqpRepository.save(smpl);
            smpl.getIntervals().forEach(interval -> {
              interval.setPublish(true);
              curatorsIntervalRepository.save(interval);
            });
          });

      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-01", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      cruise.setApproval(approval);
      cruise.setPublish(true);
      return curatorsCruiseRepository.save(cruise);
    });
    assertNotNull(cruiseEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.REJECTED);
    approvalView.setComment("There's a problem here");
    ApiException exception = assertThrows(ApiException.class, () -> cruiseService.updateApproval(approvalView, cruiseEntity.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Cannot update non-existent approval", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewCruiseChangeToPending() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    Long cruiseId = txTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      cruise.setApproval(approval);
      cruise.setPublish(true);
      return curatorsCruiseRepository.save(cruise).getId();
    });

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);

    ApiException exception = assertThrows(ApiException.class, () -> cruiseService.updateApproval(approvalView, cruiseId));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals("Cannot change approved item to pending", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdatePublishStatusCruiseNotApproved() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    CruiseView updateCruiseView = txTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);
      cruise.setPublish(false);

      CuratorsCruiseEntity cruiseEntity = curatorsCruiseRepository.save(cruise);

      CruiseView cruiseView = new CruiseView();
      cruiseView.setId(cruiseEntity.getId());
      cruiseView.setCruiseName(cruiseEntity.getCruiseName());
      cruiseView.setYear(Integer.valueOf(cruiseEntity.getYear()));
      cruiseView.setPublish(true);
      cruiseView.setApprovalState(cruiseEntity.getApproval().getApprovalState());
      cruiseView.setLegs(cruiseEntity.getLegs().stream().map(CuratorsLegEntity::getLegName).collect(Collectors.toList()));
      cruiseView.setFacilityCodes(cruiseEntity.getFacilityMappings().stream().map(CuratorsCruiseFacilityEntity::getFacility).map(CuratorsFacilityEntity::getFacilityCode).collect(Collectors.toList()));
      cruiseView.setPlatforms(cruiseEntity.getPlatformMappings().stream().map(CuratorsCruisePlatformEntity::getPlatform).map(PlatformMasterEntity::getPlatform).collect(Collectors.toList()));
      return cruiseView;
    });
    assertNotNull(updateCruiseView);

    ApiException exception = assertThrows(ApiException.class, () -> cruiseService.update(updateCruiseView, updateCruiseView.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Cruise %s (%s) is not approved", updateCruiseView.getCruiseName(), updateCruiseView.getYear()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApproval() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    Long cruiseId = txTemplate.execute(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      approval.setComment("Looks good");
      cruise.setApproval(approval);
      cruise.setPublish(true);
      return curatorsCruiseRepository.save(cruise).getId();
    });

    ApprovalView approvalView = cruiseService.getApproval(cruiseId);
    assertEquals(ApprovalState.APPROVED, approvalView.getApprovalState());
    assertEquals("Looks good", approvalView.getComment());
  }

  @Test
  public void testGetApprovalNotFound() throws Exception {
    createCruise(Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    Long cruiseId = txTemplate.execute(s -> curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
        () -> new RuntimeException("Cruise not found")
    ).getId());

    ApiException exception = assertThrows(ApiException.class, () -> cruiseService.getApproval(cruiseId));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Approval does not exist", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchByApprovalState() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    txTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruise1 = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );
      GeosamplesApprovalEntity approval1 = new GeosamplesApprovalEntity();
      approval1.setApprovalState(ApprovalState.APPROVED);
      cruise1.setApproval(approval1);
      curatorsCruiseRepository.save(cruise1);

      CuratorsCruiseEntity cruise2 = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-11", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );
      GeosamplesApprovalEntity approval2 = new GeosamplesApprovalEntity();
      approval2.setApprovalState(ApprovalState.APPROVED);
      cruise2.setApproval(approval2);
      curatorsCruiseRepository.save(cruise2);

      CuratorsCruiseEntity cruise3 = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-12", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );
      GeosamplesApprovalEntity approval3 = new GeosamplesApprovalEntity();
      approval3.setApprovalState(ApprovalState.REJECTED);
      cruise3.setApproval(approval3);
      curatorsCruiseRepository.save(cruise3);

      CuratorsCruiseEntity cruise4 = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-01", (short) 2021).orElseThrow(
          () -> new RuntimeException("Cruise not found")
      );
      GeosamplesApprovalEntity approval4 = new GeosamplesApprovalEntity();
      approval4.setApprovalState(ApprovalState.PENDING);
      cruise4.setApproval(approval4);
      curatorsCruiseRepository.save(cruise4);
    });

    CruiseSearchParameters searchParameters = new CruiseSearchParameters();
    searchParameters.setApprovalState(Collections.singletonList(ApprovalState.APPROVED));
    searchParameters.setItemsPerPage(10);

    PagedItemsView<CruiseView> cruiseViews = cruiseService.search(searchParameters);
    assertEquals(2, cruiseViews.getItems().size());
    assertEquals(2, cruiseViews.getTotalItems());
    assertEquals(1, cruiseViews.getTotalPages());
    assertEquals(1, cruiseViews.getPage());
    assertEquals(10, cruiseViews.getItemsPerPage());

    assertEquals("AQ-10", cruiseViews.getItems().get(0).getCruiseName());
    assertEquals("AQ-11", cruiseViews.getItems().get(1).getCruiseName());
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

  private void createCruise(List<String> facilityCodes, List<String> platforms, List<String> legs) throws Exception {
    createCruise("AQ-10", facilityCodes, platforms, legs);
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

  private void createCruise(@Nullable String cruiseId, List<String> facilityCodes, List<String> platforms, List<String> legs) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(JwksGenTest.createJwt("martin"));

    CruiseView cruiseView = new CruiseView();
    cruiseView.setCruiseName(cruiseId);
    cruiseView.setYear(2021);
    cruiseView.setPublish(true);
    cruiseView.setFacilityCodes(facilityCodes);
    cruiseView.setPlatforms(platforms);
    cruiseView.setLegs(legs);


    HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(cruiseView), headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/cruise", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }


}
