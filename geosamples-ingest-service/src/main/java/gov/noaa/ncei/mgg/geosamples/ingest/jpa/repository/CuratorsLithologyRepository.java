package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsLithologyRepository extends JpaRepository<CuratorsLithologyEntity, String>, JpaSpecificationExecutor<CuratorsLithologyEntity> {


  Optional<CuratorsLithologyEntity> findByLithologyCode(String code);
}
