create table ${schema_name}.GEOSAMPLES_USER
(
    USER_NAME     VARCHAR2(200)
        constraint GEOSAMPLES_USER_PK primary key,
    VERSION      NUMBER(10),
    DISPLAY_NAME VARCHAR2(200)
);

create table ${schema_name}.GEOSAMPLES_AUTHORITY
(
    AUTHORITY_NAME VARCHAR2(100)
        constraint GEOSAMPLES_AUTHORITY_PK primary key
);

create table ${schema_name}.GEOSAMPLES_USER_AUTHORITY
(
    USER_NAME VARCHAR2(200)
      constraint GEOSAMPLES_USER_AUTHORITY_USER_FK references ${schema_name}.GEOSAMPLES_USER,
    AUTHORITY_NAME VARCHAR2(100)
      constraint GEOSAMPLES_USER_AUTHORITY_AUTHORITY_FK references ${schema_name}.GEOSAMPLES_AUTHORITY,
    constraint GEOSAMPLES_USER_AUTHORITY_PK primary key (USER_NAME, AUTHORITY_NAME)
);
