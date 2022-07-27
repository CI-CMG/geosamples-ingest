alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_UK;
alter table ${schema_name}.CURATORS_INTERVAL drop column SHIP_CODE;
alter table ${schema_name}.CURATORS_INTERVAL drop column FACILITY_CODE;
alter table ${schema_name}.CURATORS_INTERVAL drop column PLATFORM;
alter table ${schema_name}.CURATORS_INTERVAL drop column CRUISE;
alter table ${schema_name}.CURATORS_INTERVAL drop column SAMPLE; -- using IMLGS
alter table ${schema_name}.CURATORS_INTERVAL drop column PARENT_IGSN; -- using IMLGS
alter table ${schema_name}.CURATORS_INTERVAL drop column DEVICE; -- referenced IMLGS sample has the device
alter table  ${schema_name}.CURATORS_INTERVAL add (LAST_UPDATE timestamp);
update ${schema_name}.CURATORS_INTERVAL set LAST_UPDATE = current_timestamp;
alter table ${schema_name}.CURATORS_INTERVAL
    modify (LAST_UPDATE timestamp constraint CURATORS_INTERVAL_LAST_UPDATE_NN not null);

alter table ${schema_name}.CURATORS_INTERVAL
    modify (PUBLISH varchar2(1) constraint CURATORS_INTERVAL_PUBLISH_NN not null);

-- todo combine CM + MM values into single floating point fields
-- todo drop redundant munsell columns and create foreign key
-- todo link exhaust?

create sequence ${schema_name}.CURATORS_INTERVAL_SEQ nocache;

alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_PK;

alter table  ${schema_name}.CURATORS_INTERVAL add (ID number(19));

update ${schema_name}.CURATORS_INTERVAL set ID = ${schema_name}.CURATORS_INTERVAL_SEQ.NEXTVAL;

alter table ${schema_name}.CURATORS_INTERVAL
    modify (ID number(19) constraint CURATORS_INTERVAL_PK primary key);

alter table ${schema_name}.CURATORS_INTERVAL
    add constraint CURATORS_INTERVAL_IMLGS_INTERVAL_UK unique (IMLGS, INTERVAL);

-- two steps to name an unnamed constraint
alter table ${schema_name}.CURATORS_INTERVAL
    modify (INTERVAL number(6) null);
alter table ${schema_name}.CURATORS_INTERVAL
    modify (INTERVAL number(6) constraint CURATORS_INTERVAL_INTERVAL_NN not null);

-- two steps to name an unnamed constraint
alter table ${schema_name}.CURATORS_INTERVAL
    modify (IMLGS varchar2(15) null);
alter table ${schema_name}.CURATORS_INTERVAL
    modify (IMLGS varchar2(15) constraint CURATORS_INTERVAL_IMLGS_NN not null);

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
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_INTERVAL_SEQ.NEXTVAL;
    end if;
end;
/


create or replace trigger ${schema_name}.CURATORS_INTERVAL_bu
    before update
    on ${schema_name}.CURATORS_INTERVAL
    for each row
begin
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/