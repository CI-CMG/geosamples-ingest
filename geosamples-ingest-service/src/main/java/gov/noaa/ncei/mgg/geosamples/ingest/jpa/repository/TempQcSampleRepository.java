package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcSampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempQcSampleRepository extends JpaRepository<TempQcSampleEntity, String> {

}
