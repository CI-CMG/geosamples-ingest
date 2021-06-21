package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsProvinceRepository extends JpaRepository<CuratorsProvinceEntity, String>, JpaSpecificationExecutor<CuratorsProvinceEntity> {


  Optional<CuratorsProvinceEntity> findByProvinceCode(String code);
}
