package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsWeathMetaRepository extends JpaRepository<CuratorsWeathMetaEntity, String>, JpaSpecificationExecutor<CuratorsWeathMetaEntity> {


  Optional<CuratorsWeathMetaEntity> findByWeathMetaCode(String code);
  boolean existsByWeathMetaCode(String code);
}
