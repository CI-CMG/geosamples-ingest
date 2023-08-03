package gov.noaa.ncei.mgg.geosamples.ingest.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.mgg.errorhandler.ApiError;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiAccessDeniedHandler.class);

  private final ObjectMapper objectMapper;

  @Autowired
  public ApiAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
    handleInternal(request, response, e);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    handleInternal(request, response, e);
  }

  private void handleInternal(HttpServletRequest request, HttpServletResponse response, RuntimeException ex) throws IOException {
    LOGGER.debug("Request was denied", ex);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    try (OutputStream out = response.getOutputStream()) {
      objectMapper.writeValue(out, ApiError.builder().error(HttpStatus.FORBIDDEN.getReasonPhrase()).build());
    }
  }
}
