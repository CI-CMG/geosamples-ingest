package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SignificantFieldTest {

  private static class Fielder {
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  @Test
  public void testNotEqual() {
    Fielder fielder1 = new Fielder();
    fielder1.setValue("foo");
    Fielder fielder2 = new Fielder();
    fielder2.setValue("bar");
    SignificantField<String> significantField = new SignificantField<>(fielder1::getValue, fielder1::setValue, fielder2::getValue, fielder2::setValue);
    assertFalse(significantField.isEqual());

  }

  @Test
  public void testEqual() {
    Fielder fielder1 = new Fielder();
    fielder1.setValue("foo");
    Fielder fielder2 = new Fielder();
    fielder2.setValue("foo");
    SignificantField<String> significantField = new SignificantField<>(fielder1::getValue, fielder1::setValue, fielder2::getValue, fielder2::setValue);
    assertTrue(significantField.isEqual());
  }

  @Test
  public void testCopy() {
    Fielder fielder1 = new Fielder();
    fielder1.setValue("foo");
    Fielder fielder2 = new Fielder();
    fielder2.setValue("bar");
    SignificantField<String> significantField = new SignificantField<>(fielder1::getValue, fielder1::setValue, fielder2::getValue, fielder2::setValue);
    significantField.copy1To2();
    assertEquals("foo", fielder2.getValue());
  }
}