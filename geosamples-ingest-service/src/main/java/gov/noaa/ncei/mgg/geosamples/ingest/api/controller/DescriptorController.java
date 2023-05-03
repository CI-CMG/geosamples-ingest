package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DescriptorView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ReadOnlySimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/descriptor")
public class DescriptorController {

  private final UserService userService;

  @Autowired
  public DescriptorController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "authority", produces = MediaType.APPLICATION_JSON_VALUE)
  public ReadOnlySimpleItemsView<DescriptorView> authority() {
    return userService.getAllAuthorities();
  }

  @GetMapping(path = "role", produces = MediaType.APPLICATION_JSON_VALUE)
  public ReadOnlySimpleItemsView<DescriptorView> role() {
    return userService.getAllRoles();
  }

}
