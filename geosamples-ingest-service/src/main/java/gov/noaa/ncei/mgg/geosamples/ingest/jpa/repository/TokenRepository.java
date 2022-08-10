package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<GeosamplesTokenEntity, Long>, JpaSpecificationExecutor<GeosamplesTokenEntity> {


  Optional<GeosamplesTokenEntity> findByTokenHash(String tokenHash);

  Optional<GeosamplesTokenEntity> findByUserAndAlias(GeosamplesUserEntity user, String alias);
}
