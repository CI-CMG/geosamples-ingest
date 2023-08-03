package gov.noaa.ncei.mgg.geosamples.ingest.swagger;

import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderCruiseController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderIntervalController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderPlatformController;
import gov.noaa.ncei.mgg.geosamples.ingest.api.controller.provider.ProviderSampleController;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
