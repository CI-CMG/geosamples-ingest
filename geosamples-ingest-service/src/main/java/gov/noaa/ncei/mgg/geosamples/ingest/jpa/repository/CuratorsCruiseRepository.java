package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuratorsCruiseRepository extends JpaRepository<CuratorsCruiseEntity, String>, JpaSpecificationExecutor<CuratorsCruiseEntity> {

  @Query("select c from CuratorsCruiseEntity c "
      + "join CuratorsCruisePlatformEntity cp on cp.cruise = c "
      + "join CuratorsCruiseFacilityEntity cf on cf.cruise = c "
      + "where c.cruiseName = :cruiseName "
      + "and cp.platform = :platform "
      + "and cf.facility = :facility")
  Optional<CuratorsCruiseEntity> findByCruiseNameAndPlatformAndFacility(
      @Param("cruiseName") String cruiseName,
      @Param("platform") PlatformMasterEntity platform,
      @Param("facility") CuratorsFacilityEntity facility);

}
