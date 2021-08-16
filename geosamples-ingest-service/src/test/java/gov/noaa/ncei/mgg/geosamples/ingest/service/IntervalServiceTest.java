package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsMunsellRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class IntervalServiceTest {

  private IntervalService intervalService;
  @Mock
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Mock
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Mock
  private CuratorsLithologyRepository curatorsLithologyRepository;
  @Mock
  private CuratorsTextureRepository curatorsTextureRepository;
  @Mock
  private CuratorsAgeRepository curatorsAgeRepository;
  @Mock
  private CuratorsRockLithRepository curatorsRockLithRepository;
  @Mock
  private CuratorsRockMinRepository curatorsRockMinRepository;
  @Mock
  private CuratorsWeathMetaRepository curatorsWeathMetaRepository;
  @Mock
  private CuratorsRemarkRepository curatorsRemarkRepository;
  @Mock
  private CuratorsMunsellRepository curatorsMunsellRepository;

  @BeforeEach
  public void before() {
    intervalService = new IntervalService(
        curatorsIntervalRepository,
        curatorsSampleTsqpRepository,
        curatorsLithologyRepository,
        curatorsTextureRepository,
        curatorsAgeRepository,
        curatorsRockLithRepository,
        curatorsRockMinRepository,
        curatorsWeathMetaRepository,
        curatorsRemarkRepository,
        curatorsMunsellRepository);
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