package gov.noaa.ncei.mgg.geosamples.ingest.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

public class GeometryAsWKTSerializerRounded extends JsonSerializer<Geometry> {
  @Override
  public void serialize(Geometry value, JsonGenerator gen,
      SerializerProvider serializers) throws IOException,
      JsonProcessingException {
    int precision = 100000; // 5 digits precision
    PrecisionModel pm = new PrecisionModel(precision);
    Geometry geometryReduced = GeometryPrecisionReducer.reducePointwise(value, pm);
    gen.writeString(geometryReduced.toText());
  }
}
