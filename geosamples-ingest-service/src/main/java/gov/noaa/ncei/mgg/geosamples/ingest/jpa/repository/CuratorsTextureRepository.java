package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsTextureRepository extends JpaRepository<CuratorsTextureEntity, String>, JpaSpecificationExecutor<CuratorsTextureEntity> {


  Optional<CuratorsTextureEntity> findByTextureCode(String code);
  boolean existsByTextureCode(String code);
}
