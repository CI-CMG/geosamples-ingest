package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuratorsAgeRepository extends JpaRepository<CuratorsAgeEntity, String> {


  Optional<CuratorsAgeEntity> findByAgeCode(String code);
}
