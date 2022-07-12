package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GeosamplesAuthorityRepository extends JpaRepository<GeosamplesAuthorityEntity, String>,
    JpaSpecificationExecutor<GeosamplesAuthorityEntity> {

}
