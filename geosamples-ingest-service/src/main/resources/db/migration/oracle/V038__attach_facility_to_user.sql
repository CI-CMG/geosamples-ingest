alter table ${schema_name}.GEOSAMPLES_USER add
    FACILITY_ID NUMBER(19)
        constraint GEOSAMPLES_USER_FACILITY_ID_FK references ${schema_name}.CURATORS_FACILITY;