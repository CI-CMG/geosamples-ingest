alter table ${schema_name}.TEMPQC_SAMPLE drop constraint TEMPQC_SAMPLE_PK;

alter table ${schema_name}.TEMPQC_SAMPLE
    add constraint TEMPQC_SAMPLE_PK
        primary key (IMLGS);

alter table ${schema_name}.TEMPQC_SAMPLE
    add constraint TEMPQC_SAMPLE_IGSN_UK
        unique (IGSN);

alter table ${schema_name}.TEMPQC_SAMPLE
    add constraint TEMPQC_SAMPLE_OBJECTID_UK
        unique (OBJECTID);

alter table ${schema_name}.TEMPQC_SAMPLE
    add constraint TEMPQC_SAMPLE_OBJECTID_NN
        check ( OBJECTID is not null );

alter table ${schema_name}.TEMPQC_SAMPLE
    add constraint TEMPQC_SAMPLE_UK
        unique (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE);


alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_FACILITY_CODE_NN
        check ( FACILITY_CODE is not null );


alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_PLATFORM_NN
        check ( PLATFORM is not null );

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_CRUISE_NN
        check ( CRUISE is not null );

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_SAMPLE_NN
        check ( SAMPLE is not null );

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_DEVICE_NN
        check ( DEVICE is not null );

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_OBJECTID_NN
        check ( OBJECTID is not null );


create or replace trigger ${schema_name}.TEMPQC_SAMPLE_bi BEFORE
    INSERT ON ${schema_name}.TEMPQC_SAMPLE REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
begin

    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.OBJECTID is null then
        -- Assign surrogate id.
        :new.OBJECTID := CURATORS_SEQ.NEXTVAL ;
    end if;
    if :new.IMLGS is null then
        :new.IMLGS := 'imlgs'||lpad(:new.objectid,7,'0');
    end if;
end;
/


alter table ${schema_name}.TEMPQC_INTERVAL drop constraint TEMPQC_INTERVAL_PK;

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_PK
        primary key (IMLGS, INTERVAL);

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_UK
        unique (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE, INTERVAL);

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_FACILITY_CODE_NN
        check ( FACILITY_CODE is not null );

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_PLATFORM_NN
        check ( PLATFORM is not null );

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_CRUISE_NN
        check ( CRUISE is not null );

alter table ${schema_name}.TEMPQC_INTERVAL
    add constraint TEMPQC_INTERVAL_DEVICE_NN
        check ( DEVICE is not null );


alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_FACILITY_CODE_NN
        check ( FACILITY_CODE is not null );

alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_PLATFORM_NN
        check ( PLATFORM is not null );

alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_CRUISE_NN
        check ( CRUISE is not null );

alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_DEVICE_NN
        check ( DEVICE is not null );

