package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class SampleIntervalControllerIT {


  @Test
  public void testUploadHappy() throws Exception {

  }

  @Test
  public void testUploadValidationError () throws Exception {

  }
}