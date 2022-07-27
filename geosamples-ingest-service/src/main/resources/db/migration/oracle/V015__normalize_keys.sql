alter table ${schema_name}.PLATFORM_MASTER add (PLATFORM_NORMALIZED varchar2(50));

update ${schema_name}.PLATFORM_MASTER set PLATFORM_NORMALIZED = upper(PLATFORM);

create or replace trigger ${schema_name}.PLATFORM_MASTER_BI
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
    :new.PLATFORM_NORMALIZED := upper(:new.PLATFORM);
end;
/

create or replace trigger ${schema_name}.PLATFORM_MASTER_BU
    before update
    on ${schema_name}.PLATFORM_MASTER
    for each row
begin
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    :new.PLATFORM_NORMALIZED := upper(:new.PLATFORM);
end;
/

alter table ${schema_name}.PLATFORM_MASTER
    modify (
        PLATFORM_NORMALIZED varchar2(50)
            constraint PLATFORM_MASTER_PLATFORM_NORMALIZED_NN not null
            constraint PLATFORM_MASTER_PLATFORM_NORMALIZED_UK unique
        );

update ${schema_name}.CURATORS_FACILITY set FACILITY_CODE = upper(FACILITY_CODE);

update ${schema_name}.CURATORS_CRUISE CS set CRUISE_NAME = (
    select F.FACILITY_CODE || '-UNKNOWN'
    from ${schema_name}.CURATORS_CRUISE C
             inner join ${schema_name}.CURATORS_CRUISE_FACILITY CF on C.ID = CF.CRUISE_ID
             inner join ${schema_name}.CURATORS_FACILITY F on CF.FACILITY_ID = F.ID
    where C.ID = CS.ID
)
where ID in (
    select C.ID
    from ${schema_name}.CURATORS_SAMPLE_TSQP S
             inner join ${schema_name}.CURATORS_CRUISE_FACILITY CF on S.CRUISE_FACILITY_ID = CF.ID
             inner join ${schema_name}.CURATORS_FACILITY F on CF.FACILITY_ID = F.ID
             inner join ${schema_name}.CURATORS_CRUISE C on S.CRUISE_ID = C.ID
    where upper(C.CRUISE_NAME) = 'UNKNOWN'
);

update ${schema_name}.CURATORS_CRUISE set CRUISE_NAME = upper(CRUISE_NAME);