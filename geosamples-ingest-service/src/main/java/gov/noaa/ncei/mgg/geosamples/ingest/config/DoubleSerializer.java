package gov.noaa.ncei.mgg.geosamples.ingest.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class DoubleSerializer extends StdSerializer<Double> {

  public static final SimpleModule DOUBLE_SERIALIZER_MODULE;
  static {
    SimpleModule module = new SimpleModule("GeoJsonWriter-Double", Version.unknownVersion());
    module.addSerializer(Double.class, new DoubleSerializer());
    DOUBLE_SERIALIZER_MODULE = module;
  }

  public DoubleSerializer() {
    this(null);
  }

  public DoubleSerializer(Class<Double> t) {
    super(t);
  }

  @Override
  public void serialize(Double value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
    if (value == null) {
      gen.writeNull();
    } else {
      gen.writeNumber(FormatUtils.doubleToString(value));
    }
  };

}
