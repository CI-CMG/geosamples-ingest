package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
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
public class SampleServiceIT {

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
  private SampleService sampleService;

  @Autowired
  private GeometryFactory geometryFactory;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

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

      curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).ifPresent((c) -> {
        c.setApproval(null);
        curatorsCruiseRepository.save(c);
      });
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
      curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).ifPresent((c) -> {
        c.setApproval(null);
        curatorsCruiseRepository.save(c);
      });
    });
  }

  @Test
  public void testSearchByFacilityCode() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    SampleSearchParameters searchParameters = new SampleSearchParameters();
    searchParameters.setFacilityCode(Collections.singletonList("GEOMAR"));
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    PagedItemsView<SampleView> result = sampleService.search(searchParameters);

    assertEquals(4, result.getTotalItems());
    assertEquals(4, result.getItems().size());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals("AQ-001", result.getItems().get(0).getSample());
    assertEquals("AQ-002", result.getItems().get(1).getSample());
    assertEquals("AQ-003", result.getItems().get(2).getSample());
    assertEquals("AQ-01-01", result.getItems().get(3).getSample());
  }

  @Test
  public void testSearchByPublished() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));

    uploadFile();

    txTemplate.executeWithoutResult(s -> {
      curatorsSampleTsqpRepository.findAll().stream().filter(t -> t.getSample().equals("AQ-001")).findFirst().ifPresent(t -> {
        t.setPublish(true);
        curatorsSampleTsqpRepository.save(t);
      });

      curatorsSampleTsqpRepository.findAll().stream().filter(t -> t.getSample().equals("AQ-002")).findFirst().ifPresent(t -> {
        t.setPublish(true);
        curatorsSampleTsqpRepository.save(t);
      });

      curatorsSampleTsqpRepository.findAll().stream().filter(t -> t.getSample().equals("AQ-003")).findFirst().ifPresent(t -> {
        t.setPublish(false);
        curatorsSampleTsqpRepository.save(t);
      });

      curatorsSampleTsqpRepository.findAll().stream().filter(t -> t.getSample().equals("AQ-01-01")).findFirst().ifPresent(t -> {
        t.setPublish(false);
        curatorsSampleTsqpRepository.save(t);
      });
    });

    SampleSearchParameters searchParameters = new SampleSearchParameters();
    searchParameters.setPublish(Collections.singletonList(true));
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);

    PagedItemsView<SampleView> result = sampleService.search(searchParameters);

    assertEquals(2, result.getTotalItems());
    assertEquals(2, result.getItems().size());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
    assertEquals("AQ-001", result.getItems().get(0).getSample());
    assertEquals("AQ-002", result.getItems().get(1).getSample());
  }

  @Test
  public void testSearchByApprovalState() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    txTemplate.executeWithoutResult(s -> curatorsSampleTsqpRepository.findAll().forEach(smpl -> {
      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      if (smpl.getSample().equals("AQ-001") || smpl.getSample().equals("AQ-002")) {
        approval.setApprovalState(ApprovalState.APPROVED);
      } else {
        approval.setApprovalState(ApprovalState.PENDING);
      }
      smpl.setApproval(approval);

      curatorsSampleTsqpRepository.save(smpl);
    }));

    SampleSearchParameters searchParameters = new SampleSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);
    searchParameters.setApprovalState(Collections.singletonList(ApprovalState.APPROVED));

    PagedItemsView<SampleView> result = sampleService.search(searchParameters);
    assertEquals(2, result.getTotalItems());
    assertEquals(2, result.getItems().size());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
    assertEquals("AQ-001", result.getItems().get(0).getSample());
    assertEquals("AQ-002", result.getItems().get(1).getSample());
  }

  @Test
  public void testSearchByArea() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    String bbox = "25.97845409376835,38.68356055251447,25.980514030291786,38.68503454336369";
    SampleSearchParameters searchParameters = new SampleSearchParameters();
    searchParameters.setBbox(bbox);

    PagedItemsView<SampleView> pagedItemsView = sampleService.search(searchParameters);
    assertEquals(1, pagedItemsView.getTotalItems());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity expectedSample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(sample -> sample.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      List<SampleView> sampleViews = pagedItemsView.getItems();
      assertEquals(1, sampleViews.size());

      SampleView sampleView = sampleViews.get(0);
      assertEquals(expectedSample.getImlgs(), sampleView.getImlgs());
      assertEquals(expectedSample.getCruise().getCruiseName(), sampleView.getCruise());
      assertEquals(expectedSample.getCruiseFacility().getFacility().getFacilityCode(), sampleView.getFacilityCode());
      assertEquals(expectedSample.getCruisePlatform().getPlatform().getPlatform(), sampleView.getPlatform());
      assertEquals(expectedSample.getLeg().getLegName(), sampleView.getLeg());
      assertEquals(expectedSample.getLat(), sampleView.getLat());
      assertEquals(expectedSample.getLon(), sampleView.getLon());
      assertEquals(expectedSample.getDevice().getDeviceCode(), sampleView.getDeviceCode());
      assertEquals(expectedSample.getSample(), sampleView.getSample());
    });
  }

  @Test
  public void testReviewSample() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    CuratorsSampleTsqpEntity sampleEntity = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sample.setApproval(approval);

      return curatorsSampleTsqpRepository.save(sample);
    });
    assertNotNull(sampleEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.APPROVED);
    approvalView.setComment("Looks good to me");
    SampleView view = sampleService.updateApproval(approvalView, sampleEntity.getImlgs());
    assertEquals(ApprovalState.APPROVED, view.getApprovalState());

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));
      assertEquals(ApprovalState.APPROVED, sample.getApproval().getApprovalState());
      assertEquals("Looks good to me", sample.getApproval().getComment());
    });
  }

  @Test
  public void testReviewSampleCruiseNotApproved() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    txTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruise = curatorsCruiseRepository.findByCruiseNameAndYear("AQ-10", (short) 2021).orElseThrow(
          () -> new IllegalStateException("Cruise does not exist: AQ-10")
      );

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      cruise.setApproval(approval);
      curatorsCruiseRepository.save(cruise);
    });

    CuratorsSampleTsqpEntity sampleEntity = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sample.setApproval(approval);

      return curatorsSampleTsqpRepository.save(sample);
    });
    assertNotNull(sampleEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.APPROVED);
    approvalView.setComment("Looks good to me");

    ApiException exception = assertThrows(ApiException.class, () -> sampleService.updateApproval(approvalView, sampleEntity.getImlgs()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Cruise AQ-10 (2021) is not approved", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewSampleRevokeApproval() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    CuratorsSampleTsqpEntity sampleEntity = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      sample.getIntervals().forEach(interval -> {
        interval.setPublish(true);
        GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
        approval.setApprovalState(ApprovalState.APPROVED);
        interval.setApproval(approval);
        curatorsIntervalRepository.save(interval);
      });

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      sample.setApproval(approval);

      sample.setPublish(true);
      return curatorsSampleTsqpRepository.save(sample);
    });
    assertNotNull(sampleEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.REJECTED);
    approvalView.setComment("There are some issues here");
    SampleView view = sampleService.updateApproval(approvalView, sampleEntity.getImlgs());
    assertEquals(ApprovalState.REJECTED, view.getApprovalState());

    txTemplate.executeWithoutResult(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));
      assertEquals(ApprovalState.REJECTED, sample.getApproval().getApprovalState());
      assertEquals("There are some issues here", sample.getApproval().getComment());
      assertFalse(sample.isPublish());

      sample.getIntervals().forEach(
          interval -> {
            assertFalse(interval.isPublish());
            assertEquals(ApprovalState.REJECTED, interval.getApproval().getApprovalState());
            assertEquals("Sample rejected", interval.getApproval().getComment());
          }
      );
    });
  }
  @Test
  public void testReviewSampleRevokeApprovalNoChildApprovals() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    CuratorsSampleTsqpEntity sampleEntity = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      sample.getIntervals().forEach(interval -> {
        interval.setPublish(true);
        curatorsIntervalRepository.save(interval);
      });

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      sample.setApproval(approval);

      sample.setPublish(true);
      return curatorsSampleTsqpRepository.save(sample);
    });
    assertNotNull(sampleEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.REJECTED);
    approvalView.setComment("There are some issues here");
    ApiException exception = assertThrows(ApiException.class, () -> sampleService.updateApproval(approvalView, sampleEntity.getImlgs()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Cannot update non-existent approval", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewSampleChangeToPending() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    String imlgs = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      sample.setApproval(approval);
      return curatorsSampleTsqpRepository.save(sample).getImlgs();
    });
    assertNotNull(imlgs);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);

    ApiException exception = assertThrows(ApiException.class, () -> sampleService.updateApproval(approvalView, imlgs));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals("Cannot change approved item to pending", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdatePublishStatusSampleNotApprovedPatch() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    CuratorsSampleTsqpEntity sampleEntity = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sample.setApproval(approval);

      return curatorsSampleTsqpRepository.save(sample);
    });
    assertNotNull(sampleEntity);

    SampleView sampleView = new SampleView();
    sampleView.setPublish(true);
    sampleView.setImlgs(sampleEntity.getImlgs());

    SimpleItemsView<SampleView> simpleItemsView = new SimpleItemsView<>();
    simpleItemsView.setItems(Collections.singletonList(sampleView));
    ApiException exception = assertThrows(ApiException.class ,() -> sampleService.patch(simpleItemsView));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Sample %s is not approved", sampleEntity.getImlgs()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdatePublishStatusSampleNotApprovedPost() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    SampleView sampleView = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      sample.setApproval(approval);
      sample.setPublish(false);

      sample = curatorsSampleTsqpRepository.save(sample);

      SampleView view = new SampleView();
      view.setImlgs(sample.getImlgs());
      view.setPublish(true);
      view.setFacilityCode(sample.getCruiseFacility().getFacility().getFacilityCode());
      view.setCruise(sample.getCruiseFacility().getCruise().getCruiseName());
      view.setSample(sample.getSample());
      view.setPlatform(sample.getCruisePlatform().getPlatform().getPlatform());
      view.setDeviceCode(sample.getDevice().getDeviceCode());
      view.setBeginDate(sample.getBeginDate());
      view.setEndDate(sample.getEndDate());
      view.setLat(sample.getLat());
      view.setEndLat(sample.getEndLat());
      view.setLon(sample.getLon());
      view.setEndLon(sample.getEndLon());
      view.setWaterDepth(sample.getWaterDepth());
      view.setEndWaterDepth(sample.getEndWaterDepth());
      view.setStorageMethCode(sample.getStorageMeth().getStorageMethCode());
      view.setCoredLength(Double.valueOf(sample.getCoredLength()));
      view.setCoredDiam(Double.valueOf(sample.getCoredDiam()));
      view.setPi(sample.getPi());
      view.setProvinceCode(sample.getProvince().getProvinceCode());
      view.setLake(sample.getLake());
      view.setIgsn(sample.getIgsn());
      view.setLeg(sample.getLeg().getLegName());
      view.setSampleComments(sample.getSampleComments());
      view.setShowSampl(sample.getShowSampl());
      return view;
    });
    assertNotNull(sampleView);

    ApiException exception = assertThrows(ApiException.class ,() -> sampleService.update(sampleView, sampleView.getImlgs()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Sample %s is not approved", sampleView.getImlgs()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApproval() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    String imlgs = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleTsqpEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      approval.setComment("Test comment");
      sampleTsqpEntity.setApproval(approval);

      return curatorsSampleTsqpRepository.save(sampleTsqpEntity).getId();
    });

    ApprovalView approvalView = sampleService.getApproval(imlgs);
    assertEquals(ApprovalState.PENDING, approvalView.getApprovalState());
    assertEquals("Test comment", approvalView.getComment());
  }

  @Test
  public void testGetApprovalNotFound() throws Exception {
    createCruise("AQ-10", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile();

    String imlgs = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleTsqpEntity = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-001"))
          .findFirst().orElseThrow(() -> new IllegalStateException("Sample does not exist: AQ-001"));

      return sampleTsqpEntity.getId();
    });

    ApiException exception = assertThrows(ApiException.class ,() -> sampleService.getApproval(imlgs));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Approval does not exist", exception.getApiError().getFlashErrors().get(0));
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
