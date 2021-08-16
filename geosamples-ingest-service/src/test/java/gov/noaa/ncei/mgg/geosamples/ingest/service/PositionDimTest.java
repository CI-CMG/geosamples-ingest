package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PositionDimTest {


  @Test
  public void testSimpleRounding() {
    Double value = 39.68416154;
    Integer degrees = 39;
    Double minutes = 41.0496924;
    String direction = "N";

    PositionDim positionDim = new PositionDim(value, degrees, minutes, direction);
    assertEquals(39.68416154, positionDim.getValue(), 0.00001);
    assertEquals(39, positionDim.getDegrees());
    assertEquals("41.05", positionDim.getMinutes());
    assertEquals("N", positionDim.getDirection());
  }

  @Test
  public void testRoundingRollover() {
    Double value = 39.9999999999;
    Integer degrees = 39;
    Double minutes = 59.999999994;
    String direction = "N";

    PositionDim positionDim = new PositionDim(value, degrees, minutes, direction);
    assertEquals(39.9999999999, positionDim.getValue(), 0.00001);
    assertEquals(40, positionDim.getDegrees());
    assertEquals("00.00", positionDim.getMinutes());
    assertEquals("N", positionDim.getDirection());
  }

}