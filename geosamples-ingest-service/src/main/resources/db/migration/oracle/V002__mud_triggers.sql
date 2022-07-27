-- create or replace trigger ${schema_name}.curators_age_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_age
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_AGE' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_AGE');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_AGE_bi
    before insert
    on ${schema_name}.CURATORS_AGE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_AGE_bu
    before update of publish
    on ${schema_name}.CURATORS_AGE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_cruise_links_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_cruise_links
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_CRUISE_LINKS' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_CRUISE_LINKS');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_CRUISE_LINKS_bi
    before insert
    on ${schema_name}.CURATORS_CRUISE_LINKS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_CRUISE_LINKS_bu
    before update of publish
    on ${schema_name}.CURATORS_CRUISE_LINKS
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_device_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_device
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_DEVICE' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_DEVICE');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_DEVICE_bi
    before insert
    on ${schema_name}.CURATORS_DEVICE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_DEVICE_bu
    before update of publish
    on ${schema_name}.CURATORS_DEVICE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_exhaust_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_exhaust
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_EXHAUST' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_EXHAUST');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_EXHAUST_bi
    before insert
    on ${schema_name}.CURATORS_EXHAUST
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_EXHAUST_bu
    before update of publish
    on ${schema_name}.CURATORS_EXHAUST
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_facility_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_facility
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_FACILITY' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_FACILITY');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_FACILITY_bi
    before insert
    on ${schema_name}.CURATORS_FACILITY
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_FACILITY_bu
    before update of publish
    on ${schema_name}.CURATORS_FACILITY
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_interval_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_interval
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_INTERVAL' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_INTERVAL');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_INTERVAL_bi
    before insert
    on ${schema_name}.CURATORS_INTERVAL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_INTERVAL_bu
    before update of publish
    on ${schema_name}.CURATORS_INTERVAL
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.CURATORS_LAKE_bi
    before insert
    on ${schema_name}.CURATORS_LAKE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_LAKE_bu
    before update of publish
    on ${schema_name}.CURATORS_LAKE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_lithology_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_lithology
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_LITHOLOGY' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_LITHOLOGY');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_LITHOLOGY_bi
    before insert
    on ${schema_name}.CURATORS_LITHOLOGY
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_LITHOLOGY_bu
    before update of publish
    on ${schema_name}.CURATORS_LITHOLOGY
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_munsell_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_munsell
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_MUNSELL' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_MUNSELL');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_MUNSELL_bi
    before insert
    on ${schema_name}.CURATORS_MUNSELL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_MUNSELL_bu
    before update of publish
    on ${schema_name}.CURATORS_MUNSELL
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_province_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_province
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_PROVINCE' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_PROVINCE');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_PROVINCE_bi
    before insert
    on ${schema_name}.CURATORS_PROVINCE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_PROVINCE_bu
    before update of publish
    on ${schema_name}.CURATORS_PROVINCE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_remark_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_remark
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_REMARK' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_REMARK');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_REMARK_bi
    before insert
    on ${schema_name}.CURATORS_REMARK
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_REMARK_bu
    before update of publish
    on ${schema_name}.CURATORS_REMARK
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_rock_lith_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_rock_lith
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_ROCK_LITH' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_ROCK_LITH');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_ROCK_LITH_bi
    before insert
    on ${schema_name}.CURATORS_ROCK_LITH
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_ROCK_LITH_bu
    before update of publish
    on ${schema_name}.CURATORS_ROCK_LITH
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_rock_min_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_rock_min
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_ROCK_MIN' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_ROCK_MIN');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_ROCK_MIN_bi
    before insert
    on ${schema_name}.CURATORS_ROCK_MIN
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_ROCK_MIN_bu
    before update of publish
    on ${schema_name}.CURATORS_ROCK_MIN
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_sample_links_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_sample_links
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_SAMPLE_LINKS' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_SAMPLE_LINKS');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_SAMPLE_LINKS_bi
    before insert
    on ${schema_name}.CURATORS_SAMPLE_LINKS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_SAMPLE_LINKS_bu
    before update of publish
    on ${schema_name}.CURATORS_SAMPLE_LINKS
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_sample_tsqp_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_sample_tsqp
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_SAMPLE_TSQP' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_SAMPLE_TSQP');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /


create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_BI BEFORE
    INSERT ON ${schema_name}.CURATORS_SAMPLE_TSQP REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
begin

    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.OBJECTID is null then
        -- Assign surrogate id.
        :new.OBJECTID := CURATORS_SEQ.NEXTVAL ;
    end if;
    if :new.IMLGS is null then
        :new.IMLGS := 'imlgs'||lpad(:new.objectid,7,'0');
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_bu
    before update of publish
    on ${schema_name}.CURATORS_SAMPLE_TSQP
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_storage_meth_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_storage_meth
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_STORAGE_METH' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_STORAGE_METH');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_STORAGE_METH_bi
    before insert
    on ${schema_name}.CURATORS_STORAGE_METH
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_STORAGE_METH_bu
    before update of publish
    on ${schema_name}.CURATORS_STORAGE_METH
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_texture_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_texture
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_TEXTURE' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_TEXTURE');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_TEXTURE_bi
    before insert
    on ${schema_name}.CURATORS_TEXTURE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_TEXTURE_bu
    before update of publish
    on ${schema_name}.CURATORS_TEXTURE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.curators_weath_meta_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.curators_weath_meta
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'CURATORS_WEATH_META' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'CURATORS_ROCK_META');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.CURATORS_WEATH_META_bi
    before insert
    on ${schema_name}.CURATORS_WEATH_META
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.CURATORS_WEATH_META_bu
    before update of publish
    on ${schema_name}.CURATORS_WEATH_META
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.DECK41_TSQP_bi
    before insert
    on ${schema_name}.DECK41_TSQP
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.DECK41_TSQP_bu
    before update of publish
    on ${schema_name}.DECK41_TSQP
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.GEOLIN_SAMPLE_bi
    before insert
    on ${schema_name}.GEOLIN_SAMPLE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.GEOLIN_SAMPLE_bu
    before update of publish
    on ${schema_name}.GEOLIN_SAMPLE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.GEOLIN_SET_bi
    before insert
    on ${schema_name}.GEOLIN_SET
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.GEOLIN_SET_bu
    before update of publish
    on ${schema_name}.GEOLIN_SET
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.INST_bi
    before insert
    on ${schema_name}.INST
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.INST_bu
    before update of publish
    on ${schema_name}.INST
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.MARINE_GEOLOGY_TSQP_bi
    before insert
    on ${schema_name}.MARINE_GEOLOGY_TSQP
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.MARINE_GEOLOGY_TSQP_bu
    before update of publish
    on ${schema_name}.MARINE_GEOLOGY_TSQP
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.MMBIB_bi
    before insert
    on ${schema_name}.MMBIB
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.MMBIB_bu
    before update of publish
    on ${schema_name}.MMBIB
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.NEWDECK_bi
    before insert
    on ${schema_name}.NEWDECK
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.NEWDECK_bu
    before update of publish
    on ${schema_name}.NEWDECK
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.NEWDECK_REFS_bi
    before insert
    on ${schema_name}.NEWDECK_REFS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.NEWDECK_REFS_bu
    before update of publish
    on ${schema_name}.NEWDECK_REFS
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.PLATFORM_bi
    before insert
    on ${schema_name}.PLATFORM
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.PLATFORM_bu
    before update of publish
    on ${schema_name}.PLATFORM
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

-- create or replace trigger ${schema_name}.platform_master_auid
-- AFTER UPDATE or INSERT or DELETE
-- ON ${schema_name}.platform_master
-- REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
-- BEGIN
--
-- insert into util.postgres_extract (table_name,date_updated)
-- select 'PLATFORM_MASTER' , sysdate from dual
-- where not exists (select 1 from util.postgres_extract where table_name = 'PLATFORM_MASTER');
--
-- exception
-- when others then
--     raise_application_error(-20002,'Error PostGres Extract -> '||SQLERRM);
-- END;
-- /

create or replace trigger ${schema_name}.PLATFORM_MASTER_bi
    before insert
    on ${schema_name}.PLATFORM_MASTER
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.PLATFORM_MASTER_bu
    before update of publish
    on ${schema_name}.PLATFORM_MASTER
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.SIZE_CRUISE_bi
    before insert
    on ${schema_name}.SIZE_CRUISE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.SIZE_CRUISE_bu
    before update of publish
    on ${schema_name}.SIZE_CRUISE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.SIZE_INTERVAL_bi
    before insert
    on ${schema_name}.SIZE_INTERVAL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.SIZE_INTERVAL_bu
    before update of publish
    on ${schema_name}.SIZE_INTERVAL
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.SIZE_SAMPLE_bi
    before insert
    on ${schema_name}.SIZE_SAMPLE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.SIZE_SAMPLE_bu
    before update of publish
    on ${schema_name}.SIZE_SAMPLE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.SIZE_WT_PCT_bi
    before insert
    on ${schema_name}.SIZE_WT_PCT
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.SIZE_WT_PCT_bu
    before update of publish
    on ${schema_name}.SIZE_WT_PCT
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.TEMPQC_INTERVAL_bi
    before insert
    on ${schema_name}.TEMPQC_INTERVAL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.TEMPQC_INTERVAL_bu
    before update of publish
    on ${schema_name}.TEMPQC_INTERVAL
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create or replace trigger ${schema_name}.TEMPQC_SAMPLE_bi
    before insert
    on ${schema_name}.TEMPQC_SAMPLE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger ${schema_name}.TEMPQC_SAMPLE_bu
    before update of publish
    on ${schema_name}.TEMPQC_SAMPLE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/