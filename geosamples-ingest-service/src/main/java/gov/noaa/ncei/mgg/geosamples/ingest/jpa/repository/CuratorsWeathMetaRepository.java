package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsWeathMetaRepository extends JpaRepository<CuratorsWeathMetaEntity, String> {


  Optional<CuratorsWeathMetaEntity> findByWeathMetaCode(String code);
}
