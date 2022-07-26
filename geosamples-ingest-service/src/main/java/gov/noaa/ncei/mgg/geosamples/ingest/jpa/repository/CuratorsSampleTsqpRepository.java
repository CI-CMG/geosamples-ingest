package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsSampleTsqpRepository extends JpaRepository<CuratorsSampleTsqpEntity, String>, JpaSpecificationExecutor<CuratorsSampleTsqpEntity> {

  Optional<CuratorsSampleTsqpEntity> findByImlgs(String imlgs);
  boolean existsByIgsn(String igsn);
}
