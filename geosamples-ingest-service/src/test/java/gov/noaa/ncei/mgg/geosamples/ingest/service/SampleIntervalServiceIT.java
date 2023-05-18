package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
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
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
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

  @Test
  public void testUpdateSampleIntervalPublishStatusIntervalNotApproved() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile("imlgs_sample_good_full.xlsm");

    List<CombinedSampleIntervalView> views = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleTsqp = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      GeosamplesApprovalEntity sampleApproval = new GeosamplesApprovalEntity();
      sampleApproval.setApprovalState(ApprovalState.APPROVED);
      sampleTsqp.setApproval(sampleApproval);
      sampleTsqp.setPublish(false);
      sampleTsqp = curatorsSampleTsqpRepository.save(sampleTsqp);

      CuratorsIntervalEntity interval1 = sampleTsqp.getIntervals().get(0);
      GeosamplesApprovalEntity intervalApproval1 = new GeosamplesApprovalEntity();
      intervalApproval1.setApprovalState(ApprovalState.PENDING);
      interval1.setApproval(intervalApproval1);
      interval1.setPublish(false);
      curatorsIntervalRepository.save(interval1);

      CuratorsIntervalEntity interval2 = sampleTsqp.getIntervals().get(1);
      GeosamplesApprovalEntity intervalApproval2 = new GeosamplesApprovalEntity();
      intervalApproval2.setApprovalState(ApprovalState.APPROVED);
      interval2.setApproval(intervalApproval2);
      interval2.setPublish(false);
      curatorsIntervalRepository.save(interval2);

      sampleTsqp = curatorsSampleTsqpRepository.save(sampleTsqp);

      CombinedSampleIntervalView view1 = new CombinedSampleIntervalView();
      view1.setCruise(sampleTsqp.getCruise().getCruiseName());
      view1.setSample(sampleTsqp.getSample());
      view1.setFacility(sampleTsqp.getCruiseFacility().getFacility().getFacility());
      view1.setPlatform(sampleTsqp.getCruisePlatform().getPlatform().getPlatform());
      view1.setDevice(sampleTsqp.getDevice().getDevice());
      view1.setBeginDate(sampleTsqp.getBeginDate());
      view1.setEndDate(sampleTsqp.getEndDate());
      view1.setLat(sampleTsqp.getLat());
      view1.setEndLat(sampleTsqp.getEndLat());
      view1.setLon(sampleTsqp.getLon());
      view1.setEndLon(sampleTsqp.getEndLon());
      view1.setWaterDepth(sampleTsqp.getWaterDepth());
      view1.setEndWaterDepth(sampleTsqp.getEndWaterDepth());
      view1.setStorageMeth(sampleTsqp.getStorageMeth().getStorageMeth());
      view1.setCoredLength(sampleTsqp.getCoredLength());
      view1.setCoredLengthMm(sampleTsqp.getCoredLengthMm());
      view1.setCoredDiam(sampleTsqp.getCoredDiam());
      view1.setCoredDiamMm(sampleTsqp.getCoredDiamMm());
      view1.setPi(sampleTsqp.getPi());
      view1.setSampleLake(sampleTsqp.getLake());
      view1.setLastUpdate(sampleTsqp.getLastUpdate());
      view1.setIgsn(sampleTsqp.getIgsn());
      view1.setSampleComments(sampleTsqp.getSampleComments());
      view1.setShowSampl(sampleTsqp.getShowSampl());
      view1.setImlgs(sampleTsqp.getImlgs());

      view1.setInterval(interval1.getInterval());
      view1.setIntervalId(interval1.getId());
      view1.setDepthBot(interval1.getDepthBot());
      view1.setDepthBotMm(interval1.getDepthBotMm());
      view1.setDepthTop(interval1.getDepthTop());
      view1.setDepthTopMm(interval1.getDepthTopMm());
      view1.setDhCoreId(interval1.getDhCoreId());
      view1.setDhCoreLength(interval1.getDhCoreLength());
      view1.setDhCoreLengthMm(interval1.getDhCoreLengthMm());
      view1.setDhCoreInterval(interval1.getDhCoreInterval());
      view1.setdTopInDhCore(interval1.getdTopInDhCore());
      view1.setdTopMmInDhCore(interval1.getdTopMmInDhCore());
      view1.setdBotInDhCore(interval1.getdBotInDhCore());
      view1.setdBotMmInDhCore(interval1.getdBotMmInDhCore());
      view1.setLith1(interval1.getLith1().getLithologyCode());
      view1.setDescription(interval1.getDescription());
      view1.setAges(interval1.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view1.setAbsoluteAgeTop(interval1.getAbsoluteAgeTop());
      view1.setAbsoluteAgeBot(interval1.getAbsoluteAgeBot());
      view1.setWeight(interval1.getWeight());
      view1.setMunsellCode(interval1.getMunsellCode());
      view1.setMunsell(interval1.getMunsell());
      view1.setExhaustCode(interval1.getExhaustCode());
      view1.setPhotoLink(interval1.getPhotoLink());
      view1.setIntervalLake(interval1.getLake());
      view1.setUnitNumber(interval1.getUnitNumber());
      view1.setIntComments(interval1.getIntComments());
      view1.setDhDevice(interval1.getDhDevice());
      view1.setCmcdTop(interval1.getCmcdTop());
      view1.setCmcdBot(interval1.getCmcdBot());
      view1.setMmcdBot(interval1.getMmcdBot());
      view1.setMmcdTop(interval1.getMmcdTop());
      view1.setIntervalIgsn(interval1.getIgsn());
      view1.setIntervalParentIsgn(interval1.getSample().getIgsn());

      view1.setPublish(true);

      CombinedSampleIntervalView view2 = new CombinedSampleIntervalView();
      view2.setCruise(sampleTsqp.getCruise().getCruiseName());
      view2.setSample(sampleTsqp.getSample());
      view2.setFacility(sampleTsqp.getCruiseFacility().getFacility().getFacility());
      view2.setPlatform(sampleTsqp.getCruisePlatform().getPlatform().getPlatform());
      view2.setDevice(sampleTsqp.getDevice().getDevice());
      view2.setBeginDate(sampleTsqp.getBeginDate());
      view2.setEndDate(sampleTsqp.getEndDate());
      view2.setLat(sampleTsqp.getLat());
      view2.setEndLat(sampleTsqp.getEndLat());
      view2.setLon(sampleTsqp.getLon());
      view2.setEndLon(sampleTsqp.getEndLon());
      view2.setWaterDepth(sampleTsqp.getWaterDepth());
      view2.setEndWaterDepth(sampleTsqp.getEndWaterDepth());
      view2.setStorageMeth(sampleTsqp.getStorageMeth().getStorageMeth());
      view2.setCoredLength(sampleTsqp.getCoredLength());
      view2.setCoredLengthMm(sampleTsqp.getCoredLengthMm());
      view2.setCoredDiam(sampleTsqp.getCoredDiam());
      view2.setCoredDiamMm(sampleTsqp.getCoredDiamMm());
      view2.setPi(sampleTsqp.getPi());
      view2.setSampleLake(sampleTsqp.getLake());
      view2.setLastUpdate(sampleTsqp.getLastUpdate());
      view2.setIgsn(sampleTsqp.getIgsn());
      view2.setSampleComments(sampleTsqp.getSampleComments());
      view2.setShowSampl(sampleTsqp.getShowSampl());
      view2.setImlgs(sampleTsqp.getImlgs());

      view2.setInterval(interval2.getInterval());
      view2.setIntervalId(interval2.getId());
      view2.setDepthBot(interval2.getDepthBot());
      view2.setDepthBotMm(interval2.getDepthBotMm());
      view2.setDepthTop(interval2.getDepthTop());
      view2.setDepthTopMm(interval2.getDepthTopMm());
      view2.setDhCoreId(interval2.getDhCoreId());
      view2.setDhCoreLength(interval2.getDhCoreLength());
      view2.setDhCoreLengthMm(interval2.getDhCoreLengthMm());
      view2.setDhCoreInterval(interval2.getDhCoreInterval());
      view2.setdTopInDhCore(interval2.getdTopInDhCore());
      view2.setdTopMmInDhCore(interval2.getdTopMmInDhCore());
      view2.setdBotInDhCore(interval2.getdBotInDhCore());
      view2.setdBotMmInDhCore(interval2.getdBotMmInDhCore());
      view2.setLith1(interval2.getLith1().getLithologyCode());
      view2.setDescription(interval2.getDescription());
      view2.setAges(interval2.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view2.setAbsoluteAgeTop(interval2.getAbsoluteAgeTop());
      view2.setAbsoluteAgeBot(interval2.getAbsoluteAgeBot());
      view2.setWeight(interval2.getWeight());
      view2.setMunsellCode(interval2.getMunsellCode());
      view2.setMunsell(interval2.getMunsell());
      view2.setExhaustCode(interval2.getExhaustCode());
      view2.setPhotoLink(interval2.getPhotoLink());
      view2.setIntervalLake(interval2.getLake());
      view2.setUnitNumber(interval2.getUnitNumber());
      view2.setIntComments(interval2.getIntComments());
      view2.setDhDevice(interval2.getDhDevice());
      view2.setCmcdTop(interval2.getCmcdTop());
      view2.setCmcdBot(interval2.getCmcdBot());
      view2.setMmcdBot(interval2.getMmcdBot());
      view2.setMmcdTop(interval2.getMmcdTop());
      view2.setIntervalIgsn(interval2.getIgsn());
      view2.setIntervalParentIsgn(interval2.getSample().getIgsn());

      view2.setPublish(true);

      List<CombinedSampleIntervalView> sampleIntervalViews = new ArrayList<>();
      sampleIntervalViews.add(view1);
      sampleIntervalViews.add(view2);

      return sampleIntervalViews;
    });
    assertNotNull(views);

    SimpleItemsView<CombinedSampleIntervalView> simpleItemsView = new SimpleItemsView<>();
    simpleItemsView.getItems().addAll(views);
    ApiException exception = assertThrows(ApiException.class, () -> sampleIntervalService.patch(simpleItemsView));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Interval %s (%s) is not approved", views.get(0).getInterval(), views.get(0).getImlgs()), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateSampleIntervalPublishStatusSampleNotApproved() throws Exception {
    createCruise("AQ-10", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-11", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-12", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));
    createCruise("AQ-01", 2021, Collections.singletonList("GEOMAR"), Collections.singletonList("African Queen"), Arrays.asList("AQ-LEFT-LEG", "AQ-RIGHT-LEG"));


    uploadFile("imlgs_sample_good_full.xlsm");

    List<CombinedSampleIntervalView> views = txTemplate.execute(s -> {
      CuratorsSampleTsqpEntity sampleTsqp = curatorsSampleTsqpRepository.findAll().stream()
          .filter(smpl -> smpl.getSample().equals("AQ-01-01"))
          .findFirst().orElseThrow(
              () -> new RuntimeException("Sample AQ-01-01 not found")
          );

      GeosamplesApprovalEntity sampleApproval = new GeosamplesApprovalEntity();
      sampleApproval.setApprovalState(ApprovalState.PENDING);
      sampleTsqp.setApproval(sampleApproval);
      sampleTsqp.setPublish(false);
      sampleTsqp = curatorsSampleTsqpRepository.save(sampleTsqp);

      CuratorsIntervalEntity interval1 = sampleTsqp.getIntervals().get(0);
      GeosamplesApprovalEntity intervalApproval1 = new GeosamplesApprovalEntity();
      intervalApproval1.setApprovalState(ApprovalState.APPROVED);
      interval1.setApproval(intervalApproval1);
      interval1.setPublish(false);
      curatorsIntervalRepository.save(interval1);

      CuratorsIntervalEntity interval2 = sampleTsqp.getIntervals().get(1);
      GeosamplesApprovalEntity intervalApproval2 = new GeosamplesApprovalEntity();
      intervalApproval2.setApprovalState(ApprovalState.APPROVED);
      interval2.setApproval(intervalApproval2);
      interval2.setPublish(false);
      curatorsIntervalRepository.save(interval2);

      sampleTsqp = curatorsSampleTsqpRepository.save(sampleTsqp);

      CombinedSampleIntervalView view1 = new CombinedSampleIntervalView();
      view1.setCruise(sampleTsqp.getCruise().getCruiseName());
      view1.setSample(sampleTsqp.getSample());
      view1.setFacility(sampleTsqp.getCruiseFacility().getFacility().getFacility());
      view1.setPlatform(sampleTsqp.getCruisePlatform().getPlatform().getPlatform());
      view1.setDevice(sampleTsqp.getDevice().getDevice());
      view1.setBeginDate(sampleTsqp.getBeginDate());
      view1.setEndDate(sampleTsqp.getEndDate());
      view1.setLat(sampleTsqp.getLat());
      view1.setEndLat(sampleTsqp.getEndLat());
      view1.setLon(sampleTsqp.getLon());
      view1.setEndLon(sampleTsqp.getEndLon());
      view1.setWaterDepth(sampleTsqp.getWaterDepth());
      view1.setEndWaterDepth(sampleTsqp.getEndWaterDepth());
      view1.setStorageMeth(sampleTsqp.getStorageMeth().getStorageMeth());
      view1.setCoredLength(sampleTsqp.getCoredLength());
      view1.setCoredLengthMm(sampleTsqp.getCoredLengthMm());
      view1.setCoredDiam(sampleTsqp.getCoredDiam());
      view1.setCoredDiamMm(sampleTsqp.getCoredDiamMm());
      view1.setPi(sampleTsqp.getPi());
      view1.setSampleLake(sampleTsqp.getLake());
      view1.setLastUpdate(sampleTsqp.getLastUpdate());
      view1.setIgsn(sampleTsqp.getIgsn());
      view1.setSampleComments(sampleTsqp.getSampleComments());
      view1.setShowSampl(sampleTsqp.getShowSampl());
      view1.setImlgs(sampleTsqp.getImlgs());

      view1.setInterval(interval1.getInterval());
      view1.setIntervalId(interval1.getId());
      view1.setDepthBot(interval1.getDepthBot());
      view1.setDepthBotMm(interval1.getDepthBotMm());
      view1.setDepthTop(interval1.getDepthTop());
      view1.setDepthTopMm(interval1.getDepthTopMm());
      view1.setDhCoreId(interval1.getDhCoreId());
      view1.setDhCoreLength(interval1.getDhCoreLength());
      view1.setDhCoreLengthMm(interval1.getDhCoreLengthMm());
      view1.setDhCoreInterval(interval1.getDhCoreInterval());
      view1.setdTopInDhCore(interval1.getdTopInDhCore());
      view1.setdTopMmInDhCore(interval1.getdTopMmInDhCore());
      view1.setdBotInDhCore(interval1.getdBotInDhCore());
      view1.setdBotMmInDhCore(interval1.getdBotMmInDhCore());
      view1.setLith1(interval1.getLith1().getLithologyCode());
      view1.setDescription(interval1.getDescription());
      view1.setAges(interval1.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view1.setAbsoluteAgeTop(interval1.getAbsoluteAgeTop());
      view1.setAbsoluteAgeBot(interval1.getAbsoluteAgeBot());
      view1.setWeight(interval1.getWeight());
      view1.setMunsellCode(interval1.getMunsellCode());
      view1.setMunsell(interval1.getMunsell());
      view1.setExhaustCode(interval1.getExhaustCode());
      view1.setPhotoLink(interval1.getPhotoLink());
      view1.setIntervalLake(interval1.getLake());
      view1.setUnitNumber(interval1.getUnitNumber());
      view1.setIntComments(interval1.getIntComments());
      view1.setDhDevice(interval1.getDhDevice());
      view1.setCmcdTop(interval1.getCmcdTop());
      view1.setCmcdBot(interval1.getCmcdBot());
      view1.setMmcdBot(interval1.getMmcdBot());
      view1.setMmcdTop(interval1.getMmcdTop());
      view1.setIntervalIgsn(interval1.getIgsn());
      view1.setIntervalParentIsgn(interval1.getSample().getIgsn());

      view1.setPublish(true);

      CombinedSampleIntervalView view2 = new CombinedSampleIntervalView();
      view2.setCruise(sampleTsqp.getCruise().getCruiseName());
      view2.setSample(sampleTsqp.getSample());
      view2.setFacility(sampleTsqp.getCruiseFacility().getFacility().getFacility());
      view2.setPlatform(sampleTsqp.getCruisePlatform().getPlatform().getPlatform());
      view2.setDevice(sampleTsqp.getDevice().getDevice());
      view2.setBeginDate(sampleTsqp.getBeginDate());
      view2.setEndDate(sampleTsqp.getEndDate());
      view2.setLat(sampleTsqp.getLat());
      view2.setEndLat(sampleTsqp.getEndLat());
      view2.setLon(sampleTsqp.getLon());
      view2.setEndLon(sampleTsqp.getEndLon());
      view2.setWaterDepth(sampleTsqp.getWaterDepth());
      view2.setEndWaterDepth(sampleTsqp.getEndWaterDepth());
      view2.setStorageMeth(sampleTsqp.getStorageMeth().getStorageMeth());
      view2.setCoredLength(sampleTsqp.getCoredLength());
      view2.setCoredLengthMm(sampleTsqp.getCoredLengthMm());
      view2.setCoredDiam(sampleTsqp.getCoredDiam());
      view2.setCoredDiamMm(sampleTsqp.getCoredDiamMm());
      view2.setPi(sampleTsqp.getPi());
      view2.setSampleLake(sampleTsqp.getLake());
      view2.setLastUpdate(sampleTsqp.getLastUpdate());
      view2.setIgsn(sampleTsqp.getIgsn());
      view2.setSampleComments(sampleTsqp.getSampleComments());
      view2.setShowSampl(sampleTsqp.getShowSampl());
      view2.setImlgs(sampleTsqp.getImlgs());

      view2.setInterval(interval2.getInterval());
      view2.setIntervalId(interval2.getId());
      view2.setDepthBot(interval2.getDepthBot());
      view2.setDepthBotMm(interval2.getDepthBotMm());
      view2.setDepthTop(interval2.getDepthTop());
      view2.setDepthTopMm(interval2.getDepthTopMm());
      view2.setDhCoreId(interval2.getDhCoreId());
      view2.setDhCoreLength(interval2.getDhCoreLength());
      view2.setDhCoreLengthMm(interval2.getDhCoreLengthMm());
      view2.setDhCoreInterval(interval2.getDhCoreInterval());
      view2.setdTopInDhCore(interval2.getdTopInDhCore());
      view2.setdTopMmInDhCore(interval2.getdTopMmInDhCore());
      view2.setdBotInDhCore(interval2.getdBotInDhCore());
      view2.setdBotMmInDhCore(interval2.getdBotMmInDhCore());
      view2.setLith1(interval2.getLith1().getLithologyCode());
      view2.setDescription(interval2.getDescription());
      view2.setAges(interval2.getAges().stream().map(CuratorsAgeEntity::getAgeCode).collect(Collectors.toList()));
      view2.setAbsoluteAgeTop(interval2.getAbsoluteAgeTop());
      view2.setAbsoluteAgeBot(interval2.getAbsoluteAgeBot());
      view2.setWeight(interval2.getWeight());
      view2.setMunsellCode(interval2.getMunsellCode());
      view2.setMunsell(interval2.getMunsell());
      view2.setExhaustCode(interval2.getExhaustCode());
      view2.setPhotoLink(interval2.getPhotoLink());
      view2.setIntervalLake(interval2.getLake());
      view2.setUnitNumber(interval2.getUnitNumber());
      view2.setIntComments(interval2.getIntComments());
      view2.setDhDevice(interval2.getDhDevice());
      view2.setCmcdTop(interval2.getCmcdTop());
      view2.setCmcdBot(interval2.getCmcdBot());
      view2.setMmcdBot(interval2.getMmcdBot());
      view2.setMmcdTop(interval2.getMmcdTop());
      view2.setIntervalIgsn(interval2.getIgsn());
      view2.setIntervalParentIsgn(interval2.getSample().getIgsn());

      view2.setPublish(true);

      List<CombinedSampleIntervalView> sampleIntervalViews = new ArrayList<>();
      sampleIntervalViews.add(view1);
      sampleIntervalViews.add(view2);

      return sampleIntervalViews;
    });
    assertNotNull(views);

    SimpleItemsView<CombinedSampleIntervalView> simpleItemsView = new SimpleItemsView<>();
    simpleItemsView.getItems().addAll(views);
    ApiException exception = assertThrows(ApiException.class, () -> sampleIntervalService.patch(simpleItemsView));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(String.format("Sample %s is not approved", views.get(0).getImlgs()), exception.getApiError().getFlashErrors().get(0));
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
