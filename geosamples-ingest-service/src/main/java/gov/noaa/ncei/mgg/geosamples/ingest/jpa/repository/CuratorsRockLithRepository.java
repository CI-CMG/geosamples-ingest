package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsRockLithRepository extends JpaRepository<CuratorsRockLithEntity, String> {


  Optional<CuratorsRockLithEntity> findByRockLithCode(String code);
}
