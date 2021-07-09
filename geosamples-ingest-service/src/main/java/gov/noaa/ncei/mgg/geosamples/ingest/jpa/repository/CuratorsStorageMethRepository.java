package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsStorageMethRepository extends JpaRepository<CuratorsStorageMethEntity, String>, JpaSpecificationExecutor<CuratorsStorageMethEntity> {


  Optional<CuratorsStorageMethEntity> findByStorageMethCode(String code);
  boolean existsByStorageMethCode(String code);
}
