package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsIntervalRepository extends JpaRepository<CuratorsIntervalEntity, IntervalPk>, JpaSpecificationExecutor<CuratorsIntervalEntity> {

  List<CuratorsIntervalEntity> findByImlgs(String imlgs);

}
