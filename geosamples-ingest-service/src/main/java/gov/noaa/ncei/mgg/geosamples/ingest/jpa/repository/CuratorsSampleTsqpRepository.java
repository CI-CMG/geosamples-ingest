package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsSampleTsqpRepository extends JpaRepository<CuratorsSampleTsqpEntity, String>, JpaSpecificationExecutor<CuratorsSampleTsqpEntity> {

  boolean existsByIgsn(String igsn);
}
