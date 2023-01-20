package gov.noaa.ncei.mgg.geosamples.ingest.service;

import java.util.regex.Pattern;

public enum HeaderNames {

  FACILITY_CODE("Facility Code.*"),
  SHIP_NAME("Ship Name.*"),
  CRUISE_ID("Cruise ID.*"),
  SAMPLE_ID("Sample ID.*"),
  DATE_COLLECTED("Date Collected.*"),
  END_DATE("End Date.*"),
  BEGINNING_LATITUDE("Beginning Latitude.*"),
  BEGINNING_LONGITUDE("Beginning Longitude.*"),
  ENDING_LATITUDE("Ending Latitude.*"),
  ENDING_LONGITUDE("Ending Longitude.*"),
  BEGINNING_WATER_DEPTH("Beginning Water Depth.*"),
  ENDING_WATER_DEPTH("Ending Water Depth.*"),
  SAMPLING_DEVICE_CODE("Sampling Device Code.*"),
  STORAGE_METHOD_CODE("Storage Method Code.*"),
  CORE_LENGTH("Core Length.*"),
  CORE_DIAMETER("Core Diameter.*"),
  DEPTH_TO_TOP_OF_INTERVAL("Depth to Top of Interval.*"),
  DEPTH_TO_BOTTOM_OF_INTERVAL("Depth to Bottom of Interval.*"),
  PRIMARY_LITHOLOGIC_COMPOSITION_CODE("Primary Lithologic Composition Code.*"),
  PRIMARY_TEXTURE_CODE("Primary Texture Code.*"),
  SECONDARY_LITHOLOGIC_COMPOSITION_CODE("Secondary Lithologic Composition Code.*"),
  SECONDARY_TEXTURE_CODE("Secondary Texture Code.*"),
  OTHER_COMPONENT_CODE("Other Component Code.*", true),
  GEOLOGIC_AGE_CODE("Geologic Age Code.*", true),
  INTERVAL_NUMBER("Interval.*"),
  BULK_WEIGHT("Bulk Weight.*"),
  PHYSIOGRAPHIC_PROVINCE_CODE("Physiographic Province Code.*"),
  SAMPLE_LITHOLOGY_CODE("Sample Lithology Code.*"),
  SAMPLE_MINERALOGY_CODE("Sample Mineralogy Code.*"),
  SAMPLE_WEATHERING_OR_METAMORPHISM_CODE("Sample Weathering or Metamorphism Code.*"),
  GLASS_REMARKS_CODE("Glass Remarks Code.*"),
  MUNSEL_COLOR("Munsell Color.*"),
  PRINCIPAL_INVESTIGATOR("Principal Investigator.*"),
  SAMPLE_NOT_AVAILABLE("Sample Not Available.*"),
  ISGN("IGSN.*"),
  ALTERNATE_CRUISE_OR_LEG("Alternate Cruise.*|Alternate Leg.*"),
  FREE_FORM_DESCRIPTION_OF_COMPOSITION("Free[ -]?form Description of Composition.*"),
  COMMENTS_ON_SUBSAMPLE_OR_INTERVAL("Comments.*");


  private final Pattern match;
  private final boolean multiValue;

  HeaderNames(String match, boolean multiValue) {
    this.match = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
    this.multiValue = multiValue;
  }

  HeaderNames(String match) {
    this(match, false);
  }

  public Pattern getMatch() {
    return match;
  }

  public boolean isMultiValue() {
    return multiValue;
  }
}
