create table ${schema_name}.CURATORS_CRUISE (
    ID number(19) constraint CURATORS_CRUISE_ID_PK primary key,
    CRUISE_NAME varchar2(30) constraint CURATORS_CRUISE_PLATFORM_CRUISE_NAME_NN not null,
    YEAR number(5) constraint CURATORS_CRUISE_PLATFORM_YEAR_NN not null,
    PUBLISH VARCHAR2(1) constraint CURATORS_CRUISE_PUBLISH_NN not null,
    constraint CURATORS_CRUISE_CRUISE_NAME_YEAR_UK unique (CRUISE_NAME, YEAR)
);

create sequence ${schema_name}.CURATORS_CRUISE_SEQ nocache;

create or replace trigger ${schema_name}.CURATORS_CRUISE_BI
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

create table ${schema_name}.CURATORS_CRUISE_FACILITY (
    ID number(19) constraint CURATORS_CRUISE_FACILITY_PK primary key,
    CRUISE_ID number(19)
        constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_NN not null
        constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE,
    FACILITY_ID number(19)
        constraint CURATORS_CRUISE_FACILITY_FACILITY_ID_NN not null
        constraint CURATORS_CRUISE_FACILITY_FACILITY_ID_FK references ${schema_name}.CURATORS_FACILITY,
    PUBLISH VARCHAR2(1) constraint CURATORS_CRUISE_FACILITY_PUBLISH_NN not null,
    constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_FACILITY_ID_UK unique (CRUISE_ID, FACILITY_ID)
);

create sequence ${schema_name}.CURATORS_CRUISE_FACILITY_SEQ nocache;

create or replace trigger ${schema_name}.CURATORS_CRUISE_FACILITY_BI
    before insert
    on ${schema_name}.CURATORS_CRUISE_FACILITY
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_FACILITY_SEQ.NEXTVAL;
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


create table ${schema_name}.CURATORS_CRUISE_PLATFORM (
     ID number(19) constraint CURATORS_CRUISE_PLATFORM_PK primary key,
     CRUISE_ID number(19)
         constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_NN not null
         constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_FK references ${schema_name}.CURATORS_CRUISE,
     PLATFORM_ID number(19)
         constraint CURATORS_CRUISE_PLATFORM_PLATFORM_ID_NN not null
         constraint CURATORS_CRUISE_PLATFORM_PLATFORM_ID_FK references ${schema_name}.PLATFORM_MASTER,
     PUBLISH VARCHAR2(1) constraint CURATORS_CRUISE_PLATFORM_PUBLISH_NN not null,
     constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_PLATFORM_ID_UK unique (CRUISE_ID, PLATFORM_ID)
);

create sequence ${schema_name}.CURATORS_CRUISE_PLATFORM_SEQ nocache;

create or replace trigger ${schema_name}.CURATORS_CRUISE_PLATFORM_BI
    before insert
    on ${schema_name}.CURATORS_CRUISE_PLATFORM
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_PLATFORM_SEQ.NEXTVAL;
    end if;
end;
/


update ${schema_name}.CURATORS_SAMPLE_TSQP S set S.END_DATE = null where S.END_DATE = ' ';

insert into ${schema_name}.CURATORS_CRUISE (CRUISE_NAME, YEAR)
select CRUISE, min(YEAR) as YEAR
from (
         select S.CRUISE, to_number(substr(COALESCE(S.BEGIN_DATE, S.END_DATE, '9999'), 1, 4), 9999) as YEAR
         from ${schema_name}.CURATORS_SAMPLE_TSQP S
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
                            'unk'
             )
     ) group by CRUISE;

insert into ${schema_name}.CURATORS_CRUISE (CRUISE_NAME, YEAR)
select distinct CRUISE, YEAR from (
          select FACILITY_CODE, PLATFORM, CRUISE, min(YEAR) as YEAR
          from (
                   select distinct S.FACILITY_CODE,
                                 S.PLATFORM,
                                 S.CRUISE,
                                 to_number(substr(COALESCE(S.BEGIN_DATE, S.END_DATE, '9999'), 1, 4), 9999) as YEAR
                   from ${schema_name}.CURATORS_SAMPLE_TSQP S
              )
          group by FACILITY_CODE, PLATFORM, CRUISE
      ) where CRUISE not in (
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
                             'unk'
    );

insert into ${schema_name}.CURATORS_CRUISE_FACILITY (CRUISE_ID, FACILITY_ID)
select distinct C.ID, F.ID from ${schema_name}.CURATORS_SAMPLE_TSQP S
inner join ${schema_name}.CURATORS_FACILITY F on S.FACILITY_CODE = F.FACILITY_CODE
inner join ${schema_name}.CURATORS_CRUISE C on S.CRUISE = C.CRUISE_NAME
where C.CRUISE_NAME in (
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
                        'unk'
    );

insert into ${schema_name}.CURATORS_CRUISE_FACILITY (CRUISE_ID, FACILITY_ID)
select distinct C.ID, F.ID from (
  select FACILITY_CODE, PLATFORM, CRUISE, min(YEAR) as YEAR
  from (
           select distinct S.FACILITY_CODE,
                         S.PLATFORM,
                         S.CRUISE,
                         to_number(substr(COALESCE(S.BEGIN_DATE, S.END_DATE, '9999'), 1, 4), 9999) as YEAR
           from ${schema_name}.CURATORS_SAMPLE_TSQP S
           where S.CRUISE not in
                 (
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
                  'unk'
                     )
       )
  group by FACILITY_CODE, PLATFORM, CRUISE
) J
  inner join ${schema_name}.CURATORS_FACILITY F on J.FACILITY_CODE = F.FACILITY_CODE
  inner join ${schema_name}.CURATORS_CRUISE C on J.CRUISE = C.CRUISE_NAME and J.YEAR = C.YEAR;



insert into ${schema_name}.CURATORS_CRUISE_PLATFORM (CRUISE_ID, PLATFORM_ID)
select distinct C.ID, P.ID from ${schema_name}.CURATORS_SAMPLE_TSQP S
                                  inner join ${schema_name}.PLATFORM_MASTER P on S.PLATFORM = P.PLATFORM
                                  inner join ${schema_name}.CURATORS_CRUISE C on S.CRUISE = C.CRUISE_NAME
where C.CRUISE_NAME in (
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
                        'unk'
    );

insert into ${schema_name}.CURATORS_CRUISE_PLATFORM (CRUISE_ID, PLATFORM_ID)
select distinct C.ID, P.ID from (
    select FACILITY_CODE, PLATFORM, CRUISE, min(YEAR) as YEAR
    from (
           select distinct S.FACILITY_CODE,
                         S.PLATFORM,
                         S.CRUISE,
                         to_number(substr(COALESCE(S.BEGIN_DATE, S.END_DATE, '9999'), 1, 4), 9999) as YEAR
           from ${schema_name}.CURATORS_SAMPLE_TSQP S
           where S.CRUISE not in
                 (
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
                  'unk'
                     )
       )
    group by FACILITY_CODE, PLATFORM, CRUISE
    ) J
  inner join ${schema_name}.PLATFORM_MASTER P on J.PLATFORM = P.PLATFORM
  inner join ${schema_name}.CURATORS_CRUISE C on J.CRUISE = C.CRUISE_NAME and J.YEAR = C.YEAR;