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