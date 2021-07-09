package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsRockMinRepository extends JpaRepository<CuratorsRockMinEntity, String>, JpaSpecificationExecutor<CuratorsRockMinEntity> {


  Optional<CuratorsRockMinEntity> findByRockMinCode(String code);
  boolean existsByRockMinCode(String code);
}
