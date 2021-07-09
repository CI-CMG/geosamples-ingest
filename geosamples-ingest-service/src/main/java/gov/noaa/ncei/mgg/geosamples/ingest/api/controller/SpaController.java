package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpaController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpaController.class);

  private final String indexHtml;

  @Autowired
  public SpaController(
      @Value("classpath:static/index.html") Resource indexHtmlFile,
      @Value("#{servletContext.contextPath}") String servletContextPath
  ) {
    try (InputStream in = indexHtmlFile.getInputStream()) {
      indexHtml = StreamUtils.copyToString(in, StandardCharsets.UTF_8)
          .replaceAll("@contextRoot@", servletContextPath);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read index.html", e);
    }
  }

  @GetMapping({"", "/view/**"})
  @ResponseBody
  public String loadApplication() {
    return indexHtml;
  }

}
