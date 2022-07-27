update ${schema_name}.CURATORS_SAMPLE_LINKS L set IMLGS =
(select S.IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP S where L.PLATFORM = S.PLATFORM and L.CRUISE = S.CRUISE and L.SAMPLE = S.SAMPLE)
where L.IMLGS is null;

update ${schema_name}.CURATORS_SAMPLE_LINKS set IMLGS =
(select IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP where PLATFORM = 'Trident' and CRUISE like 'TR%89' and SAMPLE like '%21%')
where PLATFORM = 'Trident' and CRUISE = 'TR-089' and SAMPLE = '21-D';

update ${schema_name}.CURATORS_SAMPLE_LINKS set IMLGS =
(select IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP where PLATFORM = 'Trident' and CRUISE like 'TR%89' and SAMPLE like '%22%')
where PLATFORM = 'Trident' and CRUISE = 'TR-089' and SAMPLE = '22-D';

update ${schema_name}.CURATORS_SAMPLE_LINKS set IMLGS =
(select IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP where PLATFORM = 'Trident' and CRUISE like 'TR%89' and SAMPLE like '%30%')
where PLATFORM = 'Trident' and CRUISE = 'TR-089' and SAMPLE = '30-D';

update ${schema_name}.CURATORS_SAMPLE_LINKS set IMLGS =
(select IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP where PLATFORM = 'Trident' and CRUISE like 'TR%89' and SAMPLE like '%31%')
where PLATFORM = 'Trident' and CRUISE = 'TR-089' and SAMPLE = '31-D';

update ${schema_name}.CURATORS_SAMPLE_LINKS set IMLGS =
(select IMLGS from ${schema_name}.CURATORS_SAMPLE_TSQP where PLATFORM = 'Thomas G. Thompson' and CRUISE like '%57' and SAMPLE = '21-04')
where PLATFORM = 'Thomas G. Thompson' and CRUISE = 'TN057' and SAMPLE = '04';

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