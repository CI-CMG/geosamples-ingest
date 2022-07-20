-- platform --
alter table ${schema_name}.PLATFORM_MASTER add (ID number(19));
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop constraint CUR_SAMPLE_LINKS_PLATFORM_FK;
alter table ${schema_name}.CURATORS_CRUISE_LINKS drop constraint CUR_CRUISE_LINKS_PLATFORM_FK;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPTSQP_PLATFORM_FK;
alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_PLATFORM_FK;
drop table ${schema_name}.TEMPQC_INTERVAL purge;
drop table ${schema_name}.TEMPQC_SAMPLE purge;

create sequence ${schema_name}.PLATFORM_MASTER_SEQ nocache;

create or replace trigger ${schema_name}.PLATFORM_MASTER_bi
    before insert
    on ${schema_name}.PLATFORM_MASTER
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.PLATFORM_MASTER_SEQ.NEXTVAL;
    end if;
end;
/

update ${schema_name}.PLATFORM_MASTER set ID = ${schema_name}.PLATFORM_MASTER_SEQ.NEXTVAL;

alter table ${schema_name}.PLATFORM_MASTER drop constraint PLATFORM_MASTER_PK;

-- two steps to name an unnamed constraint
alter table ${schema_name}.PLATFORM_MASTER
    modify (PLATFORM varchar2(50) null);
alter table ${schema_name}.PLATFORM_MASTER
    modify (PLATFORM varchar2(50) constraint PLATFORM_MASTER_PLATFORM_NN not null);

alter table ${schema_name}.PLATFORM_MASTER
    add constraint PLATFORM_MASTER_PLATFORM_UK unique (PLATFORM);

alter table ${schema_name}.PLATFORM_MASTER
    modify (ID number(19) constraint PLATFORM_MASTER_PK primary key);

alter table ${schema_name}.PLATFORM_MASTER
    modify (PUBLISH varchar2(1) constraint PLATFORM_MASTER_PUBLISH_NN not null);

-- facility --
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPTSQP_FACILITY_FK;
alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_FACILITY_FK;

alter table ${schema_name}.CURATORS_FACILITY drop constraint CURATORS_FACILITY_PK;

alter table ${schema_name}.CURATORS_FACILITY add (ID number(19));

alter table ${schema_name}.CURATORS_FACILITY
    modify (INST_CODE varchar2(3) constraint CURATORS_FACILITY_INST_CODE_NN not null);

alter table ${schema_name}.CURATORS_FACILITY
    modify (PUBLISH varchar2(1) constraint CURATORS_FACILITY_PUBLISH_NN not null);

-- DB contains duplicates, is this expected?
-- alter table ${schema_name}.CURATORS_FACILITY
--     add constraint CURATORS_FACILITY_INST_CODE_UK unique (INST_CODE);

alter table ${schema_name}.CURATORS_FACILITY
    modify (FACILITY varchar2(50) constraint CURATORS_FACILITY_FACILITY_NN not null);

alter table ${schema_name}.CURATORS_FACILITY
    drop column LAST_UPDATE;

alter table  ${schema_name}.CURATORS_FACILITY
    add (LAST_UPDATE timestamp);

update ${schema_name}.CURATORS_FACILITY set LAST_UPDATE = current_timestamp;

alter table ${schema_name}.CURATORS_FACILITY
    modify (LAST_UPDATE timestamp constraint CURATORS_FACILITY_LAST_UPDATE_NN not null);

create sequence ${schema_name}.CURATORS_FACILITY_SEQ nocache;

update ${schema_name}.CURATORS_FACILITY set ID = ${schema_name}.CURATORS_FACILITY_SEQ.NEXTVAL;

create or replace trigger ${schema_name}.CURATORS_FACILITY_bi
    before insert
    on ${schema_name}.CURATORS_FACILITY
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_FACILITY_SEQ.NEXTVAL;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_FACILITY_bu
    before update
    on ${schema_name}.CURATORS_FACILITY
    for each row
begin
    :new.previous_state := :old.publish;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

alter table ${schema_name}.CURATORS_FACILITY
    modify (ID number(19) constraint CURATORS_FACILITY_PK primary key);

-- cruise + leg --

create table ${schema_name}.CURATORS_CRUISE (
    ID number(19) constraint CURATORS_CRUISE_ID_PK primary key,
    CRUISE_NAME varchar2(30) constraint CURATORS_CRUISE_CRUISE_NAME_NN not null,
    PLATFORM_ID number(19)
        constraint CURATORS_CRUISE_PLATFORM_ID_NN not null
        constraint CURATORS_CRUISE_PLATFORM_ID_FK references ${schema_name}.PLATFORM_MASTER,
    FACILITY_ID number(19)
        constraint CURATORS_CRUISE_FACILITY_ID_NN not null
        constraint CURATORS_CRUISE_FACILITY_ID_FK references ${schema_name}.CURATORS_FACILITY,
    PUBLISH VARCHAR2(1) constraint CURATORS_CRUISE_PUBLISH_NN not null,
    constraint CURATORS_CRUISE_FACILITY_ID_CRUISE_NAME_UK unique (CRUISE_NAME, FACILITY_ID)
);

create sequence ${schema_name}.CURATORS_CRUISE_SEQ nocache;

create or replace trigger ${schema_name}.CURATORS_CRUISE_SEQ
    before insert
    on ${schema_name}.CURATORS_CRUISE
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_SEQ.NEXTVAL;
    end if;
end;
/

create table ${schema_name}.CURATORS_LEG (
    ID number(19) constraint CURATORS_LEG_ID_PK primary key,
    CRUISE_ID number(19)
        constraint CURATORS_LEG_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE
        constraint CURATORS_LEG_CRUISE_ID_NN not null,
    LEG_NAME varchar2(30)
        constraint CURATORS_LEG_LEG_NAME_NN not null,
    PUBLISH VARCHAR2(1) constraint CURATORS_LEG_PUBLISH_NN not null,
    constraint CURATORS_LEG_CRUISE_ID_LEG_NAME_UK unique (CRUISE_ID, LEG_NAME)
);

create sequence ${schema_name}.CURATORS_LEG_SEQ nocache;

create or replace trigger ${schema_name}.CURATORS_LEG_BI
    before insert
    on ${schema_name}.CURATORS_LEG
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_LEG_SEQ.NEXTVAL;
    end if;
end;
/

insert into ${schema_name}.CURATORS_CRUISE (CRUISE_NAME, PLATFORM_ID, FACILITY_ID)
select distinct S.CRUISE, P.ID, F.ID
from ${schema_name}.CURATORS_SAMPLE_TSQP S
    inner join ${schema_name}.PLATFORM_MASTER P on S.PLATFORM = P.PLATFORM
    inner join ${schema_name}.CURATORS_FACILITY F on S.FACILITY_CODE = F.FACILITY_CODE;




-- sample link --

-- todo validate platform, cruise, leg, sample, device, igsn

create sequence ${schema_name}.CURATORS_SAMPLE_LINKS_SEQ nocache;
alter table  ${schema_name}.CURATORS_SAMPLE_LINKS add (ID number(19));
update ${schema_name}.CURATORS_SAMPLE_LINKS set ID = ${schema_name}.CURATORS_SAMPLE_LINKS_SEQ.NEXTVAL;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (ID number(19) constraint CURATORS_SAMPLE_LINKS_PK primary key);

create or replace trigger ${schema_name}.CURATORS_SAMPLE_LINKS_bi
    before insert
    on ${schema_name}.CURATORS_SAMPLE_LINKS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_SAMPLE_LINKS_SEQ.NEXTVAL;
    end if;
end;
/

alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column PLATFORM;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column CRUISE;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column LEG;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column SAMPLE;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column DEVICE;
alter table ${schema_name}.CURATORS_SAMPLE_LINKS drop column IGSN;

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (IMLGS varchar2(15)
        constraint CURATORS_SAMPLE_LINKS_IMLGS_FK references ${schema_name}.CURATORS_SAMPLE_TSQP
        constraint CURATORS_SAMPLE_LINKS_IMLGS_NN not null
        );

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (DATALINK varchar2(500) constraint CURATORS_SAMPLE_LINKS_DATALINK_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (LINK_LEVEL varchar2(30) constraint CURATORS_SAMPLE_LINKS_LINK_LEVEL_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (LINK_SOURCE varchar2(30) constraint CURATORS_SAMPLE_LINKS_LINK_SOURCE_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (LINK_TYPE varchar2(30) constraint CURATORS_SAMPLE_LINKS_LINK_TYPE_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_LINKS
    modify (PUBLISH varchar2(1) constraint CURATORS_SAMPLE_LINKS_PUBLISH_NN not null);

-- sample --


alter table  ${schema_name}.CURATORS_SAMPLE_TSQP add
    (
        CRUISE_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE,
        LEG_ID number(19) constraint CURATORS_SAMPLE_TSQP_LEG_ID_FK references ${schema_name}.CURATORS_LEG
);

-- todo prod validate cruise matches facility, platform
-- todo prod validate cruise and leg

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_ID =
    (select C.ID
    from ${schema_name}.CURATORS_CRUISE C
        inner join ${schema_name}.PLATFORM_MASTER P on C.PLATFORM_ID = P.ID
        inner join ${schema_name}.CURATORS_FACILITY F on C.FACILITY_ID = F.ID
    where S.CRUISE = C.CRUISE_NAME and S.PLATFORM = P.PLATFORM and S.FACILITY_CODE = F.FACILITY_CODE);


insert into ${schema_name}.CURATORS_LEG (CRUISE_ID, LEG_NAME)
select distinct S.CRUISE_ID, S.LEG from ${schema_name}.CURATORS_SAMPLE_TSQP S;

update ${schema_name}.CURATORS_SAMPLE_TSQP S set LEG_ID =
     (select L.ID
      from ${schema_name}.CURATORS_LEG L
      where S.LEG = L.LEG_NAME and S.CRUISE_ID = L.CRUISE_ID);



alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPTSQP_UK;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPLE_TSQP_UK;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column SHIP_CODE;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column FACILITY_CODE;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column PLATFORM;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column CRUISE;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LEG;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LAST_UPDATE;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column OBJECTID;

-- todo verify that all lat and lon are set
-- todo verify that end_lat / end_lon have values if deg is set
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LATDEG;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LATMIN;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column NS;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LONDEG;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column LONMIN;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column EW;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_LATDEG;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_LATMIN;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_NS;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_LONDEG;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_LONMIN;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop column END_EW;


alter table  ${schema_name}.CURATORS_SAMPLE_TSQP add (LAST_UPDATE timestamp);
update ${schema_name}.CURATORS_SAMPLE_TSQP set LAST_UPDATE = current_timestamp;
alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (LAST_UPDATE timestamp constraint CURATORS_SAMPLE_TSQP_LAST_UPDATE_NN not null);

alter table  ${schema_name}.CURATORS_SAMPLE_TSQP
    add constraint CURATORS_SAMPLE_TSQP_SAMPLE_CRUISE_ID unique (SAMPLE, CRUISE_ID);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (LAT number(9, 5) constraint CURATORS_SAMPLE_LAT_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (LON number(9, 5) constraint CURATORS_SAMPLE_LON_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (PUBLISH varchar2(1) constraint CURATORS_SAMPLE_TSQP_PUBLISH_NN not null);

-- todo combine cored_length and cored_diam to a single floating point number

create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_BI BEFORE
    INSERT ON ${schema_name}.CURATORS_SAMPLE_TSQP FOR EACH ROW
declare
    leg_cruise_id number(19);
begin
    if :new.LEG_ID is not null then
        select CURATORS_LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
        where :new.LEG_ID = LEG.ID;
        if :new.CRUISE_ID != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.IMLGS is null then
        :new.IMLGS := 'imlgs'||lpad(CURATORS_SEQ.NEXTVAL,7,'0');
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_bu
    before update
    on ${schema_name}.CURATORS_SAMPLE_TSQP
    for each row
declare
    leg_cruise_id number(19);
begin
    if :new.LEG_ID is not null then
        select CURATORS_LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
        where :new.LEG_ID = LEG.ID;
        if :new.CRUISE_ID != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    :new.previous_state := :old.publish;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/


alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (CRUISE_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_NN not null);

-- cruise link --

create sequence ${schema_name}.CURATORS_CRUISE_LINKS_SEQ nocache;

alter table  ${schema_name}.CURATORS_CRUISE_LINKS add (
    ID number(19), --pk
    CRUISE_ID number(19) constraint CURATORS_CRUISE_LINKS_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE, -- nullable,
    LEG_ID number(19) constraint CURATORS_CRUISE_LINKS_LEG_ID_FK references ${schema_name}.CURATORS_LEG -- nullable
    );

update ${schema_name}.CURATORS_CRUISE_LINKS set ID = ${schema_name}.CURATORS_CRUISE_LINKS_SEQ.NEXTVAL;

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (ID number(19) constraint CURATORS_CRUISE_LINKS_PK primary key);

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (DATALINK varchar2(500) constraint CURATORS_CRUISE_LINKS_DATALINK_NN not null);

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (LINK_LEVEL varchar2(30) constraint CURATORS_CRUISE_LINKS_LINK_LEVEL_NN not null);

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (LINK_SOURCE varchar2(30) constraint CURATORS_CRUISE_LINKS_LINK_SOURCE_NN not null);

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (LINK_TYPE varchar2(30) constraint CURATORS_CRUISE_LINKS_LINK_TYPE_NN not null);

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (PUBLISH varchar2(1) constraint CURATORS_CRUISE_LINKS_PUBLISH_NN not null);

create or replace trigger ${schema_name}.CURATORS_CRUISE_LINKS_bi
    before insert
    on ${schema_name}.CURATORS_CRUISE_LINKS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_LINKS_SEQ.NEXTVAL;
    end if;
end;
/

-- todo validate prod DB for link errors
-- todo validate cruise and leg are globally unique

update ${schema_name}.CURATORS_CRUISE_LINKS LINK set CRUISE_ID =
     (select C.ID from ${schema_name}.CURATORS_CRUISE C where LINK.CRUISE = C.CRUISE_NAME);

update ${schema_name}.CURATORS_CRUISE_LINKS LINK set LEG_ID =
     (select L.ID from ${schema_name}.CURATORS_LEG L where LINK.LEG = L.LEG_NAME);

alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column PLATFORM;
alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column CRUISE;
alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column LEG;


-- interval --


alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_UK;
alter table ${schema_name}.CURATORS_INTERVAL drop column SHIP_CODE;
alter table ${schema_name}.CURATORS_INTERVAL drop column FACILITY_CODE;
alter table ${schema_name}.CURATORS_INTERVAL drop column PLATFORM;
alter table ${schema_name}.CURATORS_INTERVAL drop column CRUISE;
alter table ${schema_name}.CURATORS_INTERVAL drop column SAMPLE; -- using IMLGS
alter table ${schema_name}.CURATORS_INTERVAL drop column PARENT_IGSN; -- using IMLGS
alter table  ${schema_name}.CURATORS_INTERVAL add (LAST_UPDATE timestamp);
update ${schema_name}.CURATORS_INTERVAL set LAST_UPDATE = current_timestamp;
alter table ${schema_name}.CURATORS_INTERVAL
    modify (LAST_UPDATE timestamp constraint CURATORS_INTERVAL_LAST_UPDATE_NN not null);

alter table ${schema_name}.CURATORS_INTERVAL
    modify (PUBLISH varchar2(1) constraint CURATORS_INTERVAL_PUBLISH_NN not null);

-- todo combine CM + MM values into single floating point fields
-- todo drop redundant munsell columns and create foreign key

create or replace trigger ${schema_name}.CURATORS_INTERVAL_bi
    before insert
    on ${schema_name}.CURATORS_INTERVAL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/


create or replace trigger ${schema_name}.CURATORS_INTERVAL_bu
    before update of publish
    on ${schema_name}.CURATORS_INTERVAL
    for each row
begin
    :new.previous_state := :old.publish;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/




