package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.PlatformView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
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
    ApprovalView approvalView = new ApprovalView();
    approvalView.setApprovalState(ApprovalState.PENDING);

    ApiException exception = assertThrows(ApiException.class, () -> platformService.updateApproval(approvalView, 1L));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFlashErrors().size());
    assertEquals(1, exception.getApiError().getFormErrors().size());
    assertEquals("approvalState", exception.getApiError().getFormErrors().keySet().stream().findFirst().orElse(null));
    assertEquals(1, exception.getApiError().getFormErrors().get("approvalState").size());
    assertEquals("Cannot update approval state to: PENDING", exception.getApiError().getFormErrors().get("approvalState").get(0));
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(s -> {
      platformMasterRepository.findByPlatformNormalized("TST").ifPresent(platformMasterRepository::delete);
      geosamplesUserRepository.findById("gabby").ifPresent(geosamplesUserRepository::delete);
    });
  }

}
