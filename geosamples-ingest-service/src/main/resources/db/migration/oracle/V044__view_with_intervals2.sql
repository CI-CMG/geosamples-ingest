create or replace view ${schema_name}.CURATORS_SAMPLE_VSQP as
select
    CF.FACILITY_CODE as  FACILITY_CODE,
    CF.FACILITY as FACILITY,
    PM.PLATFORM as PLATFORM,
    CC.CRUISE_NAME as CRUISE,
    CS.SAMPLE as SAMPLE,
    CS.DEVICE as DEVICE,
    CS.BEGIN_DATE as BEGIN_DATE,
    CS.END_DATE as END_DATE,
    CS.LAT as LAT,
    CS.END_LAT as END_LAT,
    CS.LON as LON,
    CS.END_LON as END_LON,
    CS.WATER_DEPTH as WATER_DEPTH,
    CS.END_WATER_DEPTH as END_WATER_DEPTH,
    CS.STORAGE_METH as STORAGE_METH,
    CS.CORED_LENGTH as CORED_LENGTH,
    CS.CORED_LENGTH_MM as CORED_LENGTH_MM,
    CS.CORED_DIAM as CORED_DIAM,
    CS.CORED_DIAM_MM as CORED_DIAM_MM,
    CS.PI as PI,
    CS.PROVINCE as PROVINCE,
    CS.LAKE as LAKE,
    CS.OTHER_LINK as OTHER_LINK,
    CS.LAST_UPDATE as LAST_UPDATE,
    CS.IGSN as IGSN,
    CL.LEG_NAME as LEG,
    CS.SAMPLE_COMMENTS as SAMPLE_COMMENTS,
    CS.PUBLISH as PUBLISH,
    CAST ( SUBSTR(CS.IMLGS,6) as NUMBER(38) ) as OBJECTID,
    CS.SHAPE as SHAPE,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/samples/' || CS.IMLGS as SHOW_SAMPL,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/cruises/' || CC.CRUISE_NAME as CRUISE_URL,
    case when CL.LEG_NAME is null then null else 'https://maps.ngdc.noaa.gov/viewers/imlgs/cruises/' || CL.LEG_NAME end as LEG_URL,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/repositories/' || CF.FACILITY_CODE as REPOSITORY_URL,
    CS.IMLGS as IMLGS,
    (SELECT LISTAGG(V, '|') from
        (SELECT CI.COMP1 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP1 is not null
         UNION
         SELECT CI.COMP2 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP2 is not null
         UNION
         SELECT CI.COMP3 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP3 is not null
         UNION
         SELECT CI.COMP4 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP4 is not null
         UNION
         SELECT CI.COMP5 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP5 is not null
         UNION
         SELECT CI.COMP6 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.COMP6 is not null)
    ) as COMPOSITION,
    (SELECT LISTAGG(V, '|') from
        (SELECT CI.TEXT1 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.TEXT1 is not null
         UNION
         SELECT CI.TEXT2 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.TEXT2 is not null)
    ) as TEXTURE,
    (SELECT LISTAGG(V, '|') from
        (SELECT CI.LITH1 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.LITH1 is not null
         UNION
         SELECT CI.LITH2 as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.LITH2 is not null
         UNION
         SELECT CI.ROCK_LITH as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' and CI.ROCK_LITH is not null)
    ) as LITHOLOGY,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CI.ROCK_MIN as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y')) AS MINERALOGY,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CR.REMARK as V FROM ${schema_name}.CURATORS_INTERVAL CI, ${schema_name}.CURATORS_REMARK CR WHERE CI.IMLGS = CS.IMLGS AND CI.REMARK = CR.ID and CI.PUBLISH = 'Y')) AS REMARK,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CI.WEATH_META as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' AND CI.WEATH_META like 'weathering - %')) AS WEATHERING,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CI.WEATH_META as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y' AND CI.WEATH_META like 'metamorphism - %')) AS METAMORPHISM,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CI.AGE as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y')) AS AGE,
    (SELECT LISTAGG(V, '|') from (SELECT DISTINCT CI.IGSN as V FROM ${schema_name}.CURATORS_INTERVAL CI WHERE CI.IMLGS = CS.IMLGS AND CI.PUBLISH = 'Y')) AS INTERVAL_IGSN
from ${schema_name}.CURATORS_SAMPLE_TSQP CS
         inner join ${schema_name}.CURATORS_CRUISE_FACILITY CCF on CCF.ID = CS.CRUISE_FACILITY_ID
         inner join ${schema_name}.CURATORS_FACILITY CF on CF.ID = CCF.FACILITY_ID
         inner join ${schema_name}.CURATORS_CRUISE_PLATFORM CCP on CCP.ID = CS.CRUISE_PLATFORM_ID
         inner join ${schema_name}.PLATFORM_MASTER PM on CCP.PLATFORM_ID = PM.ID
         inner join ${schema_name}.CURATORS_CRUISE CC on CS.CRUISE_ID = CC.ID
         left outer join ${schema_name}.CURATORS_LEG CL on CS.LEG_ID = CL.ID
where CS.PUBLISH = 'Y'
/