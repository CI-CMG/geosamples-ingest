package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuratorsCruisePlatformRepository extends JpaRepository<CuratorsCruisePlatformEntity, Long>,
    JpaSpecificationExecutor<CuratorsCruisePlatformEntity> {

  Optional<CuratorsCruisePlatformEntity> findByCruiseAndPlatform(CuratorsCruiseEntity cruise, PlatformMasterEntity platform);
  boolean existsByCruiseAndPlatform(CuratorsCruiseEntity cruise, PlatformMasterEntity platform);
}
