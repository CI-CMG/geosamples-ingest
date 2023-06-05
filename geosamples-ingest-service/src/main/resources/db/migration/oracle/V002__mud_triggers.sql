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

create or replace trigger ${schema_name}.CURATORS_SAMPLE_TSQP_BI BEFORE
    INSERT ON ${schema_name}.CURATORS_SAMPLE_TSQP REFERENCING OLD AS OLD NEW AS NEW FOR EACH ROW
begin

    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.OBJECTID is null then
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