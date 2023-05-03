package gov.noaa.ncei.mgg.geosamples.ingest.api.controller;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleView;
import gov.noaa.ncei.mgg.geosamples.ingest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController extends ControllerBase<RoleView, RoleSearchParameters, Long, RoleService> {

  @Autowired
  public RoleController(RoleService roleService) {
    super(roleService);
  }

}
