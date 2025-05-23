package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

public enum Authorities {

  //Role given implicitly if a user is configured in the DB
  ROLE_AUTHENTICATED_USER,

  ROLE_DATA_MANAGER_CREATE,
  ROLE_DATA_MANAGER_READ,
  ROLE_DATA_MANAGER_UPDATE,
  ROLE_DATA_MANAGER_DELETE,

  ROLE_AGE_READ,
  ROLE_AGE_CREATE,
  ROLE_AGE_UPDATE,
  ROLE_AGE_DELETE,

  ROLE_DEVICE_READ,
  ROLE_DEVICE_CREATE,
  ROLE_DEVICE_UPDATE,
  ROLE_DEVICE_DELETE,

  ROLE_FACILITY_READ,
  ROLE_FACILITY_CREATE,
  ROLE_FACILITY_UPDATE,
  ROLE_FACILITY_DELETE,

  ROLE_LITHOLOGY_READ,
  ROLE_LITHOLOGY_CREATE,
  ROLE_LITHOLOGY_UPDATE,
  ROLE_LITHOLOGY_DELETE,

  ROLE_MUNSELL_READ,
  ROLE_MUNSELL_CREATE,
  ROLE_MUNSELL_UPDATE,
  ROLE_MUNSELL_DELETE,

  ROLE_PLATFORM_READ,
  ROLE_PLATFORM_CREATE,
  ROLE_PLATFORM_UPDATE,
  ROLE_PLATFORM_DELETE,

  ROLE_PROVINCE_READ,
  ROLE_PROVINCE_CREATE,
  ROLE_PROVINCE_UPDATE,
  ROLE_PROVINCE_DELETE,

  ROLE_REMARK_READ,
  ROLE_REMARK_CREATE,
  ROLE_REMARK_UPDATE,
  ROLE_REMARK_DELETE,

  ROLE_ROCK_LITHOLOGY_READ,
  ROLE_ROCK_LITHOLOGY_CREATE,
  ROLE_ROCK_LITHOLOGY_UPDATE,
  ROLE_ROCK_LITHOLOGY_DELETE,

  ROLE_ROCK_MINERAL_READ,
  ROLE_ROCK_MINERAL_CREATE,
  ROLE_ROCK_MINERAL_UPDATE,
  ROLE_ROCK_MINERAL_DELETE,

  ROLE_SAMPLE_READ,
  ROLE_SAMPLE_CREATE,
  ROLE_SAMPLE_UPDATE,
  ROLE_SAMPLE_DELETE,

  ROLE_STORAGE_METHOD_READ,
  ROLE_STORAGE_METHOD_CREATE,
  ROLE_STORAGE_METHOD_UPDATE,
  ROLE_STORAGE_METHOD_DELETE,

  ROLE_TEXTURE_READ,
  ROLE_TEXTURE_CREATE,
  ROLE_TEXTURE_UPDATE,
  ROLE_TEXTURE_DELETE,

  ROLE_WEATHERING_READ,
  ROLE_WEATHERING_CREATE,
  ROLE_WEATHERING_UPDATE,
  ROLE_WEATHERING_DELETE,

  ROLE_SAMPLE_LINK_READ,
  ROLE_SAMPLE_LINK_CREATE,
  ROLE_SAMPLE_LINK_UPDATE,
  ROLE_SAMPLE_LINK_DELETE,

  ROLE_USER_READ,
  ROLE_USER_CREATE,
  ROLE_USER_UPDATE,
  ROLE_USER_DELETE,

  ROLE_USER_ROLE_READ,
  ROLE_USER_ROLE_CREATE,
  ROLE_USER_ROLE_UPDATE,
  ROLE_USER_ROLE_DELETE,

  ROLE_CRUISE_READ,
  ROLE_CRUISE_CREATE,
  ROLE_CRUISE_UPDATE,
  ROLE_CRUISE_DELETE,

  ROLE_CRUISE_LINK_READ,
  ROLE_CRUISE_LINK_CREATE,
  ROLE_CRUISE_LINK_UPDATE,
  ROLE_CRUISE_LINK_DELETE,

  ROLE_PROVIDER_SAMPLE_READ,
  ROLE_PROVIDER_SAMPLE_CREATE,
  ROLE_PROVIDER_SAMPLE_UPDATE,
  ROLE_PROVIDER_SAMPLE_DELETE,

  ROLE_PROVIDER_CRUISE_READ,
  ROLE_PROVIDER_CRUISE_CREATE,
  ROLE_PROVIDER_CRUISE_UPDATE,
  ROLE_PROVIDER_CRUISE_DELETE,

  ROLE_PROVIDER_INTERVAL_READ,
  ROLE_PROVIDER_INTERVAL_CREATE,
  ROLE_PROVIDER_INTERVAL_UPDATE,
  ROLE_PROVIDER_INTERVAL_DELETE,

  ROLE_PROVIDER_PLATFORM_READ,
  ROLE_PROVIDER_PLATFORM_CREATE,
  ROLE_PROVIDER_PLATFORM_UPDATE,
  ROLE_PROVIDER_PLATFORM_DELETE,
}
