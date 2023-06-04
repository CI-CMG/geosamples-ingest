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
      openApi.getComponents().getSchemas().putAll(ModelConverters.getInstance().read(ApiError.class));
      Schema errorResponseSchema = new Schema();
      errorResponseSchema.setName("ApiError");
      errorResponseSchema.set$ref("#/components/schemas/ApiError");
      openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
        ApiResponses apiResponses = operation.getResponses();
        apiResponses.addApiResponse("3xx", createApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("4xx", createApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("5xx", createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponseSchema));
      }));
    };
  }

  private ApiResponse createApiResponse(String message, Schema schema) {
    MediaType mediaType = new MediaType();
    mediaType.schema(schema);
    return new ApiResponse().description(message)
        .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType));
  }

}
