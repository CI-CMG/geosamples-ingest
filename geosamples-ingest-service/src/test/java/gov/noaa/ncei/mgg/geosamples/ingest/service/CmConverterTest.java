package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CmConverterTest {

  @Test
  public void test() {
    CmConverter cmConverter = new CmConverter(25.66);
    assertEquals(25, cmConverter.getCm());
    assertEquals(7, cmConverter.getMm());
  }

}