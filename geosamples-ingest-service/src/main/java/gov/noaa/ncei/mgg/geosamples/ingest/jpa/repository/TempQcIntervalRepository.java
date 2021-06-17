package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempQcIntervalRepository extends JpaRepository<TempQcIntervalEntity, IntervalPk> {

}
