package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsRockLithRepository extends JpaRepository<CuratorsRockLithEntity, String>, JpaSpecificationExecutor<CuratorsRockLithEntity> {


  Optional<CuratorsRockLithEntity> findByRockLithCode(String code);
}
