alter table ${schema_name}.CURATORS_SAMPLE_TSQP drop constraint CURATORS_SAMPTSQP_FACILITY_FK;
alter table ${schema_name}.CURATORS_INTERVAL drop constraint CURATORS_INTERVAL_FACILITY_FK;

alter table ${schema_name}.CURATORS_FACILITY drop constraint CURATORS_FACILITY_PK;

alter table ${schema_name}.CURATORS_FACILITY add (ID number(19));

alter table ${schema_name}.CURATORS_FACILITY
    modify (INST_CODE varchar2(3) constraint CURATORS_FACILITY_INST_CODE_NN not null);

alter table ${schema_name}.CURATORS_FACILITY
    modify (PUBLISH varchar2(1) constraint CURATORS_FACILITY_PUBLISH_NN not null);

alter table ${schema_name}.CURATORS_FACILITY
    modify (FACILITY varchar2(100) constraint CURATORS_FACILITY_FACILITY_NN not null);

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
    if :new.previous_state != :old.publish then
      :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

alter table ${schema_name}.CURATORS_FACILITY
    modify (ID number(19) constraint CURATORS_FACILITY_PK primary key);