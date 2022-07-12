package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GeosamplesUserRepository extends JpaRepository<GeosamplesUserEntity, String>, JpaSpecificationExecutor<GeosamplesUserEntity> {


}
