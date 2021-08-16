package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.*;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class SampleIntervalServiceTest {

  private SampleIntervalService sampleIntervalService;
  @Mock
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Mock
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @BeforeEach
  public void before() {
    sampleIntervalService = new SampleIntervalService(curatorsIntervalRepository, curatorsSampleTsqpRepository);
  }


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