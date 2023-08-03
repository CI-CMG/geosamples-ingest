package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class PlatformServiceIT {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private PlatformService platformService;

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testGetApproval() {
    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      approval.setComment("Pending approval");
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    ApprovalView approvalView = platformService.getApproval(platformEntity.getId());
    assertEquals(ApprovalState.PENDING, approvalView.getApprovalState());
    assertEquals("Pending approval", approvalView.getComment());
  }

  @Test
  public void tetGetApprovalNotFound() {
    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    ApiException exception = assertThrows(ApiException.class, () -> {
      platformService.getApproval(platformEntity.getId());
    });
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Approval does not exist", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewPlatform() {
    GeosamplesUserEntity user = transactionTemplate.execute(s -> {
      GeosamplesUserEntity geosamplesUser = new GeosamplesUserEntity();
      geosamplesUser.setUserName("gabby");
      geosamplesUser.setDisplayName("Gabby");
      return geosamplesUserRepository.save(geosamplesUser);
    });
    assertNotNull(user);

    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);
      platform.setCreatedBy(user);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.APPROVED);
    approvalView.setComment("Approved");

    PlatformView platformView = platformService.updateApproval(approvalView, platformEntity.getId());
    assertEquals(ApprovalState.APPROVED, platformView.getApprovalState());

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity platform = platformMasterRepository.findById(platformEntity.getId()).orElseThrow(
          () -> new RuntimeException("Platform not found")
      );

      assertEquals(ApprovalState.APPROVED, platform.getApproval().getApprovalState());
      assertEquals("Approved", platform.getApproval().getComment());

      assertNull(platform.getCreatedBy());
    });
  }

  @Test
  public void testReviewPlatformRevokeApproval() {
    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });
    assertNotNull(platformEntity);

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.REJECTED);
    approvalView.setComment("There's a problem here");

    ApiException exception = assertThrows(ApiException.class, () -> platformService.updateApproval(approvalView, platformEntity.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals("Cannot revoke approval for platform", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testReviewPlatformChangeToPending() {
    PlatformMasterEntity platformEntity = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });

    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);

    ApiException exception = assertThrows(ApiException.class, () -> platformService.updateApproval(approvalView, platformEntity.getId()));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals("Cannot change approved item to pending", exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchByApprovalState() {
    PlatformMasterEntity expectedPlatform = transactionTemplate.execute(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST");
      platform.setPrefix("TST");
      platform.setIcesCode("TST");
      platform.setSourceUri("TST");
      platform.setMasterId(1);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.APPROVED);
      platform.setApproval(approval);

      return platformMasterRepository.save(platform);
    });

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST2");
      platform.setPrefix("TST2");
      platform.setIcesCode("TST2");
      platform.setSourceUri("TST2");
      platform.setMasterId(2);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.PENDING);
      platform.setApproval(approval);

      platformMasterRepository.save(platform);
    });

    transactionTemplate.executeWithoutResult(s -> {
      PlatformMasterEntity platform = new PlatformMasterEntity();
      platform.setPlatform("TST3");
      platform.setPrefix("TST3");
      platform.setIcesCode("TST3");
      platform.setSourceUri("TST3");
      platform.setMasterId(3);

      GeosamplesApprovalEntity approval = new GeosamplesApprovalEntity();
      approval.setApprovalState(ApprovalState.REJECTED);
      platform.setApproval(approval);

      platformMasterRepository.save(platform);
    });
    assertNotNull(expectedPlatform);

    PlatformSearchParameters params = new PlatformSearchParameters();
    params.setApprovalState(Collections.singletonList(ApprovalState.APPROVED));
    params.setPage(1);
    params.setItemsPerPage(10);

    PagedItemsView<PlatformView> results = platformService.search(params);
    assertEquals(1, results.getItems().size());
    assertEquals(1, results.getTotalItems());
    assertEquals(1, results.getTotalPages());
    assertEquals(10, results.getItemsPerPage());
    assertEquals(1, results.getPage());

    assertEquals(expectedPlatform.getId(), results.getItems().get(0).getId());
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(s -> {
      platformMasterRepository.findByPlatformNormalized("TST").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST1").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST2").ifPresent(platformMasterRepository::delete);
      platformMasterRepository.findByPlatformNormalized("TST3").ifPresent(platformMasterRepository::delete);
      geosamplesUserRepository.findById("gabby").ifPresent(geosamplesUserRepository::delete);
    });
  }

}
