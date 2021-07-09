package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsDeviceRepository extends JpaRepository<CuratorsDeviceEntity, String>, JpaSpecificationExecutor<CuratorsDeviceEntity> {


  Optional<CuratorsDeviceEntity> findByDeviceCode(String deviceCode);
  boolean existsByDeviceCode(String deviceCode);
}
