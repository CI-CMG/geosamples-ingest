insert into ${schema_name}.GEOSAMPLES_ROLE (VERSION, ROLE_NAME) values (0, 'DEV');

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_DELETE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_ROLE_READ'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_ROLE_CREATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_ROLE_UPDATE'
);

insert into ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (ROLE_ID, AUTHORITY_NAME) values (
  (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV'),
  'ROLE_USER_ROLE_DELETE'
);

update ${schema_name}.GEOSAMPLES_USER set ROLE_ID = (select ROLE_ID from ${schema_name}.GEOSAMPLES_ROLE where ROLE_NAME = 'DEV') where USER_NAME = 'sarah';