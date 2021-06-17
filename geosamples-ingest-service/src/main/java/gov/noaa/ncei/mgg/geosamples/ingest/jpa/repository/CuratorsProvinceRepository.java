package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsProvinceRepository extends JpaRepository<CuratorsProvinceEntity, String> {


  Optional<CuratorsProvinceEntity> findByProvinceCode(String code);
}
