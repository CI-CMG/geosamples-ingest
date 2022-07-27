package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsLegRepository extends JpaRepository<CuratorsLegEntity, Long>, JpaSpecificationExecutor<CuratorsLegEntity> {

  Optional<CuratorsLegEntity> findByLegNameAndCruise(String legName, CuratorsCruiseEntity cruise);

}
