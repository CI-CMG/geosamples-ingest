create table ${schema_name}.GEOSAMPLES_USER_TOKEN
(
    ID number(19)
        constraint GEOSAMPLES_USER_TOKEN_PK primary key,
    USER_NAME varchar(200)
        constraint GEOSAMPLES_USER_TOKEN_USER_ID_NN not null
        constraint GEOSAMPLES_USER_TOKEN_USER_ID_FK references ${schema_name}.GEOSAMPLES_USER,
    TOKEN_ALIAS varchar2(200)
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_ALIAS_NN not null,
    TOKEN_HASH varchar2(1000)
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_HASH_NN not null
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_HASH_UK unique,
    constraint GEOSAMPLES_USER_TOKEN_USER_ID_TOKEN_ALIAS_UK unique (USER_ID, TOKEN_ALIAS)
);

create sequence ${schema_name}.GEOSAMPLES_USER_TOKEN_SEQ nocache;