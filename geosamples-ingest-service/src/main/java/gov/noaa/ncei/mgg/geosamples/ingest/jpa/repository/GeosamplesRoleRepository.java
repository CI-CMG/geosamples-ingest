package gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeosamplesRoleRepository extends JpaRepository<GeosamplesRoleEntity, String> {

  Optional<GeosamplesRoleEntity> getByRoleName(String roleName);

}
