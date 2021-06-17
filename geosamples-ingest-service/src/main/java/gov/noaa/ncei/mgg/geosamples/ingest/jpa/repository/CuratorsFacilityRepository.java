package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsFacilityRepository extends JpaRepository<CuratorsFacilityEntity, String> {

}
