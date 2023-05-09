create table ${schema_name}.GEOSAMPLES_APPROVAL (
    ID NUMBER(19)
        CONSTRAINT GEOSAMPLES_APPROVAL_PK PRIMARY KEY,
    VERSION NUMBER(19) NOT NULL,
    APPROVAL_STATE VARCHAR2(255) NOT NULL,
    REVIEWER_COMMENT VARCHAR2(255)
);

alter table ${schema_name}.CURATORS_CRUISE add
    APPROVAL_ID NUMBER(19)
        CONSTRAINT CURATORS_CRUISE_APPROVAL_FK REFERENCES ${schema_name}.GEOSAMPLES_APPROVAL;

alter table ${schema_name}.CURATORS_INTERVAL add
    APPROVAL_ID NUMBER(19)
        CONSTRAINT CURATORS_INTERVAL_APPROVAL_FK REFERENCES ${schema_name}.GEOSAMPLES_APPROVAL;

alter table ${schema_name}.CURATORS_SAMPLE_TSQP add
    APPROVAL_ID NUMBER(19)
        CONSTRAINT CURATORS_SAMPLE_TSQP_APPROVAL_FK REFERENCES ${schema_name}.GEOSAMPLES_APPROVAL;

create sequence ${schema_name}.GEOSAMPLES_APPROVAL_SEQ;

create or replace trigger ${schema_name}.GEOSAMPLES_APPROVAL_BI
    before insert on ${schema_name}.GEOSAMPLES_APPROVAL
    for each row
    begin
        if :NEW.ID is null then
            :NEW.ID := ${schema_name}.GEOSAMPLES_APPROVAL_SEQ.nextval;
        end if;
    end;
