package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsIntervalRepository extends JpaRepository<CuratorsIntervalEntity, Long>, JpaSpecificationExecutor<CuratorsIntervalEntity> {

  List<CuratorsIntervalEntity> findBySample(CuratorsSampleTsqpEntity sample);
  Optional<CuratorsIntervalEntity> findBySampleAndInterval(CuratorsSampleTsqpEntity sample, int interval);

  boolean existsByIgsn(String igsn);
}
