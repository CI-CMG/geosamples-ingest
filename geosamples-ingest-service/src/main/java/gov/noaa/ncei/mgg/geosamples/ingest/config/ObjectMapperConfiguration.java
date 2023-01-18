package gov.noaa.ncei.mgg.geosamples.ingest.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

  @Bean
  public ObjectMapper jsonCustomizer(WktGeometryModule wktGeometryModule) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    objectMapper.registerModule(DoubleSerializer.DOUBLE_SERIALIZER_MODULE);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(wktGeometryModule);
    return objectMapper;
  }

}
