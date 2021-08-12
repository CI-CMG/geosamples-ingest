package gov.noaa.ncei.mgg.geosamples.ingest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class ApplicationIT {

//  @Autowired
//  private TestRestTemplate restTemplate;
//
//  @Autowired
//  private TempQcSampleRepository tempQcSampleRepository;
//
//  @Autowired
//  private TempQcIntervalRepository tempQcIntervalRepository;
//
//  @Autowired
//  private TransactionTemplate tx;
//
//  @BeforeEach
//  public void before() {
//    tx.executeWithoutResult(s -> tempQcSampleRepository.deleteAll());
//    tx.executeWithoutResult(s -> tempQcIntervalRepository.deleteAll());
//  }
//
//  @AfterEach
//  public void after() {
//    before();
//  }
//
//  @Test
//  public void test() {
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//    headers.setBasicAuth("geo", "samples");
//
//    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//    body.add("file", new FileSystemResource("src/test/resources/imlgs_new_entry_LDEO2021.xlsm"));
//
//    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
//
//    ResponseEntity<String> response = restTemplate.exchange("/api/v1/curator-data/upload", HttpMethod.POST, httpEntity, String.class);
//
//    System.out.println(response.getBody());
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//
//
//
//  }

}