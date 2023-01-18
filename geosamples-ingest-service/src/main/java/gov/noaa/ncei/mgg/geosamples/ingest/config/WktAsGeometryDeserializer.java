package gov.noaa.ncei.mgg.geosamples.ingest.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class  WktAsGeometryDeserializer extends StdDeserializer<Geometry> {

  private final GeometryConverter geometryConverter;

  @Autowired
  public WktAsGeometryDeserializer(GeometryConverter geometryConverter) {
    super((Class<?>) null);
    this.geometryConverter = geometryConverter;
  }

  @Override
  public Geometry deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    String wkt = jp.getText();
    try {
      return geometryConverter.convert(wkt);
    } catch (IllegalArgumentException e) {
      throw new JsonMappingException(jp, "Unable to parse WKT: " + wkt, e);
    }
  }
}
