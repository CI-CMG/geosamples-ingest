insert into ${schema_name}.GEOSAMPLES_ROLE (VERSION, ROLE_NAME) values (0, 'Admin');

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DATA_MANAGER_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DATA_MANAGER_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DATA_MANAGER_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DATA_MANAGER_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_AGE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_AGE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_AGE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_AGE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DEVICE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DEVICE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DEVICE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_DEVICE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_FACILITY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_FACILITY_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_FACILITY_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_FACILITY_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_LITHOLOGY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_LITHOLOGY_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_LITHOLOGY_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_LITHOLOGY_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_MUNSELL_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_MUNSELL_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_MUNSELL_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_MUNSELL_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PLATFORM_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PLATFORM_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PLATFORM_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PLATFORM_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PROVINCE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PROVINCE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PROVINCE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_PROVINCE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_REMARK_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_REMARK_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_REMARK_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_REMARK_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_LITHOLOGY_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_LITHOLOGY_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_LITHOLOGY_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_LITHOLOGY_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_MINERAL_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_MINERAL_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_MINERAL_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_ROCK_MINERAL_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_STORAGE_METHOD_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_STORAGE_METHOD_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_STORAGE_METHOD_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_STORAGE_METHOD_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_TEXTURE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_TEXTURE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_TEXTURE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_TEXTURE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_WEATHERING_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_WEATHERING_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_WEATHERING_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_WEATHERING_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_LINK_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_LINK_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_LINK_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_SAMPLE_LINK_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_ROLE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_ROLE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_ROLE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_USER_ROLE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_LINK_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_LINK_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_LINK_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'Admin'),
  'ROLE_CRUISE_LINK_DELETE'
);
