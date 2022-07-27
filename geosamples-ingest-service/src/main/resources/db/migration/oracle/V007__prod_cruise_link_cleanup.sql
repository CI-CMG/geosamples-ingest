delete from ${schema_name}.CURATORS_CRUISE_LINKS where DATALINK is null;
update ${schema_name}.CURATORS_CRUISE_LINKS set PLATFORM = 'Velero' where CRUISE = 'V180NC';