package gov.noaa.ncei.mgg.geosamples.ingest;

import static org.junit.jupiter.api.Assertions.*;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcSampleRepository;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ApplicationIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TempQcSampleRepository tempQcSampleRepository;

  @Autowired
  private TempQcIntervalRepository tempQcIntervalRepository;

  @Autowired
  private TransactionTemplate tx;

  @BeforeEach
  public void before() {
    tx.executeWithoutResult(s -> tempQcSampleRepository.deleteAll());
    tx.executeWithoutResult(s -> tempQcIntervalRepository.deleteAll());
  }

  @AfterEach
  public void after() {
    before();
  }

  @Test
  public void test() {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new FileSystemResource("src/test/resources/imlgs_new_entry_LDEO2021.xlsm"));

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());



  }

}