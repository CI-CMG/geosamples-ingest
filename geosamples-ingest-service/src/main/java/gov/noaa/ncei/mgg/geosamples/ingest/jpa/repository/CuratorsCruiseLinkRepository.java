package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseLinksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsCruiseLinkRepository extends JpaRepository<CuratorsCruiseLinksEntity, Long>,
    JpaSpecificationExecutor<CuratorsCruiseLinksEntity> {


}
