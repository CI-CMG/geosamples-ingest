package gov.noaa.ncei.mgg.geosamples.ingest.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeometryConfiguration {

  @Bean
  public GeometryFactory geometryFactory() {
    return new GeometryFactory(new PrecisionModel(), 8307);
  }

}
