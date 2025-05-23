create sequence ${schema_name}.R367
    increment by 16
    maxvalue 2147483647
/

create sequence ${schema_name}.SDE_LOGFILE_LID_GEN
/

create sequence ${schema_name}.R129
    increment by 16
    maxvalue 2147483647
/

create sequence ${schema_name}.R120
    increment by 16
    maxvalue 2147483647
/

create sequence ${schema_name}.R118
    increment by 16
    maxvalue 2147483647
/

create sequence ${schema_name}.R119
    increment by 16
    maxvalue 2147483647
/

create table ${schema_name}.NEWDECK
(
    MGGID               VARCHAR2(8),
    NODCREF             VARCHAR2(5),
    CONSECUTIVE_NO      VARCHAR2(4),
    NODC_SHIP           VARCHAR2(2),
    NODC_CRUISE         VARCHAR2(4),
    NODC_STATION        VARCHAR2(6),
    DEVICE              VARCHAR2(15),
    LATDEG              VARCHAR2(2),
    LATMIN              VARCHAR2(2),
    LATTEN              VARCHAR2(2),
    N_S                 VARCHAR2(1),
    LAT                 NUMBER(9, 5),
    LONDEG              VARCHAR2(3),
    LONMIN              VARCHAR2(2),
    LONTEN              VARCHAR2(2),
    E_W                 VARCHAR2(1),
    LON                 NUMBER(10, 5),
    WATER_DEPTH         NUMBER(5),
    DEPTH_DEVICE        VARCHAR2(30),
    DEPTH_CORRECTION    VARCHAR2(15),
    DEPTH_TOP_CM        VARCHAR2(4),
    DEPTH_BOTTOM_CM     VARCHAR2(4),
    ODOR                VARCHAR2(70),
    DESCRIPTION         VARCHAR2(255),
    DOMINANT_LITHOLOGY  VARCHAR2(100),
    SECONDARY_LITHOLOGY VARCHAR2(100),
    ANALYSIS_TYPE       VARCHAR2(10),
    BOTTOM_PHOTO        VARCHAR2(1),
    SAMPLE_PHOTO        VARCHAR2(1),
    MARSDEN_SQUARE      VARCHAR2(3),
    ONE_DEGREE_SQUARE   VARCHAR2(2),
    BEGIN_DATE          VARCHAR2(8),
    MGGLINK             VARCHAR2(100),
    SOURCE_REFERENCE    VARCHAR2(2000),
    PUBLISH             VARCHAR2(1) default 'Y',
    PREVIOUS_STATE      VARCHAR2(1)
)
/

create table ${schema_name}.NEWDECK_REFS
(
    NODCREF          VARCHAR2(5),
    SOURCE_REFERENCE VARCHAR2(2000),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_FACILITY
(
    INST_CODE        VARCHAR2(3),
    FACILITY_CODE    VARCHAR2(10) not null
        constraint CURATORS_FACILITY_PK
            primary key,
    FACILITY         VARCHAR2(50),
    ADDR_1           VARCHAR2(45),
    ADDR_2           VARCHAR2(45),
    ADDR_3           VARCHAR2(45),
    ADDR_4           VARCHAR2(45),
    CONTACT_1        VARCHAR2(45),
    CONTACT_2        VARCHAR2(45),
    CONTACT_3        VARCHAR2(45),
    EMAIL_LINK       VARCHAR2(45),
    URL_LINK         VARCHAR2(45),
    FTP_LINK         VARCHAR2(45),
    OTHER_LINK       VARCHAR2(45),
    FACILITY_COMMENT VARCHAR2(2000),
    LAST_UPDATE      NUMBER(8),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_WEATH_META
(
    WEATH_META_CODE VARCHAR2(1),
    WEATH_META      VARCHAR2(30) not null
        constraint CURATORS_WEATH_META_PK
            primary key,
    PUBLISH         VARCHAR2(1) default 'Y',
    PREVIOUS_STATE  VARCHAR2(1),
    SOURCE_URI      VARCHAR2(255)
)
/

create table ${schema_name}.GEOLIN_SAMPLE
(
    MGGID             VARCHAR2(8),
    CRUISE            VARCHAR2(30),
    SAMPLE            VARCHAR2(30),
    DEVICE_CODE       VARCHAR2(3),
    BEGIN_DATE        VARCHAR2(8),
    LAT               NUMBER(8, 4),
    LON               NUMBER(9, 4),
    WATER_DEPTH       NUMBER(5),
    DESCRIPTION       VARCHAR2(1),
    GRAIN_SIZE        VARCHAR2(1),
    GRAIN_STATS       VARCHAR2(1),
    MINERALOGY        VARCHAR2(1),
    ORGANIC_GEOCHEM   VARCHAR2(1),
    INORGANIC_GEOCHEM VARCHAR2(1),
    PALEONTOLOGY      VARCHAR2(1),
    GEOTECHNICAL      VARCHAR2(1),
    ACOUSTICS         VARCHAR2(1),
    AGE               VARCHAR2(1),
    SAMPLE_PHOTO      VARCHAR2(1),
    NEPHELOMETRY      VARCHAR2(1),
    RADIOACTIVITY     VARCHAR2(1),
    PHOTO_DESCRIPTION VARCHAR2(1),
    PETROLOGY         VARCHAR2(1),
    HEAT_FLOW         VARCHAR2(1),
    WELL_LOGS         VARCHAR2(1),
    MNNODULE_SET      VARCHAR2(1),
    PALEOMAGNETICS    VARCHAR2(1),
    GEOLIN_DEVICE     VARCHAR2(50),
    LATDEG_MIN_NS     VARCHAR2(20),
    LONDEG_MIN_EW     VARCHAR2(20),
    INST              VARCHAR2(255),
    SHIP              VARCHAR2(255),
    SEARCH_LAT        NUMBER,
    SEARCH_LON        NUMBER,
    LATLON_ORIG       VARCHAR2(1),
    CURATED           VARCHAR2(1),
    LAST_UPDATE       VARCHAR2(50),
    SCAN_LINK         VARCHAR2(500),
    SET_LINK          VARCHAR2(255),
    PUBLISH           VARCHAR2(1) default 'Y',
    PREVIOUS_STATE    VARCHAR2(1)
)
/

create index ${schema_name}.GEOLIN_SAMPLE_PHOTO_DESC
    on ${schema_name}.GEOLIN_SAMPLE (PHOTO_DESCRIPTION)
/

create index ${schema_name}.GEOLIN_SAMPLE_PETROLOGY
    on ${schema_name}.GEOLIN_SAMPLE (PETROLOGY)
/

create index ${schema_name}.GEOLIN_SAMPLE_HEAT_FLOW
    on ${schema_name}.GEOLIN_SAMPLE (HEAT_FLOW)
/

create index ${schema_name}.GEOLIN_SAMPLE_WELL_LOGS
    on ${schema_name}.GEOLIN_SAMPLE (WELL_LOGS)
/

create index ${schema_name}.GEOLIN_SAMPLE_MNNODULE_SET
    on ${schema_name}.GEOLIN_SAMPLE (MNNODULE_SET)
/

create index ${schema_name}.GEOLIN_SAMPLE_PALEOMAGNETICS
    on ${schema_name}.GEOLIN_SAMPLE (PALEOMAGNETICS)
/

create index ${schema_name}.GEOLIN_SAMPLE_INST
    on ${schema_name}.GEOLIN_SAMPLE (INST)
/

create index ${schema_name}.GEOLIN_SAMPLE_LATLON
    on ${schema_name}.GEOLIN_SAMPLE (LAT, LON)
/

create index ${schema_name}.GEOLIN_SAMPLE_WATER_DEPTH
    on ${schema_name}.GEOLIN_SAMPLE (WATER_DEPTH)
/

create index ${schema_name}.GEOLIN_SAMPLE_DEVICE_CODE
    on ${schema_name}.GEOLIN_SAMPLE (DEVICE_CODE)
/

create index ${schema_name}.GEOLIN_SAMPLE_BEGIN_DATE
    on ${schema_name}.GEOLIN_SAMPLE (BEGIN_DATE)
/

create index ${schema_name}.GEOLIN_SAMPLE_DESCRIPTION
    on ${schema_name}.GEOLIN_SAMPLE (DESCRIPTION)
/

create index ${schema_name}.GEOLIN_SAMPLE_GRAIN_SIZE
    on ${schema_name}.GEOLIN_SAMPLE (GRAIN_SIZE)
/

create index ${schema_name}.GEOLIN_SAMPLE_SIZE
    on ${schema_name}.GEOLIN_SAMPLE (GRAIN_SIZE, GRAIN_STATS)
/

create index ${schema_name}.GEOLIN_SAMPLE_MINERALOGY
    on ${schema_name}.GEOLIN_SAMPLE (MINERALOGY)
/

create index ${schema_name}.GEOLIN_SAMPLE_ORGANIC_GEOCHEM
    on ${schema_name}.GEOLIN_SAMPLE (ORGANIC_GEOCHEM)
/

create index ${schema_name}.GEOLIN_SAMPLE_INORG_GEOCHEM
    on ${schema_name}.GEOLIN_SAMPLE (INORGANIC_GEOCHEM)
/

create index ${schema_name}.GEOLIN_SAMPLE_PALEONTOLOGY
    on ${schema_name}.GEOLIN_SAMPLE (PALEONTOLOGY)
/

create index ${schema_name}.GEOLIN_SAMPLE_GEOTECHNICAL
    on ${schema_name}.GEOLIN_SAMPLE (GEOTECHNICAL)
/

create index ${schema_name}.GEOLIN_SAMPLE_ACOUSTICS
    on ${schema_name}.GEOLIN_SAMPLE (ACOUSTICS)
/

create index ${schema_name}.GEOLIN_SAMPLE_AGE
    on ${schema_name}.GEOLIN_SAMPLE (AGE)
/

create index ${schema_name}.GEOLIN_SAMPLE_SAMPLE_PHOTO
    on ${schema_name}.GEOLIN_SAMPLE (SAMPLE_PHOTO)
/

create index ${schema_name}.GEOLIN_SAMPLE_NEPHELOMETRY
    on ${schema_name}.GEOLIN_SAMPLE (NEPHELOMETRY)
/

create index ${schema_name}.GEOLIN_SAMPLE_RADIOACTIVITY
    on ${schema_name}.GEOLIN_SAMPLE (RADIOACTIVITY)
/

create index ${schema_name}.GEOLIN_SEARCH_LON
    on ${schema_name}.GEOLIN_SAMPLE (SEARCH_LON)
/

create index ${schema_name}.GEOLIN_LAT_INDEX
    on ${schema_name}.GEOLIN_SAMPLE (LAT)
/

create index ${schema_name}.GEOLIN_LON_INDEX
    on ${schema_name}.GEOLIN_SAMPLE (LON)
/

create index ${schema_name}.GEOLIN_SAMPLE_MGGID
    on ${schema_name}.GEOLIN_SAMPLE (MGGID)
/

create index ${schema_name}.GEOLIN_SEARCH_LAT
    on ${schema_name}.GEOLIN_SAMPLE (SEARCH_LAT)
/

create table ${schema_name}.TEMPINT
(
    FACILITY_CODE     VARCHAR2(10),
    SHIP_CODE         VARCHAR2(4),
    PLATFORM          VARCHAR2(50),
    CRUISE            VARCHAR2(30),
    SAMPLE            VARCHAR2(30),
    DEVICE            VARCHAR2(30),
    INTERVAL          NUMBER(6),
    DEPTH_TOP         NUMBER(6),
    DEPTH_TOP_MM      NUMBER(2),
    DEPTH_BOT         NUMBER(6),
    DEPTH_BOT_MM      NUMBER(2),
    DHCORE_ID         VARCHAR2(30),
    DHCORE_LENGTH     NUMBER(6),
    DHCORE_LENGTH_MM  NUMBER(2),
    DHCORE_INTERVAL   NUMBER(3),
    DTOP_IN_DHCORE    NUMBER(6),
    DTOP_MM_IN_DHCORE NUMBER(2),
    DBOT_IN_DHCORE    NUMBER(6),
    DBOT_MM_IN_DHCORE NUMBER(2),
    LITH1             VARCHAR2(40),
    TEXT1             VARCHAR2(40),
    LITH2             VARCHAR2(40),
    TEXT2             VARCHAR2(40),
    COMP1             VARCHAR2(40),
    COMP2             VARCHAR2(40),
    COMP3             VARCHAR2(40),
    COMP4             VARCHAR2(40),
    COMP5             VARCHAR2(40),
    COMP6             VARCHAR2(40),
    DESCRIPTION       VARCHAR2(2000),
    AGE               VARCHAR2(20),
    ABSOLUTE_AGE_TOP  VARCHAR2(50),
    ABSOLUTE_AGE_BOT  VARCHAR2(50),
    WEIGHT            NUMBER(8, 3),
    ROCK_LITH         VARCHAR2(100),
    ROCK_MIN          VARCHAR2(35),
    WEATH_META        VARCHAR2(30),
    REMARK            VARCHAR2(70),
    MUNSELL_CODE      VARCHAR2(10),
    MUNSELL           VARCHAR2(30),
    EXHAUST_CODE      VARCHAR2(1),
    PHOTO_LINK        VARCHAR2(500),
    LAKE              VARCHAR2(50),
    UNIT_NUMBER       VARCHAR2(50),
    INT_COMMENTS      VARCHAR2(2000),
    DHDEVICE          VARCHAR2(50),
    CMCD_TOP          NUMBER(6),
    MMCD_TOP          NUMBER(1),
    CMCD_BOT          NUMBER(6),
    MMCD_BOT          NUMBER(1),
    PUBLISH           VARCHAR2(1),
    PREVIOUS_STATE    VARCHAR2(1),
    IGSN              VARCHAR2(9),
    IMLGS             VARCHAR2(15),
    PARENT_IGSN       VARCHAR2(9)
)
/

create table ${schema_name}.SIZE_CRUISE
(
    MGGID                      VARCHAR2(8)  not null,
    SHIP_CODE                  VARCHAR2(4),
    SHIP                       VARCHAR2(40),
    CRUISE                     VARCHAR2(15) not null,
    MGG_UNIQUE                 VARCHAR2(4),
    CONTRIBUTOR                VARCHAR2(40),
    ORIGINAL_SHIP              VARCHAR2(40),
    SCIENTIST                  VARCHAR2(40),
    REPORT_ID                  VARCHAR2(30),
    FUNDING                    VARCHAR2(30),
    PROJECT                    VARCHAR2(30),
    TITLE                      VARCHAR2(255),
    DATE_ENTERED               VARCHAR2(8),
    UNITS_WET_UNIT_WT          VARCHAR2(10),
    UNITS_SOUND_VELOCITY       VARCHAR2(10),
    UNITS_COHESION             VARCHAR2(10),
    UNITS_COMPRESSIVE_STRENGTH VARCHAR2(10),
    UNITS_VANE_SIZE            VARCHAR2(10),
    UNITS_VANE_TURN_RATE       VARCHAR2(10),
    CRUISE_COMMENTS            VARCHAR2(255),
    MGGID_LINK                 VARCHAR2(255),
    SAMPLE_LINK                VARCHAR2(255),
    OTHER_LINK                 VARCHAR2(255),
    NOTES_LINK                 VARCHAR2(255),
    PUBLISH                    VARCHAR2(1) default 'Y',
    PREVIOUS_STATE             VARCHAR2(1),
    constraint TEXTCRUISE_PK
        primary key (MGGID, CRUISE)
)
/

create index ${schema_name}.SIZE_MGGC
    on ${schema_name}.SIZE_CRUISE (MGGID)
/

create table ${schema_name}.CURATORS_TEXTURE
(
    TEXTURE_CODE   VARCHAR2(1),
    TEXTURE        VARCHAR2(40) not null
        constraint CURATORS_TEXT_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.RESULTS
(
    SDO_ROWID ROWID,
    RESULT    VARCHAR2(1000)
)
/

create table ${schema_name}.MMBIB
(
    MMBIB          VARCHAR2(12) not null
        primary key,
    MGGID          VARCHAR2(255),
    AU             VARCHAR2(2000),
    YR             VARCHAR2(20),
    TI             VARCHAR2(2000),
    SIO            VARCHAR2(255),
    SER            VARCHAR2(2000),
    PUB            VARCHAR2(2000),
    PL             VARCHAR2(2000),
    VOL            VARCHAR2(2000),
    PG             VARCHAR2(2000),
    XR             VARCHAR2(2000),
    INREF          VARCHAR2(2000),
    PT             VARCHAR2(2000),
    LN             VARCHAR2(2000),
    DB             VARCHAR2(2000),
    HM             VARCHAR2(4),
    HDEP           VARCHAR2(2000),
    HAREA          VARCHAR2(2000),
    HEEZ           VARCHAR2(2000),
    HFEAT          VARCHAR2(2000),
    HSET           VARCHAR2(2000),
    HSUB           VARCHAR2(2000),
    HKEY           VARCHAR2(2000),
    MN             VARCHAR2(4),
    MDEP           VARCHAR2(2000),
    MAREA          VARCHAR2(2000),
    MEEZ           VARCHAR2(2000),
    MFEAT          VARCHAR2(2000),
    MSET           VARCHAR2(2000),
    MSUB           VARCHAR2(2000),
    MKEY           VARCHAR2(2000),
    PH             VARCHAR2(4),
    PDEP           VARCHAR2(2000),
    PAREA          VARCHAR2(2000),
    PEEZ           VARCHAR2(2000),
    PFEAT          VARCHAR2(2000),
    PSET           VARCHAR2(2000),
    PSUB           VARCHAR2(2000),
    PKEY           VARCHAR2(2000),
    PS             VARCHAR2(4),
    SDEP           VARCHAR2(2000),
    SAREA          VARCHAR2(2000),
    SEEZ           VARCHAR2(2000),
    SFEAT          VARCHAR2(2000),
    SSET           VARCHAR2(2000),
    SSUB           VARCHAR2(2000),
    SKEY           VARCHAR2(2000),
    ST             VARCHAR2(255),
    HOT            VARCHAR2(50),
    MOT            VARCHAR2(50),
    POT            VARCHAR2(50),
    SOT            VARCHAR2(50),
    LINK           VARCHAR2(4000),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_PROVINCE
(
    PROVINCE_CODE    VARCHAR2(2),
    PROVINCE         VARCHAR2(35) not null
        constraint CURATORS_PROV_PK
            primary key,
    PROVINCE_COMMENT VARCHAR2(40),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1),
    SOURCE_URI       VARCHAR2(255)
)
/

create table ${schema_name}.SIZE_SAMPLE
(
    MGGID           VARCHAR2(8)  not null,
    SHIP            VARCHAR2(40),
    CRUISE          VARCHAR2(15) not null,
    SAMPLE          VARCHAR2(15) not null,
    DEVICE_CODE     VARCHAR2(3)  not null,
    DEVICE          VARCHAR2(50),
    SAMPLE_YEAR     VARCHAR2(4),
    SAMPLE_MONTH    VARCHAR2(2),
    SAMPLE_DAY      VARCHAR2(2),
    SAMPLE_TIME     VARCHAR2(4),
    LATDEG          VARCHAR2(2),
    LATMIN          VARCHAR2(2),
    LATMINHUN       VARCHAR2(2),
    NS              VARCHAR2(1),
    LONDEG          VARCHAR2(3),
    LONMIN          VARCHAR2(2),
    LONMINHUN       VARCHAR2(2),
    EW              VARCHAR2(1),
    LATLON_ORIG     VARCHAR2(1),
    LAT             NUMBER(8, 4),
    LON             NUMBER(9, 4),
    WATER_DEPTH     NUMBER(5),
    CORE_LENGTH     NUMBER(6),
    DEVICE2         VARCHAR2(50),
    SAMPLE_COMMENTS VARCHAR2(255),
    MGGID_LINK      VARCHAR2(255),
    CRUISE_LINK     VARCHAR2(255),
    INTERVAL_LINK   VARCHAR2(255),
    PUBLISH         VARCHAR2(1) default 'Y',
    PREVIOUS_STATE  VARCHAR2(1),
    constraint TEXTSAMP_PK
        primary key (MGGID, CRUISE, SAMPLE, DEVICE_CODE)
)
/

create index ${schema_name}.SIZE_MGGCRUSAMPDEV
    on ${schema_name}.SIZE_SAMPLE (MGGID, CRUISE, SAMPLE, DEVICE)
/

create index ${schema_name}.SIZE_MGGCRUS
    on ${schema_name}.SIZE_SAMPLE (MGGID, CRUISE)
/

create index ${schema_name}.SIZE_MGGS
    on ${schema_name}.SIZE_SAMPLE (MGGID)
/

create table ${schema_name}.SIZE_INTERVAL
(
    MGGID                VARCHAR2(8)  not null,
    SHIP                 VARCHAR2(40),
    CRUISE               VARCHAR2(15) not null,
    SAMPLE               VARCHAR2(15) not null,
    DEVICE_CODE          VARCHAR2(3)  not null,
    DEVICE               VARCHAR2(50),
    SUBCORE              NUMBER(1)    not null,
    INTERVAL             NUMBER(2)    not null,
    REPLICATE            NUMBER(1)    not null,
    ANALYSIS_TYPE        VARCHAR2(3),
    DEPTH_TOP_CM         NUMBER(6),
    DEPTH_TOP_MM         NUMBER(2),
    DEPTH_BOT_CM         NUMBER(6),
    DEPTH_BOT_MM         NUMBER(2),
    TEST_DATE            VARCHAR2(8),
    TEST_TIME            VARCHAR2(4),
    TOTAL_WEIGHT         VARCHAR2(8),
    COARSE_METH          VARCHAR2(25),
    FINE_METH            VARCHAR2(25),
    COARSE_FINE_BOUNDARY VARCHAR2(10),
    COARSE_BOUNDARY      VARCHAR2(10),
    FINE_BOUNDARY        VARCHAR2(10),
    PCT_COARSER          NUMBER(5, 2),
    PCT_FINER            NUMBER(5, 2),
    PCT_GRAVEL           NUMBER(6, 3),
    PCT_SAND             NUMBER(6, 3),
    PCT_SILT             NUMBER(6, 3),
    PCT_CLAY             NUMBER(6, 3),
    PCT_MUD              NUMBER(6, 3),
    USC_GRAVEL           NUMBER(6, 3),
    USC_SAND             NUMBER(6, 3),
    USC_FINES            NUMBER(6, 3),
    METH_DESCRIPTION     VARCHAR2(11),
    MEAN_MM              NUMBER(7, 4),
    MEAN_PHI             NUMBER(7, 4),
    MEDIAN_PHI           NUMBER(7, 4),
    MODAL_PHI            NUMBER(7, 4),
    SKEWNESS             NUMBER(9, 4),
    KURTOSIS             NUMBER(9, 4),
    STD_DEV              NUMBER(9, 4),
    SORT_COEFF           NUMBER(5, 2),
    INTERVAL_COMMENTS    VARCHAR2(256),
    PUBLISH              VARCHAR2(1) default 'Y',
    PREVIOUS_STATE       VARCHAR2(1),
    constraint TEXTREPL_PK
        primary key (MGGID, CRUISE, SAMPLE, DEVICE_CODE, SUBCORE, INTERVAL, REPLICATE)
)
/

create index ${schema_name}.SIZE_MGGI
    on ${schema_name}.SIZE_INTERVAL (MGGID)
/

create index ${schema_name}.SIZE_MGGCRUSAMPDEVI
    on ${schema_name}.SIZE_INTERVAL (MGGID, CRUISE, SAMPLE, DEVICE)
/

create index ${schema_name}.SIZE_MGGCRUSAMPDEVSIR
    on ${schema_name}.SIZE_INTERVAL (MGGID, CRUISE, SAMPLE, DEVICE, SUBCORE, INTERVAL, REPLICATE)
/

create index ${schema_name}.SIZE_MGGCRUI
    on ${schema_name}.SIZE_INTERVAL (MGGID, CRUISE)
/

create table ${schema_name}.CURATORS_EXHAUST
(
    EXHAUST_CODE   VARCHAR2(1) not null
        constraint CURATORS_EXHAUST__CODE_PK
            primary key,
    EXHAUST        VARCHAR2(30),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_MUNSELL
(
    MUNSELL_CODE   VARCHAR2(10) not null
        constraint CURATORS_MUN_CODE_PK
            primary key,
    MUNSELL        VARCHAR2(30),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_REMARK
(
    REMARK_CODE    VARCHAR2(1),
    REMARK         VARCHAR2(70) not null
        constraint CURATORS_REMARK_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.PLATFORM
(
    INST_CODE      VARCHAR2(3),
    SHIP_CODE      VARCHAR2(4) not null
        constraint PLATFORM_PK
            primary key,
    PLATFORM       VARCHAR2(50),
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.CURATORS_AGE
(
    AGE_CODE       VARCHAR2(2),
    AGE            VARCHAR2(20) not null
        constraint CURATORS_AGE_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.CURATORS_DEVICE
(
    DEVICE_CODE    VARCHAR2(2),
    DEVICE         VARCHAR2(30) not null
        constraint CURATORS_DEVICE_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.PLATFORM_MASTER
(
    MASTER_ID      NUMBER(8),
    PLATFORM       VARCHAR2(50) not null
        constraint PLATFORM_MASTER_PK
            primary key,
    DATE_ADDED     DATE,
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1),
    PREFIX         VARCHAR2(30),
    ICES_CODE      VARCHAR2(4),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.CURATORS_CRUISE_LINKS
(
    PLATFORM       VARCHAR2(50)
        constraint CUR_CRUISE_LINKS_PLATFORM_FK
            references PLATFORM_MASTER,
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

create table ${schema_name}.SIZE_WT_PCT
(
    MGGID           VARCHAR2(8),
    SHIP            VARCHAR2(40),
    CRUISE          VARCHAR2(15),
    SAMPLE          VARCHAR2(15),
    DEVICE_CODE     VARCHAR2(3),
    DEVICE          VARCHAR2(50),
    SUBCORE         NUMBER(1),
    INTERVAL        NUMBER(2),
    REPLICATE       NUMBER(1),
    LOWER_PHI_LIMIT NUMBER(5, 2),
    UPPER_PHI_LIMIT NUMBER(5, 2),
    WT_PERCENT      NUMBER(5, 2),
    PUBLISH         VARCHAR2(1) default 'Y',
    PREVIOUS_STATE  VARCHAR2(1)
)
/

create index ${schema_name}.SIZE_MGGCRUSAMPDEVW
    on ${schema_name}.SIZE_WT_PCT (MGGID, CRUISE, SAMPLE, DEVICE)
/

create index ${schema_name}.SIZE_MGGCRUSAMPDEVSIRW
    on ${schema_name}.SIZE_WT_PCT (MGGID, CRUISE, SAMPLE, DEVICE, SUBCORE, INTERVAL, REPLICATE)
/

create index ${schema_name}.SIZE_MGGCRUW
    on ${schema_name}.SIZE_WT_PCT (MGGID, CRUISE)
/

create index ${schema_name}.SIZE_MGGW
    on ${schema_name}.SIZE_WT_PCT (MGGID)
/

create table ${schema_name}.CURATORS_ROCK_LITH
(
    ROCK_LITH_CODE VARCHAR2(2),
    ROCK_LITH      VARCHAR2(100) not null
        constraint CURATORS_RLITH_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.CURATORS_STORAGE_METH
(
    STORAGE_METH_CODE VARCHAR2(1),
    STORAGE_METH      VARCHAR2(35) not null
        constraint CURATORS_STORAGE_PK
            primary key,
    PUBLISH           VARCHAR2(1) default 'Y',
    PREVIOUS_STATE    VARCHAR2(1),
    SOURCE_URI        VARCHAR2(255)
)
/

create table ${schema_name}.CURATORS_SAMPLE_LINKS
(
    PLATFORM       VARCHAR2(50)
        constraint CUR_SAMPLE_LINKS_PLATFORM_FK
            references PLATFORM_MASTER,
    CRUISE         VARCHAR2(30),
    LEG            VARCHAR2(30),
    SAMPLE         VARCHAR2(30),
    DEVICE         VARCHAR2(30)
        constraint CUR_SAMPLE_LINKS_DEVICE_FK
            references CURATORS_DEVICE,
    DATALINK       VARCHAR2(500),
    LINK_LEVEL     VARCHAR2(30),
    LINK_SOURCE    VARCHAR2(30),
    LINK_TYPE      VARCHAR2(30),
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1),
    IGSN           VARCHAR2(9),
    IMLGS          VARCHAR2(15)
)
/

create table ${schema_name}.GEOLIN_SET
(
    MGGID               VARCHAR2(8) not null
        constraint GEOL_REP_PK
            primary key,
    INSTITUTION         VARCHAR2(255),
    TITLE               VARCHAR2(255),
    REPORT              VARCHAR2(255),
    FENUMBER            VARCHAR2(255),
    AUTHORS             VARCHAR2(255),
    SHIPS               VARCHAR2(255),
    CRUISES             VARCHAR2(255),
    AREAS               VARCHAR2(255),
    FUNDING             VARCHAR2(255),
    PROJECT             VARCHAR2(255),
    NUM_SAMPLES         VARCHAR2(255),
    CRUISE_DATES        VARCHAR2(255),
    BEGIN_YEAR          NUMBER(4),
    BEGIN_MONTH         NUMBER(2),
    BEGIN_DAY           NUMBER(2),
    END_YEAR            NUMBER(4),
    END_MONTH           NUMBER(2),
    END_DAY             NUMBER(2),
    DATE_ORIG           VARCHAR2(50),
    DATE_RECD           VARCHAR2(50),
    TAPES_OLD           VARCHAR2(255),
    TAPES_3480          VARCHAR2(255),
    DEVICES             VARCHAR2(255),
    ANALYSES            VARCHAR2(255),
    FORMATS             VARCHAR2(255),
    NUM_REELS           NUMBER(6),
    REEL_NOS            VARCHAR2(255),
    NUM_FICHE           NUMBER(6),
    NUM_PAGES           NUMBER(6),
    OTHER_FORMAT        VARCHAR2(255),
    FLIERS              VARCHAR2(255),
    URL                 VARCHAR2(255),
    LAST_UPDATE         VARCHAR2(50),
    SET_LINK            VARCHAR2(255),
    SCAN_LINK           VARCHAR2(500),
    SIZE_LINK           VARCHAR2(255),
    DECK41_LINK         VARCHAR2(335),
    PUBLISH             VARCHAR2(1) default 'Y',
    PREVIOUS_STATE      VARCHAR2(1),
    DATASET_IDENTIFIER  VARCHAR2(50),
    DATASET_URL         VARCHAR2(255),
    DATASET_DISPOSITION VARCHAR2(20),
    DATASET_COMMENTS    VARCHAR2(2000)
)
/

create index ${schema_name}.GEOLIN_SET_INSTITUTION
    on ${schema_name}.GEOLIN_SET (INSTITUTION)
/

create table ${schema_name}.CURATORS_LITHOLOGY
(
    LITHOLOGY_CODE VARCHAR2(1),
    LITHOLOGY      VARCHAR2(40) not null
        constraint CUR_LITH_PK
            primary key,
    OLD_LITHOLOGY  VARCHAR2(40),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.CURATORS_ROCK_MIN
(
    ROCK_MIN_CODE  VARCHAR2(1),
    ROCK_MIN       VARCHAR2(35) not null
        constraint CURATORS_MIN_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create table ${schema_name}.TEMPQC_INTERVAL
(
    FACILITY_CODE     VARCHAR2(10) not null
        constraint TEMPQC_INTERVAL_FACILITY_FK
            references CURATORS_FACILITY,
    SHIP_CODE         VARCHAR2(4),
    PLATFORM          VARCHAR2(50) not null
        constraint TEMPQC_INTERVAL_PLATFORM_FK
            references PLATFORM_MASTER,
    CRUISE            VARCHAR2(30) not null,
    SAMPLE            VARCHAR2(30) not null,
    DEVICE            VARCHAR2(30) not null
        constraint TEMPQC_INTERVAL_DEVICE_FK
            references CURATORS_DEVICE,
    INTERVAL          NUMBER(6)    not null,
    DEPTH_TOP         NUMBER(6),
    DEPTH_TOP_MM      NUMBER(2),
    DEPTH_BOT         NUMBER(6),
    DEPTH_BOT_MM      NUMBER(2),
    DHCORE_ID         VARCHAR2(30),
    DHCORE_LENGTH     NUMBER(6),
    DHCORE_LENGTH_MM  NUMBER(2),
    DHCORE_INTERVAL   NUMBER(3),
    DTOP_IN_DHCORE    NUMBER(6),
    DTOP_MM_IN_DHCORE NUMBER(2),
    DBOT_IN_DHCORE    NUMBER(6),
    DBOT_MM_IN_DHCORE NUMBER(2),
    LITH1             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_LITH1_FK
            references CURATORS_LITHOLOGY,
    TEXT1             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_TEXT1_FK
            references CURATORS_TEXTURE,
    LITH2             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_LITH2_FK
            references CURATORS_LITHOLOGY,
    TEXT2             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_TEXT2_FK
            references CURATORS_TEXTURE,
    COMP1             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP1_FK
            references CURATORS_LITHOLOGY,
    COMP2             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP2_FK
            references CURATORS_LITHOLOGY,
    COMP3             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP3_FK
            references CURATORS_LITHOLOGY,
    COMP4             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP4_FK
            references CURATORS_LITHOLOGY,
    COMP5             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP5_FK
            references CURATORS_LITHOLOGY,
    COMP6             VARCHAR2(40)
        constraint TEMPQC_INTERVAL_COMP6_FK
            references CURATORS_LITHOLOGY,
    DESCRIPTION       VARCHAR2(2000),
    AGE               VARCHAR2(20)
        constraint TEMPQC_INTERVAL_AGE_FK
            references CURATORS_AGE,
    ABSOLUTE_AGE_TOP  VARCHAR2(50),
    ABSOLUTE_AGE_BOT  VARCHAR2(50),
    WEIGHT            NUMBER(8, 3),
    ROCK_LITH         VARCHAR2(100)
        constraint TEMPQC_INTERVAL_RKLITH_FK
            references CURATORS_ROCK_LITH,
    ROCK_MIN          VARCHAR2(35)
        constraint TEMPQC_INTERVAL_RKMIN_FK
            references CURATORS_ROCK_MIN,
    WEATH_META        VARCHAR2(30)
        constraint TEMPQC_INTERVAL_WEATH_FK
            references CURATORS_WEATH_META,
    REMARK            VARCHAR2(70)
        constraint TEMPQC_INTERVAL_REMARK_FK
            references CURATORS_REMARK,
    MUNSELL_CODE      VARCHAR2(10),
    MUNSELL           VARCHAR2(30),
    EXHAUST_CODE      VARCHAR2(1),
    PHOTO_LINK        VARCHAR2(500),
    LAKE              VARCHAR2(50),
    UNIT_NUMBER       VARCHAR2(50),
    INT_COMMENTS      VARCHAR2(2000),
    DHDEVICE          VARCHAR2(50),
    CMCD_TOP          NUMBER(6),
    MMCD_TOP          NUMBER(1),
    CMCD_BOT          NUMBER(6),
    MMCD_BOT          NUMBER(1),
    PUBLISH           VARCHAR2(1),
    PREVIOUS_STATE    VARCHAR2(1),
    IGSN              VARCHAR2(9),
    IMLGS             VARCHAR2(15),
    PARENT_IGSN       VARCHAR2(9),
    constraint TEMPQC_INTERVAL_PK
        primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE, INTERVAL)
)
/

create table ${schema_name}.CURATORS_LAKE
(
    LAKE             VARCHAR2(255),
    CONTINENT        VARCHAR2(30),
    COUNTRY          VARCHAR2(30),
    STATE            VARCHAR2(30),
    COUNTY           VARCHAR2(30),
    ELEVATION        NUMBER(7),
    LAKE_TYPE        VARCHAR2(30),
    HYDROLOGY        VARCHAR2(30),
    SALINITY         VARCHAR2(30),
    TDS              VARCHAR2(30),
    BRINE_TYPE       VARCHAR2(30),
    TROPHIC_STATE    VARCHAR2(30),
    CATCHMENT        VARCHAR2(30),
    SURFACE_AREA     VARCHAR2(30),
    VOLUME           VARCHAR2(10),
    MAX_DEPTH        NUMBER(7),
    MEAN_DEPTH       NUMBER(7),
    SHORELINE_LENGTH NUMBER(10),
    PH               VARCHAR2(10),
    MIXING           VARCHAR2(20),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1)
)
/

create index ${schema_name}.CURLAKE_LAKE
    on ${schema_name}.CURATORS_LAKE (LAKE)
/

create table ${schema_name}.INST
(
    INST_CODE      VARCHAR2(3) not null
        constraint INST_CODE_PK
            primary key,
    ABBRV          VARCHAR2(8),
    COUNTRY_CODE   NUMBER(3),
    INSTITUTION    VARCHAR2(50),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.MARINE_GEOLOGY_TSQP
(
    OBJECTID          NUMBER(38)         not null
        primary key,
    LATITUDE          NUMBER(9, 5)       not null,
    LONGITUDE         NUMBER(10, 5)      not null,
    MGGID             VARCHAR2(8)        not null,
    SHIP              VARCHAR2(30),
    CRUISE            VARCHAR2(30),
    SAMPLE            VARCHAR2(30),
    DEVICE            VARCHAR2(35),
    YEARMODA          VARCHAR2(8),
    WDEPTH            NUMBER(5),
    ANALYSES          VARCHAR2(255),
    DESCRIPTION       VARCHAR2(1),
    GRAIN_SIZE        VARCHAR2(1),
    GRAIN_STATS       VARCHAR2(1),
    MINERALOGY        VARCHAR2(1),
    ORGANIC_GEOCHEM   VARCHAR2(1),
    INORGANIC_GEOCHEM VARCHAR2(1),
    PALEONTOLOGY      VARCHAR2(1),
    GEOTECHNICAL      VARCHAR2(1),
    ACOUSTICS         VARCHAR2(1),
    AGE               VARCHAR2(1),
    SAMPLE_PHOTO      VARCHAR2(1),
    NEPHELOMETRY      VARCHAR2(1),
    RADIOACTIVITY     VARCHAR2(1),
    PHOTO_DESCRIPTION VARCHAR2(1),
    PETROLOGY         VARCHAR2(1),
    HEAT_FLOW         VARCHAR2(1),
    WELL_LOGS         VARCHAR2(1),
    MNNODULE_SET      VARCHAR2(1),
    PALEOMAGNETICS    VARCHAR2(1),
    TITLE             VARCHAR2(255),
    FGDCID            VARCHAR2(10),
    SHAPE             MDSYS.SDO_GEOMETRY not null,
    IGSN              VARCHAR2(9),
    PUBLISH           VARCHAR2(1) default 'Y',
    PREVIOUS_STATE    VARCHAR2(1)
)
/

insert into MDSYS.USER_SDO_GEOM_METADATA (table_name, column_name, diminfo, srid)
values ('MARINE_GEOLOGY_TSQP', 'SHAPE',
        MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Longitude', -180, 180, 0.05), MDSYS.SDO_DIM_ELEMENT('Latitude', -90, 90, 0.05)), 8307);


create index ${schema_name}.MARINE_GEOLOGY_TSQP_SPX
    on ${schema_name}.MARINE_GEOLOGY_TSQP (SHAPE)
    indextype is MDSYS.SPATIAL_INDEX
/

create table ${schema_name}.CURATORS_SAMPLE_WEBM_TSQP
(
    FACILITY_CODE   VARCHAR2(10) not null,
    SHIP_CODE       VARCHAR2(4),
    PLATFORM        VARCHAR2(50) not null,
    CRUISE          VARCHAR2(30) not null,
    SAMPLE          VARCHAR2(30) not null,
    DEVICE          VARCHAR2(30) not null,
    BEGIN_DATE      VARCHAR2(8),
    END_DATE        VARCHAR2(8),
    LAT             NUMBER(9, 5),
    LATDEG          NUMBER(2),
    LATMIN          VARCHAR2(5),
    NS              VARCHAR2(1),
    END_LAT         NUMBER(9, 5),
    END_LATDEG      NUMBER(2),
    END_LATMIN      VARCHAR2(5),
    END_NS          VARCHAR2(1),
    LON             NUMBER(10, 5),
    LONDEG          NUMBER(3),
    LONMIN          VARCHAR2(5),
    EW              VARCHAR2(1),
    END_LON         NUMBER(10, 5),
    END_LONDEG      NUMBER(3),
    END_LONMIN      VARCHAR2(5),
    END_EW          VARCHAR2(1),
    LATLON_ORIG     VARCHAR2(1),
    WATER_DEPTH     NUMBER(5),
    END_WATER_DEPTH NUMBER(5),
    STORAGE_METH    VARCHAR2(35),
    CORED_LENGTH    NUMBER(6),
    CORED_LENGTH_MM NUMBER(2),
    CORED_DIAM      NUMBER(3),
    CORED_DIAM_MM   NUMBER(2),
    PI              VARCHAR2(255),
    PROVINCE        VARCHAR2(35),
    LAKE            VARCHAR2(50),
    OTHER_LINK      VARCHAR2(500),
    LAST_UPDATE     VARCHAR2(8),
    IGSN            VARCHAR2(9),
    LEG             VARCHAR2(30),
    SAMPLE_COMMENTS VARCHAR2(2000),
    PUBLISH         VARCHAR2(1),
    PREVIOUS_STATE  VARCHAR2(1),
    OBJECTID        NUMBER(38)   not null,
    SHAPE           MDSYS.SDO_GEOMETRY,
    SHOW_SAMPL      VARCHAR2(254),
    constraint CURATORS_SAMPTSQP_PK1
        primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE)
)
/

insert into MDSYS.USER_SDO_GEOM_METADATA (table_name, column_name, diminfo, srid)
values ('CURATORS_SAMPLE_WEBM_TSQP', 'SHAPE',
        MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Longitude', -180, 180, 0.05), MDSYS.SDO_DIM_ELEMENT('Latitude', -90, 90, 0.05)), 3857);

create index ${schema_name}.CURATORS_SAMPLE_WEBM_TSQP_SPX
    on ${schema_name}.CURATORS_SAMPLE_WEBM_TSQP (SHAPE)
    indextype is MDSYS.SPATIAL_INDEX
/

create table ${schema_name}.TEMPQC_SAMPLE
(
    FACILITY_CODE   VARCHAR2(10) not null
        constraint TEMPQC_SAMPLE_FACILITY_FK
            references CURATORS_FACILITY,
    SHIP_CODE       VARCHAR2(4),
    PLATFORM        VARCHAR2(50) not null
        constraint TEMPQC_SAMPLE_PLATFORM_FK
            references PLATFORM_MASTER,
    CRUISE          VARCHAR2(30) not null,
    SAMPLE          VARCHAR2(30) not null,
    DEVICE          VARCHAR2(30) not null
        constraint TEMPQC_SAMPLE_DEVICE_FK
            references CURATORS_DEVICE,
    BEGIN_DATE      VARCHAR2(8),
    END_DATE        VARCHAR2(8),
    LAT             NUMBER(9, 5),
    LATDEG          NUMBER(2),
    LATMIN          VARCHAR2(5),
    NS              VARCHAR2(1),
    END_LAT         NUMBER(9, 5),
    END_LATDEG      NUMBER(2),
    END_LATMIN      VARCHAR2(5),
    END_NS          VARCHAR2(1),
    LON             NUMBER(10, 5),
    LONDEG          NUMBER(3),
    LONMIN          VARCHAR2(5),
    EW              VARCHAR2(1),
    END_LON         NUMBER(10, 5),
    END_LONDEG      NUMBER(3),
    END_LONMIN      VARCHAR2(5),
    END_EW          VARCHAR2(1),
    LATLON_ORIG     VARCHAR2(1),
    WATER_DEPTH     NUMBER(5),
    END_WATER_DEPTH NUMBER(5),
    STORAGE_METH    VARCHAR2(35)
        constraint TEMPQC_SAMPLE_STORAGE_FK
            references CURATORS_STORAGE_METH,
    CORED_LENGTH    NUMBER(6),
    CORED_LENGTH_MM NUMBER(2),
    CORED_DIAM      NUMBER(3),
    CORED_DIAM_MM   NUMBER(2),
    PI              VARCHAR2(255),
    PROVINCE        VARCHAR2(35)
        constraint TEMPQC_SAMPLE_PROVINCE_FK
            references CURATORS_PROVINCE,
    LAKE            VARCHAR2(50),
    OTHER_LINK      VARCHAR2(500),
    LAST_UPDATE     VARCHAR2(8),
    IGSN            VARCHAR2(9),
    LEG             VARCHAR2(30),
    SAMPLE_COMMENTS VARCHAR2(2000),
    PUBLISH         VARCHAR2(1),
    PREVIOUS_STATE  VARCHAR2(1),
    OBJECTID        NUMBER(38),
    SHAPE           MDSYS.SDO_GEOMETRY,
    SHOW_SAMPL      VARCHAR2(254),
    IMLGS           VARCHAR2(15),
    constraint TEMPQC_SAMPLE_PK
        primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE)
)
/

create table ${schema_name}.TEMPSMP
(
    FACILITY_CODE   VARCHAR2(10),
    SHIP_CODE       VARCHAR2(4),
    PLATFORM        VARCHAR2(50),
    CRUISE          VARCHAR2(30),
    SAMPLE          VARCHAR2(30),
    DEVICE          VARCHAR2(30),
    BEGIN_DATE      VARCHAR2(8),
    END_DATE        VARCHAR2(8),
    LAT             NUMBER(9, 5),
    LATDEG          NUMBER(2),
    LATMIN          VARCHAR2(5),
    NS              VARCHAR2(1),
    END_LAT         NUMBER(9, 5),
    END_LATDEG      NUMBER(2),
    END_LATMIN      VARCHAR2(5),
    END_NS          VARCHAR2(1),
    LON             NUMBER(10, 5),
    LONDEG          NUMBER(3),
    LONMIN          VARCHAR2(5),
    EW              VARCHAR2(1),
    END_LON         NUMBER(10, 5),
    END_LONDEG      NUMBER(3),
    END_LONMIN      VARCHAR2(5),
    END_EW          VARCHAR2(1),
    LATLON_ORIG     VARCHAR2(1),
    WATER_DEPTH     NUMBER(5),
    END_WATER_DEPTH NUMBER(5),
    STORAGE_METH    VARCHAR2(35),
    CORED_LENGTH    NUMBER(6),
    CORED_LENGTH_MM NUMBER(2),
    CORED_DIAM      NUMBER(3),
    CORED_DIAM_MM   NUMBER(2),
    PI              VARCHAR2(255),
    PROVINCE        VARCHAR2(35),
    LAKE            VARCHAR2(50),
    OTHER_LINK      VARCHAR2(500),
    LAST_UPDATE     VARCHAR2(8),
    IGSN            VARCHAR2(9),
    LEG             VARCHAR2(30),
    SAMPLE_COMMENTS VARCHAR2(2000),
    PUBLISH         VARCHAR2(1),
    PREVIOUS_STATE  VARCHAR2(1),
    OBJECTID        NUMBER(38),
    SHAPE           MDSYS.SDO_GEOMETRY,
    SHOW_SAMPL      VARCHAR2(254),
    IMLGS           VARCHAR2(15)
)
/

create table ${schema_name}.CURATORS_SAMPLE_TSQP
(
    FACILITY_CODE   VARCHAR2(10)
        constraint CURATORS_SAMPTSQP_FACILITY_FK
            references CURATORS_FACILITY
        constraint CURATORS_SAMPLE_TSQP_FACILITY_CODE_NN
            check (FACILITY_CODE is not null),
    SHIP_CODE       VARCHAR2(4),
    PLATFORM        VARCHAR2(50)
        constraint CURATORS_SAMPTSQP_PLATFORM_FK
            references PLATFORM_MASTER
        constraint CURATORS_SAMPLE_TSQP_PLATFORM_NN
            check (PLATFORM is not null),
    CRUISE          VARCHAR2(30)
        constraint CURATORS_SAMPLE_TSQP_CRUISE_NN
            check (CRUISE is not null),
    SAMPLE          VARCHAR2(30)
        constraint CURATORS_SAMPLE_TSQP_SAMPLE_NN
            check (SAMPLE is not null),
    DEVICE          VARCHAR2(30)
        constraint CURATORS_SAMPTSQP_DEVICE_FK
            references CURATORS_DEVICE
        constraint CURATORS_SAMPLE_TSQP_DEVICE_NN
            check (DEVICE is not null),
    BEGIN_DATE      VARCHAR2(8),
    END_DATE        VARCHAR2(8),
    LAT             NUMBER(9, 5),
    LATDEG          NUMBER(2),
    LATMIN          VARCHAR2(5),
    NS              VARCHAR2(1),
    END_LAT         NUMBER(9, 5),
    END_LATDEG      NUMBER(2),
    END_LATMIN      VARCHAR2(5),
    END_NS          VARCHAR2(1),
    LON             NUMBER(10, 5),
    LONDEG          NUMBER(3),
    LONMIN          VARCHAR2(5),
    EW              VARCHAR2(1),
    END_LON         NUMBER(10, 5),
    END_LONDEG      NUMBER(3),
    END_LONMIN      VARCHAR2(5),
    END_EW          VARCHAR2(1),
    LATLON_ORIG     VARCHAR2(1),
    WATER_DEPTH     NUMBER(5),
    END_WATER_DEPTH NUMBER(5),
    STORAGE_METH    VARCHAR2(35)
        constraint CURATORS_SAMPTSQP_STORAGE_FK
            references CURATORS_STORAGE_METH,
    CORED_LENGTH    NUMBER(6),
    CORED_LENGTH_MM NUMBER(2),
    CORED_DIAM      NUMBER(3),
    CORED_DIAM_MM   NUMBER(2),
    PI              VARCHAR2(255),
    PROVINCE        VARCHAR2(35)
        constraint CURATORS_SAMPTSQP_PROVINCE_FK
            references CURATORS_PROVINCE,
    LAKE            VARCHAR2(50),
    OTHER_LINK      VARCHAR2(500),
    LAST_UPDATE     VARCHAR2(8),
    IGSN            VARCHAR2(9)
        constraint CURATORS_SAMPLE_TSQP_IGSN_UK
            unique,
    LEG             VARCHAR2(30),
    SAMPLE_COMMENTS VARCHAR2(2000),
    PUBLISH         VARCHAR2(1),
    PREVIOUS_STATE  VARCHAR2(1),
    OBJECTID        NUMBER(38)   not null
        constraint CURATORS_SAMPLE_TSQP_UK
            unique
        constraint CURATORS_SAMPLE_TSQP_OBJECTID_NN
            check (OBJECTID is not null),
    SHAPE           MDSYS.SDO_GEOMETRY,
    SHOW_SAMPL      VARCHAR2(254),
    IMLGS           VARCHAR2(15) not null
        constraint CURATORS_SAMPTSQP_PK
            primary key,
    constraint CURATORS_SAMPTSQP_UK
        unique (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE)
)
/

create table ${schema_name}.CURATORS_INTERVAL
(
    FACILITY_CODE     VARCHAR2(10)
        constraint CURATORS_INTERVAL_FACILITY_FK
            references CURATORS_FACILITY
        constraint CURATORS_INTERVAL_FACILITY_CODE_NN
            check (FACILITY_CODE is not null),
    SHIP_CODE         VARCHAR2(4),
    PLATFORM          VARCHAR2(50)
        constraint CURATORS_INTERVAL_PLATFORM_FK
            references PLATFORM_MASTER
        constraint CURATORS_INTERVAL_PLATFORM_NN
            check (PLATFORM is not null),
    CRUISE            VARCHAR2(30)
        constraint CURATORS_INTERVAL_CRUISE_NN
            check (CRUISE is not null),
    SAMPLE            VARCHAR2(30),
    DEVICE            VARCHAR2(30)
        constraint CURATORS_INTERVAL_DEVICE_FK
            references CURATORS_DEVICE
        constraint CURATORS_INTERVAL_DEVICE_NN
            check (DEVICE is not null),
    INTERVAL          NUMBER(6)    not null,
    DEPTH_TOP         NUMBER(6),
    DEPTH_TOP_MM      NUMBER(2),
    DEPTH_BOT         NUMBER(6),
    DEPTH_BOT_MM      NUMBER(2),
    DHCORE_ID         VARCHAR2(30),
    DHCORE_LENGTH     NUMBER(6),
    DHCORE_LENGTH_MM  NUMBER(2),
    DHCORE_INTERVAL   NUMBER(3),
    DTOP_IN_DHCORE    NUMBER(6),
    DTOP_MM_IN_DHCORE NUMBER(2),
    DBOT_IN_DHCORE    NUMBER(6),
    DBOT_MM_IN_DHCORE NUMBER(2),
    LITH1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_LITH1_FK
            references CURATORS_LITHOLOGY,
    TEXT1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_TEXT1_FK
            references CURATORS_TEXTURE,
    LITH2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_LITH2_FK
            references CURATORS_LITHOLOGY,
    TEXT2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_TEXT2_FK
            references CURATORS_TEXTURE,
    COMP1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP1_FK
            references CURATORS_LITHOLOGY,
    COMP2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP2_FK
            references CURATORS_LITHOLOGY,
    COMP3             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP3_FK
            references CURATORS_LITHOLOGY,
    COMP4             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP4_FK
            references CURATORS_LITHOLOGY,
    COMP5             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP5_FK
            references CURATORS_LITHOLOGY,
    COMP6             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP6_FK
            references CURATORS_LITHOLOGY,
    DESCRIPTION       VARCHAR2(2000),
    AGE               VARCHAR2(20)
        constraint CURATORS_INTERVAL_AGE_FK
            references CURATORS_AGE,
    ABSOLUTE_AGE_TOP  VARCHAR2(50),
    ABSOLUTE_AGE_BOT  VARCHAR2(50),
    WEIGHT            NUMBER(8, 3),
    ROCK_LITH         VARCHAR2(100)
        constraint CURATORS_INTERVAL_RKLITH_FK
            references CURATORS_ROCK_LITH,
    ROCK_MIN          VARCHAR2(35)
        constraint CURATORS_INTERVAL_RKMIN_FK
            references CURATORS_ROCK_MIN,
    WEATH_META        VARCHAR2(30)
        constraint CURATORS_INTERVAL_WEATH_FK
            references CURATORS_WEATH_META,
    REMARK            VARCHAR2(70)
        constraint CURATORS_INTERVAL_REMARK_FK
            references CURATORS_REMARK,
    MUNSELL_CODE      VARCHAR2(10),
    MUNSELL           VARCHAR2(30),
    EXHAUST_CODE      VARCHAR2(1),
    PHOTO_LINK        VARCHAR2(500),
    LAKE              VARCHAR2(50),
    UNIT_NUMBER       VARCHAR2(50),
    INT_COMMENTS      VARCHAR2(2000),
    DHDEVICE          VARCHAR2(50),
    CMCD_TOP          NUMBER(6),
    MMCD_TOP          NUMBER(1),
    CMCD_BOT          NUMBER(6),
    MMCD_BOT          NUMBER(1),
    PUBLISH           VARCHAR2(1),
    PREVIOUS_STATE    VARCHAR2(1),
    IGSN              VARCHAR2(9),
    IMLGS             VARCHAR2(15) not null
        constraint CURATORS_INTERVAL_IMLGS_FK
            references CURATORS_SAMPLE_TSQP,
    PARENT_IGSN       VARCHAR2(9)
        constraint CUR_INTERVAL_IGSN_FK
            references CURATORS_SAMPLE_TSQP (IGSN),
    constraint CURATORS_INTERVAL_PK
        primary key (IMLGS, INTERVAL),
    constraint CURATORS_INTERVAL_UK
        unique (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE, INTERVAL)
)
/

insert into MDSYS.USER_SDO_GEOM_METADATA (table_name, column_name, diminfo, srid)
values ('CURATORS_SAMPLE_TSQP', 'SHAPE',
        MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Longitude', -180, 180, 0.05), MDSYS.SDO_DIM_ELEMENT('Latitude', -90, 90, 0.05)), 4326);


create index ${schema_name}.CURATORS_SAMPLE_TSQP_SPX
    on ${schema_name}.CURATORS_SAMPLE_TSQP (SHAPE)
    indextype is MDSYS.SPATIAL_INDEX
/

create table ${schema_name}.TEMP_GEOLOGY_TSQP
(
    OBJECTID          NUMBER(38),
    LATITUDE          NUMBER(9, 5)  not null,
    LONGITUDE         NUMBER(10, 5) not null,
    MGGID             VARCHAR2(8)   not null,
    SHIP              VARCHAR2(30),
    CRUISE            VARCHAR2(30),
    SAMPLE            VARCHAR2(30),
    DEVICE            VARCHAR2(35),
    YEARMODA          VARCHAR2(8),
    WDEPTH            NUMBER(5),
    ANALYSES          VARCHAR2(255),
    DESCRIPTION       VARCHAR2(1),
    GRAIN_SIZE        VARCHAR2(1),
    GRAIN_STATS       VARCHAR2(1),
    MINERALOGY        VARCHAR2(1),
    ORGANIC_GEOCHEM   VARCHAR2(1),
    INORGANIC_GEOCHEM VARCHAR2(1),
    PALEONTOLOGY      VARCHAR2(1),
    GEOTECHNICAL      VARCHAR2(1),
    ACOUSTICS         VARCHAR2(1),
    AGE               VARCHAR2(1),
    SAMPLE_PHOTO      VARCHAR2(1),
    NEPHELOMETRY      VARCHAR2(1),
    RADIOACTIVITY     VARCHAR2(1),
    PHOTO_DESCRIPTION VARCHAR2(1),
    PETROLOGY         VARCHAR2(1),
    HEAT_FLOW         VARCHAR2(1),
    WELL_LOGS         VARCHAR2(1),
    MNNODULE_SET      VARCHAR2(1),
    PALEOMAGNETICS    VARCHAR2(1),
    TITLE             VARCHAR2(255),
    FGDCID            VARCHAR2(10),
    SHAPE             MDSYS.SDO_GEOMETRY,
    IGSN              VARCHAR2(9),
    PUBLISH           VARCHAR2(1),
    PREVIOUS_STATE    VARCHAR2(1)
)
/

create table ${schema_name}.DECK41_TSQP
(
    OBJECTID       NUMBER(38)         not null
        primary key,
    SETLINK        VARCHAR2(9)        not null,
    MGGID          VARCHAR2(8),
    LATITUDE       NUMBER(9, 5)       not null,
    LONGITUDE      NUMBER(10, 5)      not null,
    LITH1          VARCHAR2(254),
    LITH2          VARCHAR2(254),
    DESC_          VARCHAR2(254),
    STATIONID      VARCHAR2(254),
    DEVICE         VARCHAR2(254),
    YEARMODA       VARCHAR2(8),
    WDEPTH         NUMBER(5),
    SHAPE          MDSYS.SDO_GEOMETRY not null,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

insert into MDSYS.USER_SDO_GEOM_METADATA (table_name, column_name, diminfo, srid)
values ('DECK41_TSQP', 'SHAPE',
        MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Longitude', -180, 180, 0.05), MDSYS.SDO_DIM_ELEMENT('Latitude', -90, 90, 0.05)), 8307);


create index ${schema_name}.DECK41_TSQP_SPX
    on ${schema_name}.DECK41_TSQP (SHAPE)
    indextype is MDSYS.SPATIAL_INDEX
/

create table ${schema_name}.NEUMANN
(
    SURVEY         VARCHAR2(30) not null,
    SAMPLE         VARCHAR2(38) not null,
    LAT            NUMBER(30, 11),
    LON            NUMBER(30, 11),
    COLOUR         VARCHAR2(30),
    NATQUA         VARCHAR2(30),
    NATSUR         VARCHAR2(30),
    SORDAT         VARCHAR2(30),
    SORIND         VARCHAR2(30),
    DESCRP         VARCHAR2(255),
    PRMSEC         VARCHAR2(30),
    REMRKS         VARCHAR2(255),
    BEGIN_OBSTIM   DATE,
    END_OBSTIM     DATE,
    ORIG_DATUM     VARCHAR2(30),
    DEVICE         VARCHAR2(30),
    SOURCE         VARCHAR2(15),
    OBJECTID       NUMBER(38),
    SHAPE_NAD83    MDSYS.SDO_GEOMETRY,
    SHAPE_WGS84    MDSYS.SDO_GEOMETRY,
    DATE_CREATED   DATE,
    DATE_UPDATED   DATE,
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1)
)
/

create table ${schema_name}.UNPUBLISH_SURVEYS
(
    SURVEY         VARCHAR2(30) not null,
    SAMPLE         VARCHAR2(38) not null,
    LAT            NUMBER(30, 11),
    LON            NUMBER(30, 11),
    COLOUR         VARCHAR2(30),
    NATQUA         VARCHAR2(30),
    NATSUR         VARCHAR2(30),
    SORDAT         VARCHAR2(30),
    SORIND         VARCHAR2(30),
    DESCRP         VARCHAR2(255),
    PRMSEC         VARCHAR2(30),
    REMRKS         VARCHAR2(255),
    BEGIN_OBSTIM   DATE,
    END_OBSTIM     DATE,
    ORIG_DATUM     VARCHAR2(30),
    DEVICE         VARCHAR2(30),
    SOURCE         VARCHAR2(15),
    OBJECTID       NUMBER(38),
    SHAPE_NAD83    MDSYS.SDO_GEOMETRY,
    SHAPE_WGS84    MDSYS.SDO_GEOMETRY,
    DATE_CREATED   DATE,
    DATE_UPDATED   DATE,
    PUBLISH        VARCHAR2(1),
    PREVIOUS_STATE VARCHAR2(1)
)
/

create or replace view ${schema_name}.CURATORS_SAMPLE_VSQP as
select
    FACILITY_CODE,
    PLATFORM,
    CRUISE,
    SAMPLE,
    DEVICE,
    BEGIN_DATE,
    END_DATE,
    LAT,
    END_LAT,
    LON,
    END_LON,
    WATER_DEPTH,
    END_WATER_DEPTH,
    STORAGE_METH,
    CORED_LENGTH,
    CORED_LENGTH_MM,
    CORED_DIAM,
    CORED_DIAM_MM,
    PI,
    PROVINCE,
    LAKE,
    OTHER_LINK,
    LAST_UPDATE,
    IGSN,
    LEG,
    SAMPLE_COMMENTS,
    PUBLISH,
    OBJECTID,
    SHAPE,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/samples/imlgs' || TO_CHAR(OBJECTID, 'fm0000000') as SHOW_SAMPL,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/cruises/' || CRUISE as CRUISE_URL,
    case when LEG is null then null else 'https://maps.ngdc.noaa.gov/viewers/imlgs/cruises/' || LEG end as LEG_URL,
    'https://maps.ngdc.noaa.gov/viewers/imlgs/repositories/' || FACILITY_CODE as REPOSITORY_URL,
    IMLGS
from ${schema_name}.CURATORS_SAMPLE_TSQP
/

create or replace view ${schema_name}.MARINE_GEOLOGY_VSQP as
select
    a.OBJECTID,
    a.LATITUDE,
    a.LONGITUDE,
    a.MGGID,
    a.SHIP,
    a.CRUISE,
    a.SAMPLE,
    a.DEVICE,
    a.YEARMODA,
    a.WDEPTH,
    a.ANALYSES,
    a.DESCRIPTION,
    a.GRAIN_SIZE,
    a.GRAIN_STATS,
    a.MINERALOGY,
    a.ORGANIC_GEOCHEM,
    a.INORGANIC_GEOCHEM,
    a.PALEONTOLOGY,
    a.GEOTECHNICAL,
    a.ACOUSTICS,
    a.AGE,
    a.SAMPLE_PHOTO,
    a.NEPHELOMETRY,
    a.RADIOACTIVITY,
    a.PHOTO_DESCRIPTION,
    a.PETROLOGY,
    a.HEAT_FLOW,
    a.WELL_LOGS,
    a.MNNODULE_SET,
    a.PALEOMAGNETICS,
    a.TITLE,
    a.FGDCID,
    a.SHAPE,
    a.IGSN,
    a.PUBLISH,
    a.PREVIOUS_STATE,
    b.INSTITUTION,
    'http://www.ngdc.noaa.gov/nndc/struts/results?op_28=eq&t=101477&s=1&d=2&v_28='|| a.MGGID URL,
    CASE WHEN a.FGDCID IS NULL THEN NULL ELSE 'http://www.ngdc.noaa.gov/docucomp/page?xml=NOAA/NESDIS/NGDC/MGG/Geology/iso/xml/'|| a.FGDCID || '.xml&view=getDataView&header=none' end METADATA_URL
from ${schema_name}.MARINE_GEOLOGY_TSQP a, ${schema_name}.GEOLIN_SET b
where a.mggid = b.mggid(+)
/


create sequence ${schema_name}.CURATORS_SEQ
    nocache
/