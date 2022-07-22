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

//  @Query("select c from CuratorsCruiseEntity c "
//      + "join PlatformMasterEntity p "
//      + "join CuratorsFacilityEntity f "
//      + "where c.cruiseName = :cruiseName "
//      + "and p.platform = :platformName "
//      + "and f.facility = :facilityName " )
//  Optional<CuratorsCruiseEntity> findByCruisePlatformFacility(
//      @Param("cruiseName") String cruiseName,
//      @Param("platformName") String platformName,
//      @Param("facilityName") String facilityName);
//
  Optional<CuratorsCruiseEntity> findByCruiseNameAndPlatformAndFacility(String cruiseName, PlatformMasterEntity platform, CuratorsFacilityEntity facility);

}
