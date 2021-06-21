package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcSampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TempQcSampleRepository extends JpaRepository<TempQcSampleEntity, String>, JpaSpecificationExecutor<TempQcSampleEntity> {

}
