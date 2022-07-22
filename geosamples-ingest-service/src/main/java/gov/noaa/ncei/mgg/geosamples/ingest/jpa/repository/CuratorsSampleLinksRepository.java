package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsSampleLinksRepository extends JpaRepository<CuratorsSampleLinksEntity, String>, JpaSpecificationExecutor<CuratorsSampleLinksEntity> {

  boolean existsByImlgs(String imlgs);
}
