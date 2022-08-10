package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TokenGenerateModel;
import gov.noaa.ncei.mgg.geosamples.ingest.service.TokenService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-token")
@Validated
public class TokenController {

  private final TokenService tokenService;

  @Autowired
  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping(path = "generate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public TokenGenerateModel generate(
      @Valid @RequestBody TokenGenerateModel tokenGenerateModel,
      Authentication authentication) {

    String token = tokenService.createToken(authentication.getName(), tokenGenerateModel.getAlias().trim());

    tokenGenerateModel.setToken(token);
    return tokenGenerateModel;
  }

  @PostMapping(path = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public TokenGenerateModel delete(
      @Valid @RequestBody TokenGenerateModel tokenGenerateModel,
      Authentication authentication) {

    tokenService.deleteToken(authentication.getName(), tokenGenerateModel.getAlias().trim());

    tokenGenerateModel.setToken(null);
    return tokenGenerateModel;

  }

}
