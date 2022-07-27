alter table  ${schema_name}.CURATORS_SAMPLE_TSQP add
    (
    CRUISE_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE,
    CRUISE_PLATFORM_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_PLATFORM_ID_FK references ${schema_name}.CURATORS_CRUISE_PLATFORM,
    CRUISE_FACILITY_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_FACILITY_ID_FK references ${schema_name}.CURATORS_CRUISE_FACILITY,
    LEG_ID number(19) constraint CURATORS_SAMPLE_TSQP_LEG_ID_FK references ${schema_name}.CURATORS_LEG
    );

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_PLATFORM_ID =
  (
      select CP.ID from (
                            select FACILITY_CODE, PLATFORM, CRUISE, min(YEAR) as YEAR
                            from (
                                     select FACILITY_CODE,
                                            PLATFORM,
                                            CRUISE,
                                            to_number(substr(COALESCE(BEGIN_DATE, END_DATE, '9999'), 1, 4), 9999) as YEAR
                                     from ${schema_name}.CURATORS_SAMPLE_TSQP
                                     where FACILITY_CODE = S.FACILITY_CODE
                                       and PLATFORM = S.PLATFORM
                                       and CRUISE = S.CRUISE
                                 )
                            group by FACILITY_CODE, PLATFORM, CRUISE
                        ) J
                            inner join ${schema_name}.CURATORS_CRUISE C on J.CRUISE = C.CRUISE_NAME and J.YEAR = C.YEAR
                            inner join ${schema_name}.PLATFORM_MASTER P on J.PLATFORM = P.PLATFORM
                            inner join ${schema_name}.CURATORS_CRUISE_PLATFORM CP on C.ID = CP.CRUISE_ID and P.ID = CP.PLATFORM_ID
  )
where S.CRUISE not in (
                       'BAH',
                       'CONMAR',
                       'COOL',
                       'ELT06',
                       'ELT08',
                       'ELT16',
                       'ELT27',
                       'ELT37',
                       'ELT38',
                       'ELT42',
                       'ELT45',
                       'ELT55',
                       'GNP',
                       'H09198',
                       'H10073',
                       'H10077',
                       'H10157',
                       'ISSYK',
                       'MISSC',
                       'PANORAMA',
                       'PENN',
                       'PIERCE SEDIMENT COLLECTION',
                       'PLUTO',
                       'Santa Barbara',
                       'unk');

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_PLATFORM_ID =
  (
      select CP.ID from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
                            inner join ${schema_name}.CURATORS_CRUISE C on CP.CRUISE_ID = C.ID
                            inner join ${schema_name}.PLATFORM_MASTER P on CP.PLATFORM_ID = P.ID
      where C.CRUISE_NAME = S.CRUISE and P.PLATFORM = S.PLATFORM
  )
where S.CRUISE in (
                   'BAH',
                   'CONMAR',
                   'COOL',
                   'ELT06',
                   'ELT08',
                   'ELT16',
                   'ELT27',
                   'ELT37',
                   'ELT38',
                   'ELT42',
                   'ELT45',
                   'ELT55',
                   'GNP',
                   'H09198',
                   'H10073',
                   'H10077',
                   'H10157',
                   'ISSYK',
                   'MISSC',
                   'PANORAMA',
                   'PENN',
                   'PIERCE SEDIMENT COLLECTION',
                   'PLUTO',
                   'Santa Barbara',
                   'unk');

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_FACILITY_ID =
  (
      select CF.ID from (
                            select FACILITY_CODE, PLATFORM, CRUISE, min(YEAR) as YEAR
                            from (
                                     select FACILITY_CODE,
                                            PLATFORM,
                                            CRUISE,
                                            to_number(substr(COALESCE(BEGIN_DATE, END_DATE, '9999'), 1, 4), 9999) as YEAR
                                     from ${schema_name}.CURATORS_SAMPLE_TSQP
                                     where FACILITY_CODE = S.FACILITY_CODE
                                       and PLATFORM = S.PLATFORM
                                       and CRUISE = S.CRUISE
                                 )
                            group by FACILITY_CODE, PLATFORM, CRUISE
                        ) J
                            inner join ${schema_name}.CURATORS_CRUISE C on J.CRUISE = C.CRUISE_NAME and J.YEAR = C.YEAR
                            inner join ${schema_name}.CURATORS_FACILITY F on J.FACILITY_CODE = F.FACILITY_CODE
                            inner join ${schema_name}.CURATORS_CRUISE_FACILITY CF on C.ID = CF.CRUISE_ID and F.ID = CF.FACILITY_ID
  )
where S.CRUISE not in (
                       'BAH',
                       'CONMAR',
                       'COOL',
                       'ELT06',
                       'ELT08',
                       'ELT16',
                       'ELT27',
                       'ELT37',
                       'ELT38',
                       'ELT42',
                       'ELT45',
                       'ELT55',
                       'GNP',
                       'H09198',
                       'H10073',
                       'H10077',
                       'H10157',
                       'ISSYK',
                       'MISSC',
                       'PANORAMA',
                       'PENN',
                       'PIERCE SEDIMENT COLLECTION',
                       'PLUTO',
                       'Santa Barbara',
                       'unk');

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_FACILITY_ID =
  (
      select CF.ID from ${schema_name}.CURATORS_CRUISE_FACILITY CF
                            inner join ${schema_name}.CURATORS_CRUISE C on CF.CRUISE_ID = C.ID
                            inner join ${schema_name}.CURATORS_FACILITY F on CF.FACILITY_ID = F.ID
      where C.CRUISE_NAME = S.CRUISE and F.FACILITY_CODE = S.FACILITY_CODE
  )
where S.CRUISE in (
                   'BAH',
                   'CONMAR',
                   'COOL',
                   'ELT06',
                   'ELT08',
                   'ELT16',
                   'ELT27',
                   'ELT37',
                   'ELT38',
                   'ELT42',
                   'ELT45',
                   'ELT55',
                   'GNP',
                   'H09198',
                   'H10073',
                   'H10077',
                   'H10157',
                   'ISSYK',
                   'MISSC',
                   'PANORAMA',
                   'PENN',
                   'PIERCE SEDIMENT COLLECTION',
                   'PLUTO',
                   'Santa Barbara',
                   'unk');

update ${schema_name}.CURATORS_SAMPLE_TSQP S set CRUISE_ID =
  (
      select CF.CRUISE_ID from ${schema_name}.CURATORS_CRUISE_FACILITY CF where S.CRUISE_FACILITY_ID = CF.ID
  );


insert into ${schema_name}.CURATORS_LEG (CRUISE_ID, LEG_NAME)
select distinct S.CRUISE_ID, S.LEG from ${schema_name}.CURATORS_SAMPLE_TSQP S where S.LEG is not null;

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

-- -- there appears to be duplicates, talk to Clint
-- alter table  ${schema_name}.CURATORS_SAMPLE_TSQP
--     add constraint CURATORS_SAMPLE_TSQP_SAMPLE_CRUISE_FACILITY_ID_DEVICE_UK unique (SAMPLE, CRUISE_ID, DEVICE);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (LAT number(9, 5) constraint CURATORS_SAMPLE_LAT_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (LON number(10, 5) constraint CURATORS_SAMPLE_LON_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (PUBLISH varchar2(1) constraint CURATORS_SAMPLE_TSQP_PUBLISH_NN not null);

-- todo combine cored_length and cored_diam to a single floating point number

create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_BI BEFORE
    INSERT ON ${schema_name}.CURATORS_SAMPLE_TSQP FOR EACH ROW
declare
    leg_cruise_id number(19);
    cp_cruise_id number(19);
    cf_cruise_id number(19);
begin
    select CP.CRUISE_ID into cp_cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
    where :new.CRUISE_PLATFORM_ID = CP.ID;
    if :new.CRUISE_ID != cp_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-platform mapping');
    end if;
    select CF.CRUISE_ID into cf_cruise_id from ${schema_name}.CURATORS_CRUISE_FACILITY CF
    where :new.CRUISE_FACILITY_ID = CF.ID;
    if :new.CRUISE_ID != cf_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-facility mapping');
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
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
    cp_cruise_id number(19);
    cf_cruise_id number(19);
begin
    select CP.CRUISE_ID into cp_cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
    where :new.CRUISE_PLATFORM_ID = CP.ID;
    if :new.CRUISE_ID != cp_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-platform mapping');
    end if;
    select CF.CRUISE_ID into cf_cruise_id from ${schema_name}.CURATORS_CRUISE_FACILITY CF
    where :new.CRUISE_FACILITY_ID = CF.ID;
    if :new.CRUISE_ID != cf_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-facility mapping');
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
        where :new.LEG_ID = LEG.ID;
        if :new.CRUISE_ID != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/


alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (CRUISE_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (CRUISE_PLATFORM_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_PLATFORM_ID_NN not null);

alter table ${schema_name}.CURATORS_SAMPLE_TSQP
    modify (CRUISE_FACILITY_ID number(19) constraint CURATORS_SAMPLE_TSQP_CRUISE_FACILITY_ID_NN not null);

