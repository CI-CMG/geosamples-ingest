insert into ${schema_name}.GEOSAMPLES_ROLE (VERSION, ROLE_NAME) values (0, 'Provider');

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_SAMPLE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_SAMPLE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_SAMPLE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_SAMPLE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_CRUISE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_CRUISE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_CRUISE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_CRUISE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_INTERVAL_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_INTERVAL_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_INTERVAL_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_INTERVAL_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_PLATFORM_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_PLATFORM_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_PLATFORM_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVIDER_PLATFORM_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_AGE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_DEVICE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_FACILITY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_LITHOLOGY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_MUNSELL_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PROVINCE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_REMARK_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_ROCK_LITHOLOGY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_ROCK_MINERAL_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_STORAGE_METHOD_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_TEXTURE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_WEATHERING_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Provider'),
  'ROLE_PLATFORM_READ'
);
