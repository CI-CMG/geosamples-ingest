package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsTextureRepository extends JpaRepository<CuratorsTextureEntity, String> {


  Optional<CuratorsTextureEntity> findByTextureCode(String code);
}
