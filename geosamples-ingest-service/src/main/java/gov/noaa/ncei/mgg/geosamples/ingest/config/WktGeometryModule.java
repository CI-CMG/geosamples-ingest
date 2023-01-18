package gov.noaa.ncei.mgg.geosamples.ingest.config;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WktGeometryModule extends SimpleModule {

  @Autowired
  public WktGeometryModule(WktAsGeometryDeserializer geometryDeserializer) {
    super(VersionUtil.parseVersion("0.1", "gov.noaa.ncei.mgg.geosamples.ingest.config", "WktGeometryModule"));
    addDeserializer(Geometry.class, geometryDeserializer);
    addSerializer(Geometry.class, new GeometryAsWKTSerializerRounded());
  }

  @Override
  public String getModuleName() {
    return getClass().getSimpleName();
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }

}
