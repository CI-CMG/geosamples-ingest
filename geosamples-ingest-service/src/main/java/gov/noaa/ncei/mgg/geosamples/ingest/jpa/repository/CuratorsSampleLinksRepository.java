package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsSampleLinksRepository extends JpaRepository<CuratorsSampleLinksEntity, Long>, JpaSpecificationExecutor<CuratorsSampleLinksEntity> {

  Optional<CuratorsSampleLinksEntity> findBySample(CuratorsSampleTsqpEntity sample);
  boolean existsBySample(CuratorsSampleTsqpEntity sample);
}
