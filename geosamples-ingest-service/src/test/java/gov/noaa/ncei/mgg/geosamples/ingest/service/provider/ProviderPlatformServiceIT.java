package gov.noaa.ncei.mgg.geosamples.ingest.service.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ProviderPlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ProviderPlatformServiceIT {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private ProviderPlatformService providerPlatformService;

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreateProviderPlatform() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformView platformView = new PlatformView();
    platformView.setPlatform("TST");
    platformView.setMasterId(1);
    platformView.setPrefix("TST");
    platformView.setIcesCode("TST");
    platformView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ProviderPlatformView resultView = providerPlatformService.create(platformView, authentication);

    assertNotNull(resultView.getId());
    assertEquals(platformView.getPlatform(), resultView.getPlatform());
    assertEquals(platformView.getMasterId(), resultView.getMasterId());
    assertEquals(platformView.getPrefix(), resultView.getPrefix());
    assertEquals(platformView.getIcesCode(), resultView.getIcesCode());
    assertEquals(platformView.getSourceUri(), resultView.getSourceUri());

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity resultEntity = platformMasterRepository.findById(resultView.getId()).orElseThrow(
          () -> new RuntimeException("Platform not found")
      );

      assertEquals(resultView.getId(), resultEntity.getId());
      assertEquals(resultView.getPlatform(), resultEntity.getPlatform());
      assertEquals(resultView.getMasterId(), resultEntity.getMasterId());
      assertEquals(resultView.getPrefix(), resultEntity.getPrefix());
      assertEquals(resultView.getIcesCode(), resultEntity.getIcesCode());
      assertEquals(resultView.getSourceUri(), resultEntity.getSourceUri());
      assertEquals(ApprovalState.PENDING, resultEntity.getApproval().getApprovalState());

      GeosamplesUserEntity resultUserEntity = resultEntity.getCreatedBy();
      assertEquals(userEntity.getId(), resultUserEntity.getId());
      assertEquals(userEntity.getUserName(), resultUserEntity.getUserName());
      assertEquals(userEntity.getDisplayName(), resultUserEntity.getDisplayName());

    });
  }

  @Test
  public void testCreateProviderPlatformConflict() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platformMasterRepository.save(platform);
    });

    PlatformView platformView = new PlatformView();
    platformView.setPlatform("TST");
    platformView.setMasterId(1);
    platformView.setPrefix("TST");
    platformView.setIcesCode("TST");
    platformView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    assertThrows(DataIntegrityViolationException.class, () -> providerPlatformService.create(platformView, authentication));
  }

  @Test
  public void testCreateProviderPlatformNoUser() {
    PlatformView platformView = new PlatformView();
    platformView.setPlatform("TST");
    platformView.setMasterId(1);
    platformView.setPrefix("TST");
    platformView.setIcesCode("TST");
    platformView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("gabby");
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.create(platformView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User not found: gabby", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApproval() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      approval.setComment("comment");
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApprovalView approvalView = providerPlatformService.getApproval(platformMaster.getId(), authentication);
    assertEquals(ApprovalState.PENDING, approvalView.getApprovalState());
    assertEquals("comment", approvalView.getComment());
  }

  @Test
  public void testGetApprovalNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.getApproval(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Approval does not exist", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetApprovalPlatformDoesNotBelongToUser() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      approval.setComment("comment");
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.getApproval(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderPlatform() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ProviderPlatformView resultView = providerPlatformService.get(platformMaster.getId(), authentication);

    assertEquals(platformMaster.getId(), resultView.getId());
    assertEquals(platformMaster.getPlatform(), resultView.getPlatform());
    assertEquals(platformMaster.getMasterId(), resultView.getMasterId());
    assertEquals(platformMaster.getPrefix(), resultView.getPrefix());
    assertEquals(platformMaster.getIcesCode(), resultView.getIcesCode());
    assertEquals(platformMaster.getSourceUri(), resultView.getSourceUri());
  }

  @Test
  public void testGetProviderPlatformPlatformHasNoUser() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.get(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderPlatformNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.get(100000000000L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testGetProviderPlatformPlatformDoesNotBelongToUser() {
    GeosamplesUserEntity userEntity1 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity1);

    GeosamplesUserEntity userEntity2 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby2");
      geosamplesUserEntity.setDisplayName("Gabby2");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity1.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.get(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchProviderPlatformBelongingToUser() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster1);

    PlatformMasterEntity platformMaster2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      platform.setMasterId(2);
      platform.setPrefix("TST2");
      platform.setIcesCode("TST2");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster2);

    PlatformMasterEntity platformMaster3 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST3");
      platform.setMasterId(3);
      platform.setPrefix("TST3");
      platform.setIcesCode("TST3");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster3);

    PlatformMasterEntity platformMaster4 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST4");
      platform.setMasterId(4);
      platform.setPrefix("TST4");
      platform.setIcesCode("TST4");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster4);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderPlatformSearchParameters searchParameters = new ProviderPlatformSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(4);
    searchParameters.setOrder(Collections.singletonList("platform:asc"));
    searchParameters.setPlatform(Collections.singletonList("TST"));

    PagedItemsView<PlatformView> pagedItemsView = providerPlatformService.search(searchParameters, authentication);
    assertEquals(1, pagedItemsView.getPage());
    assertEquals(4, pagedItemsView.getItemsPerPage());
    assertEquals(4, pagedItemsView.getTotalItems());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(4, pagedItemsView.getItems().size());

    assertEquals(platformMaster1.getId(), pagedItemsView.getItems().get(0).getId());
    assertEquals(platformMaster1.getPlatform(), pagedItemsView.getItems().get(0).getPlatform());
    assertEquals(platformMaster1.getMasterId(), pagedItemsView.getItems().get(0).getMasterId());
    assertEquals(platformMaster1.getPrefix(), pagedItemsView.getItems().get(0).getPrefix());
    assertEquals(platformMaster1.getIcesCode(), pagedItemsView.getItems().get(0).getIcesCode());
    assertEquals(platformMaster1.getSourceUri(), pagedItemsView.getItems().get(0).getSourceUri());

    assertEquals(platformMaster2.getId(), pagedItemsView.getItems().get(1).getId());
    assertEquals(platformMaster2.getPlatform(), pagedItemsView.getItems().get(1).getPlatform());
    assertEquals(platformMaster2.getMasterId(), pagedItemsView.getItems().get(1).getMasterId());
    assertEquals(platformMaster2.getPrefix(), pagedItemsView.getItems().get(1).getPrefix());
    assertEquals(platformMaster2.getIcesCode(), pagedItemsView.getItems().get(1).getIcesCode());
    assertEquals(platformMaster2.getSourceUri(), pagedItemsView.getItems().get(1).getSourceUri());

    assertEquals(platformMaster3.getId(), pagedItemsView.getItems().get(2).getId());
    assertEquals(platformMaster3.getPlatform(), pagedItemsView.getItems().get(2).getPlatform());
    assertEquals(platformMaster3.getMasterId(), pagedItemsView.getItems().get(2).getMasterId());
    assertEquals(platformMaster3.getPrefix(), pagedItemsView.getItems().get(2).getPrefix());
    assertEquals(platformMaster3.getIcesCode(), pagedItemsView.getItems().get(2).getIcesCode());
    assertEquals(platformMaster3.getSourceUri(), pagedItemsView.getItems().get(2).getSourceUri());

    assertEquals(platformMaster4.getId(), pagedItemsView.getItems().get(3).getId());
    assertEquals(platformMaster4.getPlatform(), pagedItemsView.getItems().get(3).getPlatform());
    assertEquals(platformMaster4.getMasterId(), pagedItemsView.getItems().get(3).getMasterId());
    assertEquals(platformMaster4.getPrefix(), pagedItemsView.getItems().get(3).getPrefix());
    assertEquals(platformMaster4.getIcesCode(), pagedItemsView.getItems().get(3).getIcesCode());
    assertEquals(platformMaster4.getSourceUri(), pagedItemsView.getItems().get(3).getSourceUri());
  }

  @Test
  public void testSearchProviderPlatformNoResults() {
    GeosamplesUserEntity userEntity1 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity1);

    GeosamplesUserEntity userEntity2 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby2");
      geosamplesUserEntity.setDisplayName("Gabby2");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });

    PlatformMasterEntity platformMaster1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster1);

    PlatformMasterEntity platformMaster2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      platform.setMasterId(2);
      platform.setPrefix("TST2");
      platform.setIcesCode("TST2");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster2);

    PlatformMasterEntity platformMaster3 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST3");
      platform.setMasterId(3);
      platform.setPrefix("TST3");
      platform.setIcesCode("TST3");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster3);

    PlatformMasterEntity platformMaster4 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST4");
      platform.setMasterId(4);
      platform.setPrefix("TST4");
      platform.setIcesCode("TST4");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster4);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity1.getUserName());

    ProviderPlatformSearchParameters searchParameters = new ProviderPlatformSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(4);
    searchParameters.setOrder(Collections.singletonList("platform:asc"));
    searchParameters.setPlatform(Collections.singletonList("TST"));

    PagedItemsView<PlatformView> pagedItemsView = providerPlatformService.search(searchParameters, authentication);
    assertEquals(1, pagedItemsView.getPage());
    assertEquals(4, pagedItemsView.getItemsPerPage());
    assertEquals(0, pagedItemsView.getTotalItems());
    assertEquals(0, pagedItemsView.getTotalPages());
    assertEquals(0, pagedItemsView.getItems().size());
  }

  @Test
  public void testSearchProviderPlatformMixedResults() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster1 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster1);

    PlatformMasterEntity platformMaster2 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      platform.setMasterId(2);
      platform.setPrefix("TST2");
      platform.setIcesCode("TST2");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster2);

    PlatformMasterEntity platformMaster3 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST3");
      platform.setMasterId(3);
      platform.setPrefix("TST3");
      platform.setIcesCode("TST3");
      platform.setSourceUri("http://example.com");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster3);

    PlatformMasterEntity platformMaster4 = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST4");
      platform.setMasterId(4);
      platform.setPrefix("TST4");
      platform.setIcesCode("TST4");
      platform.setSourceUri("http://example.com");
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster4);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderPlatformSearchParameters searchParameters = new ProviderPlatformSearchParameters();
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(4);
    searchParameters.setOrder(Collections.singletonList("platform:asc"));
    searchParameters.setPlatform(Collections.singletonList("TST"));

    PagedItemsView<PlatformView> pagedItemsView = providerPlatformService.search(searchParameters, authentication);
    assertEquals(1, pagedItemsView.getPage());
    assertEquals(4, pagedItemsView.getItemsPerPage());
    assertEquals(4, pagedItemsView.getTotalItems());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(4, pagedItemsView.getItems().size());

    assertEquals(platformMaster1.getId(), pagedItemsView.getItems().get(0).getId());
    assertEquals(platformMaster1.getPlatform(), pagedItemsView.getItems().get(0).getPlatform());
    assertEquals(platformMaster1.getMasterId(), pagedItemsView.getItems().get(0).getMasterId());
    assertEquals(platformMaster1.getPrefix(), pagedItemsView.getItems().get(0).getPrefix());
    assertEquals(platformMaster1.getIcesCode(), pagedItemsView.getItems().get(0).getIcesCode());
    assertEquals(platformMaster1.getSourceUri(), pagedItemsView.getItems().get(0).getSourceUri());

    assertEquals(platformMaster2.getId(), pagedItemsView.getItems().get(1).getId());
    assertEquals(platformMaster2.getPlatform(), pagedItemsView.getItems().get(1).getPlatform());
    assertEquals(platformMaster2.getMasterId(), pagedItemsView.getItems().get(1).getMasterId());
    assertEquals(platformMaster2.getPrefix(), pagedItemsView.getItems().get(1).getPrefix());
    assertEquals(platformMaster2.getIcesCode(), pagedItemsView.getItems().get(1).getIcesCode());
    assertEquals(platformMaster2.getSourceUri(), pagedItemsView.getItems().get(1).getSourceUri());

    assertEquals(platformMaster3.getId(), pagedItemsView.getItems().get(2).getId());
    assertEquals(platformMaster3.getPlatform(), pagedItemsView.getItems().get(2).getPlatform());
    assertEquals(platformMaster3.getMasterId(), pagedItemsView.getItems().get(2).getMasterId());
    assertEquals(platformMaster3.getPrefix(), pagedItemsView.getItems().get(2).getPrefix());
    assertEquals(platformMaster3.getIcesCode(), pagedItemsView.getItems().get(2).getIcesCode());
    assertEquals(platformMaster3.getSourceUri(), pagedItemsView.getItems().get(2).getSourceUri());

    assertEquals(platformMaster4.getId(), pagedItemsView.getItems().get(3).getId());
    assertEquals(platformMaster4.getPlatform(), pagedItemsView.getItems().get(3).getPlatform());
    assertEquals(platformMaster4.getMasterId(), pagedItemsView.getItems().get(3).getMasterId());
    assertEquals(platformMaster4.getPrefix(), pagedItemsView.getItems().get(3).getPrefix());
    assertEquals(platformMaster4.getIcesCode(), pagedItemsView.getItems().get(3).getIcesCode());
    assertEquals(platformMaster4.getSourceUri(), pagedItemsView.getItems().get(3).getSourceUri());
  }

  @Test
  public void testUpdateProviderPlatform() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ProviderPlatformView resultView = providerPlatformService.update(platformMaster.getId(), updateView, authentication);

    assertEquals(updateView.getId(), resultView.getId());
    assertNotEquals(updateView.getPlatform(), resultView.getPlatform());
    assertEquals(platformMaster.getPlatform(), resultView.getPlatform());
    assertEquals(updateView.getMasterId(), resultView.getMasterId());
    assertEquals(updateView.getPrefix(), resultView.getPrefix());
    assertEquals(updateView.getIcesCode(), resultView.getIcesCode());
    assertEquals(updateView.getSourceUri(), resultView.getSourceUri());

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity resultEntity = platformMasterRepository.findById(platformMaster.getId()).orElseThrow(
          () -> new RuntimeException("Platform not found")
      );
      assertEquals(updateView.getId(), resultEntity.getId());
      assertNotEquals(updateView.getPlatform(), resultEntity.getPlatform());
      assertEquals(resultView.getPlatform(), resultEntity.getPlatform());
      assertEquals(updateView.getMasterId(), resultEntity.getMasterId());
      assertEquals(updateView.getPrefix(), resultEntity.getPrefix());
      assertEquals(updateView.getIcesCode(), resultEntity.getIcesCode());
      assertEquals(updateView.getSourceUri(), resultEntity.getSourceUri());
      assertEquals(ApprovalState.PENDING, resultEntity.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderPlatformFromRejected() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.REJECTED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ProviderPlatformView resultView = providerPlatformService.update(platformMaster.getId(), updateView, authentication);

    assertEquals(updateView.getId(), resultView.getId());
    assertNotEquals(updateView.getPlatform(), resultView.getPlatform());
    assertEquals(platformMaster.getPlatform(), resultView.getPlatform());
    assertEquals(updateView.getMasterId(), resultView.getMasterId());
    assertEquals(updateView.getPrefix(), resultView.getPrefix());
    assertEquals(updateView.getIcesCode(), resultView.getIcesCode());
    assertEquals(updateView.getSourceUri(), resultView.getSourceUri());

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity resultEntity = platformMasterRepository.findById(platformMaster.getId()).orElseThrow(
          () -> new RuntimeException("Platform not found")
      );
      assertEquals(updateView.getId(), resultEntity.getId());
      assertNotEquals(updateView.getPlatform(), resultEntity.getPlatform());
      assertEquals(resultView.getPlatform(), resultEntity.getPlatform());
      assertEquals(updateView.getMasterId(), resultEntity.getMasterId());
      assertEquals(updateView.getPrefix(), resultEntity.getPrefix());
      assertEquals(updateView.getIcesCode(), resultEntity.getIcesCode());
      assertEquals(updateView.getSourceUri(), resultEntity.getSourceUri());
      assertEquals(ApprovalState.PENDING, resultEntity.getApproval().getApprovalState());
    });
  }

  @Test
  public void testUpdateProviderPlatformApproved() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.update(platformMaster.getId(), updateView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderPlatformNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(100000000000000000L);
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.update(updateView.getId(), updateView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderPlatformPlatformDoesNotBelongToUser() {
    GeosamplesUserEntity userEntity1 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity1);

    GeosamplesUserEntity userEntity2 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby2");
      geosamplesUserEntity.setDisplayName("Gabby2");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity2);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity1.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.update(platformMaster.getId(), updateView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderPlatformCannotUpdateNoUser() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.update(platformMaster.getId(), updateView, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testUpdateProviderPlatformCannotUpdateNoApproval() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);
      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    ProviderPlatformView updateView = new ProviderPlatformView();
    updateView.setId(platformMaster.getId());
    updateView.setPlatform("TST2"); // not updatable
    updateView.setMasterId(2);
    updateView.setPrefix("TST2");
    updateView.setIcesCode("TST2");
    updateView.setSourceUri("http://example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());
    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.update(platformMaster.getId(), updateView, authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderPlatform() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ProviderPlatformView resultView = providerPlatformService.delete(platformMaster.getId(), authentication);
    assertEquals(platformMaster.getId(), resultView.getId());
    assertEquals(platformMaster.getPlatform(), resultView.getPlatform());
    assertEquals(platformMaster.getMasterId(), resultView.getMasterId());
    assertEquals(platformMaster.getPrefix(), resultView.getPrefix());
    assertEquals(platformMaster.getIcesCode(), resultView.getIcesCode());
    assertEquals(platformMaster.getSourceUri(), resultView.getSourceUri());

    assertFalse(platformMasterRepository.findById(platformMaster.getId()).isPresent());
  }

  @Test
  public void testDeleteProviderPlatformNotFound() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.delete(100000000L, authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderPlatformPlatformDoesNotBelongToUser() {
    GeosamplesUserEntity userEntity1 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity1);

    GeosamplesUserEntity userEntity2 = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby2");
      geosamplesUserEntity.setDisplayName("Gabby2");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity2);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity2);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity1.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.delete(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderPlatformApproved() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.delete(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderPlatformNoUser() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.delete(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteProviderPlatformNoApprovalState() {
    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUserEntity = new GeosamplesUserEntity();
      geosamplesUserEntity.setUserName("gabby");
      geosamplesUserEntity.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUserEntity);
    });
    assertNotNull(userEntity);

    PlatformMasterEntity platformMaster = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setMasterId(1);
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("http://example.com");
      platform.setCreatedBy(userEntity);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformMaster);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEntity.getUserName());

    ApiException exception = assertThrows(ApiException.class, () -> providerPlatformService.delete(platformMaster.getId(), authentication));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("User cannot update", exception.getApiError().getFlashErrors().get(0));
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(status -> {
      platformMasterRepository.findByPlatformNormalized("TST").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST2").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST3").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST4").ifPresent(platformMasterRepository::delete);
      geosamplesUserRepository.findById("gabby").ifPresent(geosamplesUserRepository::delete);
      geosamplesUserRepository.findById("gabby2").ifPresent(geosamplesUserRepository::delete);
    });
  }

}
