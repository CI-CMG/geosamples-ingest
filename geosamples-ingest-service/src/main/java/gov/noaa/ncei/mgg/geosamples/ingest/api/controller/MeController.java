package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public UserView me(Authentication authentication) {
    UserView userView = new UserView();
    userView.setUsername(authentication.getName());
    return userView;
  }

}
