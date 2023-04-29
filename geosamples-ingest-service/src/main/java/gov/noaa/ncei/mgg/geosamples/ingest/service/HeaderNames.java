package gov.noaa.ncei.mgg.geosamples.ingest.service;

import java.util.regex.Pattern;

public enum HeaderNames {

  FACILITY_CODE("Facility Code.*", "Facility Code"),
  SHIP_NAME("Ship Name.*", "Ship Name"),
  CRUISE_ID("Cruise ID.*", "Cruise ID"),
  SAMPLE_ID("Sample ID.*", "Sample ID"),
  DATE_COLLECTED("Date Collected.*", "Date Collected"),
  END_DATE("End Date.*", "End Date"),
  BEGINNING_LATITUDE("Beginning Latitude.*", "Beginning Latitude"),
  BEGINNING_LONGITUDE("Beginning Longitude.*", "Beginning Longitude"),
  ENDING_LATITUDE("Ending Latitude.*", "Ending Latitude"),
  ENDING_LONGITUDE("Ending Longitude.*", "Ending Longitude"),
  BEGINNING_WATER_DEPTH("Beginning Water Depth.*", "Beginning Water Depth"),
  ENDING_WATER_DEPTH("Ending Water Depth.*", "Ending Water Depth"),
  SAMPLING_DEVICE_CODE("Sampling Device Code.*", "Sampling Device Code"),
  STORAGE_METHOD_CODE("Storage Method Code.*", "Storage Method Code"),
  CORE_LENGTH("Core Length.*", "Core Length"),
  CORE_DIAMETER("Core Diameter.*", "Core Diameter"),
  DEPTH_TO_TOP_OF_INTERVAL("Depth to Top of Interval.*", "Depth to Top of Interval"),
  DEPTH_TO_BOTTOM_OF_INTERVAL("Depth to Bottom of Interval.*", "Depth to Bottom of Interval"),
  PRIMARY_LITHOLOGIC_COMPOSITION_CODE("Primary Lithologic Composition Code.*", "Primary Lithologic Composition Code"),
  PRIMARY_TEXTURE_CODE("Primary Texture Code.*", "Primary Texture Code"),
  SECONDARY_LITHOLOGIC_COMPOSITION_CODE("Secondary Lithologic Composition Code.*", "Secondary Lithologic Composition Code"),
  SECONDARY_TEXTURE_CODE("Secondary Texture Code.*", "Secondary Texture Code"),
  OTHER_COMPONENT_CODE("Other Component Code.*", true, "Other Component Code"),
  GEOLOGIC_AGE_CODE("Geologic Age Code.*", "Geologic Age Code"),
  INTERVAL_NUMBER("Interval.*", "Interval"),
  BULK_WEIGHT("Bulk Weight.*", "Bulk Weight"),
  PHYSIOGRAPHIC_PROVINCE_CODE("Physiographic Province Code.*", "Physiographic Province Code"),
  SAMPLE_LITHOLOGY_CODE("Sample Lithology Code.*", "Sample Lithology Code"),
  SAMPLE_MINERALOGY_CODE("Sample Mineralogy Code.*", "Sample Mineralogy Code"),
  SAMPLE_WEATHERING_OR_METAMORPHISM_CODE("Sample Weathering or Metamorphism Code.*", "Sample Weathering or Metamorphism Code"),
  GLASS_REMARKS_CODE("Glass Remarks Code.*", "Glass Remarks Code"),
  MUNSEL_COLOR("Munsell Color.*", "Munsell Color"),
  PRINCIPAL_INVESTIGATOR("Principal Investigator.*", "Principal Investigator"),
  SAMPLE_NOT_AVAILABLE("Sample Not Available.*", "Sample Not Available"),
  ISGN("IGSN.*", "IGSN"),
  ALTERNATE_CRUISE_OR_LEG("Alternate Cruise.*|Alternate Leg.*", "Alternate Leg"),
  FREE_FORM_DESCRIPTION_OF_COMPOSITION("Free[ -]?form Description of Composition.*", "Free form Description of Composition"),
  COMMENTS_ON_SUBSAMPLE_OR_INTERVAL("Comments.*", "Comments");


  private final Pattern match;
  private final String text;
  private final boolean multiValue;

  HeaderNames(String match, boolean multiValue, String text) {
    this.match = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
    this.multiValue = multiValue;
    this.text = text;
  }

  HeaderNames(String match, String text) {
    this(match, false, text);
  }

  public Pattern getMatch() {
    return match;
  }

  public boolean isMultiValue() {
    return multiValue;
  }

  public String getText() {
    return text;
  }
}
