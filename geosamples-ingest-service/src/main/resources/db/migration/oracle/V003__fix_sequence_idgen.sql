declare
    ex number;
begin
    select COALESCE(MAX(OBJECTID), 0) + 1 into ex from ${schema_name}.CURATORS_SAMPLE_TSQP;
--     execute immediate 'drop sequence ${schema_name}.CURATORS_SEQ';
    execute immediate 'create sequence ${schema_name}.CURATORS_SEQ start with ' || ex || ' nocache';
end;
/

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