package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformMasterRepository extends JpaRepository<PlatformMasterEntity, String> {

}
