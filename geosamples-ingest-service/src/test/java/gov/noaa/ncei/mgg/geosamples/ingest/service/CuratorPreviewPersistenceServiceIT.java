package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class CuratorPreviewPersistenceServiceIT {

  @Autowired
  private CuratorsIntervalRepository curatorsIntervalRepository;
  @Autowired
  private CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  @Autowired
  private TransactionTemplate txTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  private void before() {
    txTemplate.executeWithoutResult(s -> {
      curatorsIntervalRepository.deleteAll();
      curatorsIntervalRepository.flush();
      curatorsSampleTsqpRepository.deleteAll();
    });
  }

  @AfterEach
  private void after() {
    before();
  }

  private void uploadFile() {
    LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    parameters.add("file", new ClassPathResource("imlgs_sample_good_full.xlsm"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setBasicAuth("geo", "samples");

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, entity, String.class);

    assertEquals(200, response.getStatusCode().value());
  }

  private static final TypeReference<PagedItemsView<CombinedSampleIntervalView>> ITEMS_VIEW = new TypeReference<PagedItemsView<CombinedSampleIntervalView>>() {
  };

  private List<CombinedSampleIntervalView> getAll() throws JsonProcessingException {

    List<CombinedSampleIntervalView> views = new ArrayList<>();

    int page = 1;
    int maxPage = 1;
    while (page <=  maxPage) {

      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.setBasicAuth("geo", "samples");
      headers.put("page", Arrays.asList("1"));

      HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

      ResponseEntity<String> response = restTemplate.exchange("/api/v1/sample-interval", HttpMethod.GET, entity, String.class);

      assertEquals(200, response.getStatusCode().value());

      PagedItemsView<CombinedSampleIntervalView> itemsView = objectMapper.readValue(response.getBody(), ITEMS_VIEW);
      views.addAll(itemsView.getItems());

      page++;
      maxPage = itemsView.getTotalPages();
    }


    return views;

  }

  @Test
  public void testSaveHappyPath() throws Exception {

    uploadFile();
    List<CombinedSampleIntervalView> sampleIntervals = getAll();

    assertEquals(5, sampleIntervals.size());

    CombinedSampleIntervalView view = sampleIntervals.get(0);

    assertEquals("AQ-01", view.getCruise());
    assertEquals("AQ-01-01", view.getSample());
    assertEquals("GEOMAR", view.getFacility());
    assertEquals("African Queen", view.getPlatform());
    assertEquals("F", view.getDevice());
    assertEquals(null, view.getShipCode());
    assertEquals("20210729", view.getBeginDate());
    assertEquals("20210730", view.getEndDate());
    assertEquals(39.68416154, view.getLat(), 0.0001);
    assertEquals(39, view.getLatDeg());
    assertEquals("41.05", view.getLatMin());
    assertEquals("N", view.getNs());
    assertEquals(40.684161537, view.getEndLat(), 0.0001);
    assertEquals(40, view.getEndLatDeg());
    assertEquals("41.05", view.getEndLatMin());
    assertEquals("N", view.getEndNs());
    assertEquals(26.97946201, view.getLon(), 0.0001);
    assertEquals(26, view.getLonDeg());
    assertEquals("58.77", view.getLonMin());
    assertEquals("E", view.getEw());
    assertEquals(27.979462008, view.getEndLon(), 0.0001);
    assertEquals(27, view.getEndLonDeg());
    assertEquals("58.77", view.getEndLonMin());
    assertEquals("E", view.getEndEw());
    assertEquals("D", view.getLatLonOrig());
    assertEquals(14, view.getWaterDepth());
    assertEquals(15, view.getEndWaterDepth());
    assertEquals("D", view.getLatLonOrig());
    assertEquals(536, view.getCoredLength());
    assertEquals(4, view.getCoredLengthMm());
    assertEquals(10, view.getCoredDiam());
    assertEquals(0, view.getCoredDiamMm());
    assertEquals(null, view.getPi());
    assertEquals(null, view.getProvince());
    assertEquals(null, view.getSampleLake());
    assertEquals(null, view.getOtherLink());
    assertNotNull(view.getLastUpdate());
    assertEquals(null, view.getIgsn());
    assertEquals(null, view.getLeg());
    assertEquals(null, view.getSampleComments());
    assertNotNull(view.getObjectId());
    assertEquals("https://www.ngdc.noaa.gov/geosamples/showsample.jsp?" + view.getImlgs(), view.getShowSampl());
    assertNotNull(view.getImlgs());
    assertEquals(1, view.getInterval());
    assertEquals(1, view.getDepthTop());
    assertEquals(5, view.getDepthTopMm());
    assertEquals(3, view.getDepthBot());
    assertEquals(5, view.getDepthBotMm());
    assertEquals(null, view.getDhCoreId());
    assertEquals(null, view.getDhCoreLength());
    assertEquals(null, view.getDhCoreLengthMm());
    assertEquals(null, view.getDhCoreInterval());
    assertEquals(null, view.getdTopInDhCore());
    assertEquals(null, view.getdTopMmInDhCore());
    assertEquals(null, view.getdBotInDhCore());
    assertEquals(null, view.getdBotMmInDhCore());
    assertEquals("5", view.getLith1());
    assertEquals(null, view.getText1());
    assertEquals(null, view.getLith2());
    assertEquals(null, view.getText2());
    assertEquals(null, view.getComp1());
    assertEquals(null, view.getComp2());
    assertEquals(null, view.getComp3());
    assertEquals(null, view.getComp4());
    assertEquals(null, view.getComp5());
    assertEquals(null, view.getComp6());
    assertEquals(null, view.getDescription());
    assertEquals(null, view.getAge());
    assertEquals(null, view.getAbsoluteAgeTop());
    assertEquals(null, view.getAbsoluteAgeBot());
    assertEquals(null, view.getWeight());
    assertEquals(null, view.getRockLith());
    assertEquals(null, view.getRockMin());
    assertEquals(null, view.getWeathMeta());
    assertEquals(null, view.getRemark());
    assertEquals(null, view.getMunsellCode());
    assertEquals(null, view.getMunsell());
    assertEquals(null, view.getExhaustCode());
    assertEquals(null, view.getPhotoLink());
    assertEquals(null, view.getIntervalLake());
    assertEquals(null, view.getUnitNumber());
    assertEquals(null, view.getIntComments());
    assertEquals(null, view.getDhDevice());
    assertEquals(null, view.getCmcdTop());
    assertEquals(null, view.getMmcdTop());
    assertEquals(null, view.getCmcdBot());
    assertEquals(null, view.getMmcdBot());
    assertEquals(null, view.getIntervalIgsn());
    assertEquals(null, view.getIntervalParentIsgn());
    assertEquals(false, view.isPublish());

    view = sampleIntervals.get(2);


    assertEquals("AQ-10", view.getCruise());
    assertEquals("AQ-001", view.getSample());
    assertEquals("GEOMAR", view.getFacility());
    assertEquals("B", view.getStorageMeth());
    assertEquals("African Queen", view.getPlatform());
    assertEquals("F", view.getDevice());
    assertEquals(null, view.getShipCode());
    assertEquals("20210727", view.getBeginDate());
    assertEquals("20210827", view.getEndDate());
    assertEquals(38.68416154, view.getLat(), 0.0001);
    assertEquals(38, view.getLatDeg());
    assertEquals("41.05", view.getLatMin());
    assertEquals("N", view.getNs());
    assertEquals(39.684161537, view.getEndLat(), 0.0001);
    assertEquals(39, view.getEndLatDeg());
    assertEquals("41.05", view.getEndLatMin());
    assertEquals("N", view.getEndNs());
    assertEquals(25.97946201, view.getLon(), 0.0001);
    assertEquals(25, view.getLonDeg());
    assertEquals("58.77", view.getLonMin());
    assertEquals("E", view.getEw());
    assertEquals(26.979462008, view.getEndLon(), 0.0001);
    assertEquals(26, view.getEndLonDeg());
    assertEquals("58.77", view.getEndLonMin());
    assertEquals("E", view.getEndEw());
    assertEquals("D", view.getLatLonOrig());
    assertEquals(14, view.getWaterDepth());
    assertEquals(15, view.getEndWaterDepth());
    assertEquals("D", view.getLatLonOrig());
    assertEquals(537, view.getCoredLength());
    assertEquals(6, view.getCoredLengthMm());
    assertEquals(10, view.getCoredDiam());
    assertEquals(7, view.getCoredDiamMm());
    assertEquals("Rocky", view.getPi());
    assertEquals("11", view.getProvince());
    assertEquals(null, view.getSampleLake());
    assertEquals(null, view.getOtherLink());
    assertNotNull(view.getLastUpdate());
    assertEquals("aasw32111", view.getIgsn());
    assertEquals("AQ-LEFT-LEG", view.getLeg());
    assertEquals(null, view.getSampleComments());
    assertNotNull(view.getObjectId());
    assertEquals("https://www.ngdc.noaa.gov/geosamples/showsample.jsp?" + view.getImlgs(), view.getShowSampl());
    assertNotNull(view.getImlgs());
    assertEquals(1, view.getInterval());
    assertEquals(11, view.getDepthTop());
    assertEquals(5, view.getDepthTopMm());
    assertEquals(13, view.getDepthBot());
    assertEquals(3, view.getDepthBotMm());
    assertEquals(null, view.getDhCoreId());
    assertEquals(null, view.getDhCoreLength());
    assertEquals(null, view.getDhCoreLengthMm());
    assertEquals(null, view.getDhCoreInterval());
    assertEquals(null, view.getdTopInDhCore());
    assertEquals(null, view.getdTopMmInDhCore());
    assertEquals(null, view.getdBotInDhCore());
    assertEquals(null, view.getdBotMmInDhCore());
    assertEquals("K", view.getLith1());
    assertEquals("N", view.getText1());
    assertEquals("9", view.getLith2());
    assertEquals("7", view.getText2());
    assertEquals("8", view.getComp1());
    assertEquals("M", view.getComp2());
    assertEquals("N", view.getComp3());
    assertEquals("O", view.getComp4());
    assertEquals("P", view.getComp5());
    assertEquals("Q", view.getComp6());
    assertEquals("Test Description", view.getDescription());
    assertEquals("58", view.getAge());
    assertEquals(null, view.getAbsoluteAgeTop());
    assertEquals(null, view.getAbsoluteAgeBot());
    assertEquals(50.5, view.getWeight(), 0.01);
    assertEquals("2P", view.getRockLith());
    assertEquals("E", view.getRockMin());
    assertEquals("0", view.getWeathMeta());
    assertEquals("5", view.getRemark());
    assertEquals("5Y 3/2", view.getMunsellCode());
    assertEquals("dark olive gray", view.getMunsell());
    assertEquals("X", view.getExhaustCode());
    assertEquals(null, view.getPhotoLink());
    assertEquals(null, view.getIntervalLake());
    assertEquals(null, view.getUnitNumber());
    assertEquals("Test Comments", view.getIntComments());
    assertEquals(null, view.getDhDevice());
    assertEquals(null, view.getCmcdTop());
    assertEquals(null, view.getMmcdTop());
    assertEquals(null, view.getCmcdBot());
    assertEquals(null, view.getMmcdBot());
    assertEquals(null, view.getIntervalIgsn());
    assertEquals("aasw32111", view.getIntervalParentIsgn());
    assertEquals(false, view.isPublish());

  }

}