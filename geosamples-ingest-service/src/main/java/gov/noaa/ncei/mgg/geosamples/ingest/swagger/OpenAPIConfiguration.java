package gov.noaa.ncei.mgg.geosamples.ingest.swagger;

import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderCruiseController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderIntervalController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderPlatformController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderSampleController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class OpenAPIConfiguration {

  @Bean
  public GroupedOpenApi adminOpenApi(OpenApiCustomiser openApiCustomiser) {
    return GroupedOpenApi.builder()
        .group("v1-admin").packagesToExclude(
            ProviderPlatformController.class.getPackage().getName(),
            ProviderCruiseController.class.getPackage().getName(),
            ProviderSampleController.class.getPackage().getName(),
            ProviderIntervalController.class.getPackage().getName()
        )
        .addOpenApiCustomiser(openApiCustomiser)
        .build();
  }

  @Bean
  public GroupedOpenApi providerOpenApi(OpenApiCustomiser openApiCustomiser) {
    return GroupedOpenApi.builder()
        .group("provider").packagesToScan(
            ProviderPlatformController.class.getPackage().getName(),
            ProviderCruiseController.class.getPackage().getName(),
            ProviderSampleController.class.getPackage().getName(),
            ProviderIntervalController.class.getPackage().getName()
        )
        .addOpenApiCustomiser(openApiCustomiser)
        .build();
  }

  @Bean
  public OpenApiCustomiser openApiCustomiser() {
    return openApi -> {
    };
  }

}
