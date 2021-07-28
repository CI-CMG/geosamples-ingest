package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsMunsellRepository extends JpaRepository<CuratorsMunsellEntity, String>, JpaSpecificationExecutor<CuratorsMunsellEntity> {

}
