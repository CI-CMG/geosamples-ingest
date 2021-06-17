package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsRockMinRepository extends JpaRepository<CuratorsRockMinEntity, String> {


  Optional<CuratorsRockMinEntity> findByRockMinCode(String code);
}
