package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsRemarkRepository extends JpaRepository<CuratorsRemarkEntity, String> {


  Optional<CuratorsRemarkEntity> findByRemarkCode(String code);
}
