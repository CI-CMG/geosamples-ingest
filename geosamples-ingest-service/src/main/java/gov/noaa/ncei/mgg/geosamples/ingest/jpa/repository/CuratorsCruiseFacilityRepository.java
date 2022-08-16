package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsCruiseFacilityRepository extends JpaRepository<CuratorsCruiseFacilityEntity, Long>,
    JpaSpecificationExecutor<CuratorsCruiseFacilityEntity> {

  Optional<CuratorsCruiseFacilityEntity> findByCruiseAndFacility(CuratorsCruiseEntity cruise, CuratorsFacilityEntity facility);

  boolean existsByCruiseAndFacility(CuratorsCruiseEntity cruise, CuratorsFacilityEntity facility);

}
