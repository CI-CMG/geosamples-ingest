package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsFacilityRepository extends JpaRepository<CuratorsFacilityEntity, Long>, JpaSpecificationExecutor<CuratorsFacilityEntity> {

  boolean existsByFacilityCode(String facilityCode);

  Optional<CuratorsFacilityEntity> findByFacilityCode(String facilityCode);
}
