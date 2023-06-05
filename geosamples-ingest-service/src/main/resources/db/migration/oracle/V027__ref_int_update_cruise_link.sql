create table ${schema_name}.CURATORS_CRUISE_LINKS_BROKEN_LINKS
(
    PLATFORM       VARCHAR2(50),
    CRUISE         VARCHAR2(30),
    LEG            VARCHAR2(30),
    DATALINK       VARCHAR2(500),
    LINK_LEVEL     VARCHAR2(30),
    LINK_SOURCE    VARCHAR2(30),
    LINK_TYPE      VARCHAR2(30),
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1)
)
/

create sequence ${schema_name}.CURATORS_CRUISE_LINKS_SEQ nocache;

alter table  ${schema_name}.CURATORS_CRUISE_LINKS add (
    ID number(19),
    CRUISE_PLATFORM_ID number(19) constraint CURATORS_CRUISE_LINKS_CRUISE_PLATFORM_ID_FK references ${schema_name}.CURATORS_CRUISE_PLATFORM,
    LEG_ID number(19) constraint CURATORS_CRUISE_LINKS_LEG_ID_FK references ${schema_name}.CURATORS_LEG
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
declare
    leg_cruise_id number(19);
    cruise_id number(19);
begin
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_LINKS_SEQ.NEXTVAL;
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG where :new.LEG_ID = LEG.ID;
        select CP.CRUISE_ID into cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP where :new.CRUISE_PLATFORM_ID = CP.ID;
        if cruise_id != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_CRUISE_LINKS_bu
    before update
    on ${schema_name}.CURATORS_CRUISE_LINKS
    for each row
declare
    leg_cruise_id number(19);
    cruise_id number(19);
begin
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG where :new.LEG_ID = LEG.ID;
        select CP.CRUISE_ID into cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP where :new.CRUISE_PLATFORM_ID = CP.ID;
        if cruise_id != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.previous_state != :old.publish then
      :new.previous_state := :old.publish;
    end if;
end;
/

update ${schema_name}.CURATORS_CRUISE_LINKS LINK set (CRUISE_PLATFORM_ID) =
(select CP.ID from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
                     inner join ${schema_name}.CURATORS_CRUISE C on CP.CRUISE_ID = C.ID
                     inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
where C.CRUISE_NAME = LINK.CRUISE and P.PLATFORM = LINK.PLATFORM )
where LINK.CRUISE is not null and (LINK.PLATFORM, LINK.CRUISE) not in (
    select PLATFORM, CRUISE
    from (
             select P.PLATFORM as PLATFORM, C.CRUISE_NAME as CRUISE, count(*) as NUM
             from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
                      inner join ${schema_name}.CURATORS_CRUISE C on CP.CRUISE_ID = C.ID
                      inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
             where (P.PLATFORM, C.CRUISE_NAME) in (
                 select distinct LINK.PLATFORM, LINK.CRUISE
                 from ${schema_name}.CURATORS_CRUISE_LINKS LINK
                 where LINK.CRUISE is not null
             )
             group by P.PLATFORM, C.CRUISE_NAME
         )
    where NUM > 1
);


update ${schema_name}.CURATORS_CRUISE_LINKS LINK set (LEG_ID) =
  (select L.ID from CURATORS_CRUISE_PLATFORM CP
                        inner join ${schema_name}.CURATORS_CRUISE C on CP.CRUISE_ID = C.ID
                        inner join ${schema_name}.CURATORS_LEG L on C.ID = L.CRUISE_ID
   where LINK.LEG = L.LEG_NAME and CP.ID = LINK.CRUISE_PLATFORM_ID)
where LINK.LEG is not null and LINK.CRUISE_PLATFORM_ID is not null;


update ${schema_name}.CURATORS_CRUISE_LINKS LINK set (LEG_ID, CRUISE_PLATFORM_ID) =
  (select L.ID, CP.ID from CURATORS_CRUISE_PLATFORM CP
                               inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
                               inner join ${schema_name}.CURATORS_CRUISE C on CP.CRUISE_ID = C.ID
                               inner join ${schema_name}.CURATORS_LEG L on C.ID = L.CRUISE_ID
   where LINK.LEG = L.LEG_NAME and P.PLATFORM = LINK.PLATFORM and C.CRUISE_NAME = LINK.CRUISE)
where LINK.CRUISE_PLATFORM_ID is null
  and LINK.LEG is not null
  and LINK.LEG_ID is null
  and LINK.CRUISE is not null;


update ${schema_name}.CURATORS_CRUISE_LINKS LINK set (LEG_ID, CRUISE_PLATFORM_ID) =
(
  select L.ID, CP.ID from ${schema_name}.CURATORS_LEG L
                              inner join ${schema_name}.CURATORS_CRUISE C on L.CRUISE_ID = C.ID
                              inner join ${schema_name}.CURATORS_CRUISE_PLATFORM CP on C.ID = CP.CRUISE_ID
                              inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
  where L.LEG_NAME = LINK.LEG and P.PLATFORM = LINK.PLATFORM
)
where LINK.CRUISE_PLATFORM_ID is null and LINK.LEG is not null and LINK.LEG_ID is null
  and (LINK.PLATFORM, LINK.LEG) not in (select PLATFORM, LEG
  from (
      select P.PLATFORM as PLATFORM, L.LEG_NAME as LEG, count(*) as NUM
      from ${schema_name}.CURATORS_LEG L
               inner join ${schema_name}.CURATORS_CRUISE C on L.CRUISE_ID = C.ID
               inner join ${schema_name}.CURATORS_CRUISE_PLATFORM CP on C.ID = CP.CRUISE_ID
               inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
      where (P.PLATFORM, L.LEG_NAME) in (
          select distinct LINK.PLATFORM, LINK.LEG
          from ${schema_name}.CURATORS_CRUISE_LINKS LINK
          where LINK.CRUISE_PLATFORM_ID is null
            and LINK.LEG is not null
            and LINK.LEG_ID is null
      )
      group by P.PLATFORM, L.LEG_NAME
  ) where NUM > 1);

insert into ${schema_name}.CURATORS_CRUISE_LINKS_BROKEN_LINKS
( PLATFORM, CRUISE, LEG, DATALINK, LINK_LEVEL, LINK_SOURCE, LINK_TYPE, PUBLISH, PREVIOUS_STATE)
select CL.PLATFORM, CL.CRUISE, CL.LEG, CL.DATALINK, CL.LINK_LEVEL, CL.LINK_SOURCE, CL.LINK_TYPE, CL.PUBLISH, CL.PREVIOUS_STATE
from ${schema_name}.CURATORS_CRUISE_LINKS CL where CL.CRUISE_PLATFORM_ID is null or (CL.LEG is not null and CL.LEG_ID is null);

delete from ${schema_name}.CURATORS_CRUISE_LINKS where ROWID in (
    select distinct CL.ROWID from ${schema_name}.CURATORS_CRUISE_LINKS CL
    where CL.CRUISE_PLATFORM_ID is null or (CL.LEG is not null and CL.LEG_ID is null)
);

alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column PLATFORM;
alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column CRUISE;
alter table ${schema_name}.CURATORS_CRUISE_LINKS drop column LEG;

alter table ${schema_name}.CURATORS_CRUISE_LINKS
    modify (CRUISE_PLATFORM_ID number(19) constraint CURATORS_CRUISE_LINKS_CRUISE_PLATFORM_ID_NN not null);


update ${schema_name}.CURATORS_CRUISE_LINKS set LINK_LEVEL = lower(LINK_LEVEL);

