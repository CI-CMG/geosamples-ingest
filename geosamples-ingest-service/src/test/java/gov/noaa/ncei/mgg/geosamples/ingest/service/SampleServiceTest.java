package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest
@ActiveProfiles("it")
public class SampleServiceTest {

  @Autowired
  private SampleService sampleService;

  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  private CuratorsCruiseRepository curatorsCruiseRepository;

  @Autowired
  private PlatformMasterRepository platformMasterRepository;

  @Autowired
  private CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  private CuratorsDeviceRepository curatorsDeviceRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private GeometryFactory geometryFactory;

  @BeforeEach
  private void beforeEach() {
    cleanDb();
  }

  @AfterEach
  private void afterEach() {
    cleanDb();
  }

  @Test
  public void testSearchByArea() throws ParseException {

    transactionTemplate.executeWithoutResult(s -> {
      CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
      cruise.setId(1L);
      cruise.setCruiseName("TEST");
      cruise.setPublish(true);
      cruise.setYear(Short.parseShort("2020"));
      cruise = curatorsCruiseRepository.save(cruise);

      PlatformMasterEntity platformMasterEntity = new PlatformMasterEntity();
      platformMasterEntity.setPlatform("TEST");
      platformMasterEntity.setId(1L);
      platformMasterEntity.setPublish(true);
      platformMasterEntity.setMasterId(1);
      platformMasterEntity.setPrefix("TEST");
      platformMasterEntity.setDateAdded(Instant.now());
      platformMasterEntity.setIcesCode("TEST");
      platformMasterEntity.setSourceUri("TEST");
      platformMasterEntity.setPreviousState("T");
      platformMasterEntity = platformMasterRepository.save(platformMasterEntity);

      CuratorsFacilityEntity facilityEntity = new CuratorsFacilityEntity();
      facilityEntity.setId(1L);
      facilityEntity.setFacilityCode("T");
      facilityEntity.setPublish(true);
      facilityEntity.setPreviousState("T");
      facilityEntity.setAddr1("TEST-1");
      facilityEntity.setAddr2("TEST-2");
      facilityEntity.setAddr3("TEST-3");
      facilityEntity.setAddr4("TEST-4");
      facilityEntity.setPreviousState("T");
      facilityEntity.setFacility("TEST");
      facilityEntity.setDoiLink("TEST");
      facilityEntity.setEmailLink("TEST");
      facilityEntity.setFacilityComment("TEST");
      facilityEntity.setFtpLink("TEST");
      facilityEntity.setInstCode("T");
      facilityEntity.setLastUpdate(Instant.now());
      facilityEntity.setUrlLink("TEST");
      facilityEntity.setContact1("TEST-1");
      facilityEntity.setContact2("TEST-1");
      facilityEntity.setContact3("TEST-3");
      facilityEntity = curatorsFacilityRepository.save(facilityEntity);

      CuratorsCruiseFacilityEntity cruiseFacilityEntity = new CuratorsCruiseFacilityEntity();
      cruiseFacilityEntity.setFacility(facilityEntity);
      cruiseFacilityEntity.setCruise(cruise);
      cruiseFacilityEntity.setPublish(true);
      cruiseFacilityEntity.setId(1L);
      cruise.addFacilityMapping(cruiseFacilityEntity);

      CuratorsCruisePlatformEntity cruisePlatformEntity = new CuratorsCruisePlatformEntity();
      cruisePlatformEntity.setCruise(cruise);
      cruisePlatformEntity.setPlatform(platformMasterEntity);
      cruisePlatformEntity.setPublish(true);
      cruisePlatformEntity.setId(1L);
      cruise.addPlatformMapping(cruisePlatformEntity);

      CuratorsLegEntity curatorsLegEntity = new CuratorsLegEntity();
      curatorsLegEntity.setCruise(cruise);
      curatorsLegEntity.setPublish(true);
      curatorsLegEntity.setId(1L);
      curatorsLegEntity.setLegName("TEST");
      cruise.addLeg(curatorsLegEntity);

      curatorsCruiseRepository.save(cruise);

      CuratorsDeviceEntity deviceEntity = new CuratorsDeviceEntity();
      deviceEntity.setDevice("TEST");
      deviceEntity.setDeviceCode("T");
      deviceEntity.setPublish(true);
      deviceEntity.setPreviousState("T");
      deviceEntity.setSourceUri("TEST");
      curatorsDeviceRepository.save(deviceEntity);
    });

    SampleView sampleView1 = new SampleView();
    sampleView1.setImlgs("TEST-1");
    sampleView1.setCruise("TEST");
    sampleView1.setFacilityCode("T");
    sampleView1.setPlatform("TEST");
    sampleView1.setLeg("TEST");
    sampleView1.setLat(15.); // Center of square
    sampleView1.setLon(15.);
    sampleView1.setDeviceCode("T");
    sampleView1.setSample("TEST-1");
    sampleView1 = sampleService.create(sampleView1);

    SampleView sampleView2 = new SampleView();
    sampleView2.setImlgs("TEST-2");
    sampleView2.setCruise("TEST");
    sampleView2.setFacilityCode("T");
    sampleView2.setPlatform("TEST");
    sampleView2.setLeg("TEST");
    sampleView2.setLat(25.); // Outside of square
    sampleView2.setLon(25.);
    sampleView2.setDeviceCode("T");
    sampleView2.setSample("TEST-2");
    sampleService.create(sampleView2);

    SampleSearchParameters searchParameters = new SampleSearchParameters();

    String wkt = "POLYGON((10 10,10 20,20 20,20 10, 10 10))";
    searchParameters.setArea(new WKTReader(geometryFactory).read(wkt));

    PagedItemsView<SampleView> pagedItemsView = sampleService.search(searchParameters);
    assertEquals(1, pagedItemsView.getTotalItems());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());

    List<SampleView> sampleViews = pagedItemsView.getItems();
    assertEquals(1, sampleViews.size());

    SampleView sampleView = sampleViews.get(0);
    assertEquals(sampleView1.getImlgs(), sampleView.getImlgs());
    assertEquals(sampleView1.getCruise(), sampleView.getCruise());
    assertEquals(sampleView1.getFacilityCode(), sampleView.getFacilityCode());
    assertEquals(sampleView1.getPlatform(), sampleView.getPlatform());
    assertEquals(sampleView1.getLeg(), sampleView.getLeg());
    assertEquals(sampleView1.getLat(), sampleView.getLat());
    assertEquals(sampleView1.getLon(), sampleView.getLon());
    assertEquals(sampleView1.getDeviceCode(), sampleView.getDeviceCode());
    assertEquals(sampleView1.getSample(), sampleView.getSample());
  }

  private void cleanDb() {
    transactionTemplate.executeWithoutResult(s -> {
      curatorsSampleTsqpRepository.deleteAll();
      curatorsCruiseRepository.deleteAll();
      platformMasterRepository.deleteAll();
      curatorsFacilityRepository.deleteAll();
      curatorsDeviceRepository.deleteAll();
    });
  }

}