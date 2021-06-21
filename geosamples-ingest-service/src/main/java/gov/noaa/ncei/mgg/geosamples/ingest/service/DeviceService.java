package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DeviceSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.DeviceView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceService extends
    SearchServiceBase<CuratorsDeviceEntity, String, DeviceSearchParameters, DeviceView, CuratorsDeviceRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(DeviceView.class);

  private final CuratorsDeviceRepository curatorsDeviceRepository;

  @Autowired
  public DeviceService(CuratorsDeviceRepository curatorsDeviceRepository) {
    this.curatorsDeviceRepository = curatorsDeviceRepository;
  }

  @Override
  protected List<Specification<CuratorsDeviceEntity>> getSpecs(DeviceSearchParameters searchParameters) {
    List<Specification<CuratorsDeviceEntity>> specs = new ArrayList<>();

    List<String> device = searchParameters.getDevice();
    List<String> deviceCode = searchParameters.getDeviceCode();

    if (!device.isEmpty()) {
      specs.add(SearchUtils.contains(device, CuratorsDeviceEntity_.DEVICE));
    }
    if (!deviceCode.isEmpty()) {
      specs.add(SearchUtils.equal(deviceCode, CuratorsDeviceEntity_.DEVICE_CODE));
    }

    return specs;
  }

  @Override
  protected DeviceView toView(CuratorsDeviceEntity entity) {
    DeviceView view = new DeviceView();
    view.setDevice(entity.getDevice());
    view.setDeviceCode(entity.getDeviceCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsDeviceEntity newEntityWithDefaultValues(String id) {
    CuratorsDeviceEntity entity = new CuratorsDeviceEntity();
    entity.setDevice(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsDeviceEntity entity, DeviceView view) {
    entity.setDeviceCode(view.getDeviceCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsDeviceRepository getRepository() {
    return curatorsDeviceRepository;
  }

}
