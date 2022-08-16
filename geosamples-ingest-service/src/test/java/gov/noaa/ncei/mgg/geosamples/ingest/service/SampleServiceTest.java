package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@Disabled
@ExtendWith(MockitoExtension.class)
public class SampleServiceTest {

  private SampleService sampleService;
  @Mock
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Mock
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Mock
  private SampleDataUtils sampleDataUtils;
  @Mock
  private ServiceProperties serviceProperties;

//  @BeforeEach
//  public void before() {
//    sampleService = new SampleService(curatorsSampleTsqpRepository, curatorsIntervalRepository, sampleDataUtils, serviceProperties,
//        curatorsCruisePlatformRepository, curatorsCruiseFacilityRepository);
//  }


//  public void testPatchHappy() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testPatchValidationError() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testSearch() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testCreate() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testUpdate() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testGet() throws Exception {
//    sampleIntervalService.patch();
//  }
//
//  public void testDelete() throws Exception {
//    sampleIntervalService.patch();
//  }

}