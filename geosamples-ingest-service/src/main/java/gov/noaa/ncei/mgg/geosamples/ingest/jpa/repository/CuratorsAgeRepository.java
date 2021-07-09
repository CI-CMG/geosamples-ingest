package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsAgeRepository extends JpaRepository<CuratorsAgeEntity, String>, JpaSpecificationExecutor<CuratorsAgeEntity> {


  Optional<CuratorsAgeEntity> findByAgeCode(String code);
  boolean existsByAgeCode(String code);
}
