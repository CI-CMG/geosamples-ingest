create table ${schema_name}.GEOSAMPLES_ROLE (
    ROLE_ID NUMBER(19)
       constraint GEOSAMPLES_ROLE_pk primary key,
    VERSION NUMBER(19) not null,
    ROLE_NAME VARCHAR2(100) not null unique
);

create table ${schema_name}.GEOSAMPLES_ROLE_AUTHORITY (
    ROLE_ID NUMBER(19)
        constraint GEOSAMPLES_ROLE_AUTHORITY_ROLE_ID_FK references ${schema_name}.GEOSAMPLES_ROLE,
    AUTHORITY_NAME VARCHAR2(1100)
        constraint GEOSAMPLES_ROLE_AUTHORITY_AUTHORITY_NAME_FK references ${schema_name}.GEOSAMPLES_AUTHORITY,
    constraint GEOSAMPLES_ROLE_AUTHORITY_pk primary key (ROLE_ID, AUTHORITY_NAME)
);

alter table ${schema_name}.GEOSAMPLES_USER add
    ROLE_ID NUMBER(19)
        constraint GEOSAMPLES_USER_ROLE_ID_FK references ${schema_name}.GEOSAMPLES_ROLE;

create sequence ${schema_name}.GEOSAMPLES_ROLE_SEQ nocache;

create or replace trigger ${schema_name}.GEOSAMPLES_ROLE_BI
    before insert
    on ${schema_name}.GEOSAMPLES_ROLE
    for each row
    begin
        if :NEW.ROLE_ID is null then
            :NEW.ROLE_ID := ${schema_name}.GEOSAMPLES_ROLE_SEQ.nextval;
        end if;
    end;