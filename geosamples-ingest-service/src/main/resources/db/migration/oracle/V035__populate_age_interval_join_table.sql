insert into ${schema_name}.AGE_INTERVAL (AGE, INTERVAL_ID)
select ${schema_name}.CURATORS_AGE.AGE, ${schema_name}.CURATORS_INTERVAL.ID from ${schema_name}.CURATORS_INTERVAL
join ${schema_name}.CURATORS_AGE on ${schema_name}.CURATORS_INTERVAL.AGE = ${schema_name}.CURATORS_AGE.AGE;