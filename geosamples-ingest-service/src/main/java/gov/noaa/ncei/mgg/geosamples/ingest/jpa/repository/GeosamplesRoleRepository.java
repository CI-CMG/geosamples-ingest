package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GeosamplesRoleRepository extends JpaRepository<GeosamplesRoleEntity, Long>, JpaSpecificationExecutor<GeosamplesRoleEntity> {

  Optional<GeosamplesRoleEntity> getByRoleName(String roleName);

}
