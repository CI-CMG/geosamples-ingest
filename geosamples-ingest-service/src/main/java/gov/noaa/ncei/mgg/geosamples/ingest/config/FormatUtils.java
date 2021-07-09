package gov.noaa.ncei.mgg.geosamples.ingest.config;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class FormatUtils {

  public static String doubleToString(double d) {
    DecimalFormat df = new DecimalFormat("0.#################", DecimalFormatSymbols.getInstance(Locale.US));
    return df.format(d);
  }

  public static String doubleToString2LeadingZeros(double d) {
    DecimalFormat df = new DecimalFormat("00.#################", DecimalFormatSymbols.getInstance(Locale.US));
    return df.format(d);
  }
  public static String doubleToString2LeadingZeros2Decimal(double d) {
    DecimalFormat df = new DecimalFormat("00.##", DecimalFormatSymbols.getInstance(Locale.US));
    return df.format(d);
  }

  private FormatUtils() {

  }

}
