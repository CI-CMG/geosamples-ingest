alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPTSQP_PLATFORM_FK;



alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPTSQP_PLATFORM_FK
        foreign key (PLATFORM)
            references ${schema_name}.PLATFORM_MASTER(PLATFORM);

alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_PLATFORM_FK;

alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_PLATFORM_FK
        foreign key (PLATFORM)
            references ${schema_name}.PLATFORM_MASTER(PLATFORM);
