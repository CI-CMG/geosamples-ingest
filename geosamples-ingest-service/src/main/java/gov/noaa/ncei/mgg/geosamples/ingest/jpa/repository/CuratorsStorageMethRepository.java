package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsStorageMethRepository extends JpaRepository<CuratorsStorageMethEntity, String> {


  Optional<CuratorsStorageMethEntity> findByStorageMethCode(String code);
}
