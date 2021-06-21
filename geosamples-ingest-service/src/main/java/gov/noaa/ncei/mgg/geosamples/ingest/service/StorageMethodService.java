package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.StorageMethodSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.StorageMethodView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsStorageMethRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageMethodService extends
    SearchServiceBase<CuratorsStorageMethEntity, String, StorageMethodSearchParameters, StorageMethodView, CuratorsStorageMethRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("storageMethod", "storageMeth");
    map.put("storageMethodCode", "storageMethCode");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsStorageMethRepository curatorsStorageMethRepository;

  @Autowired
  public StorageMethodService(CuratorsStorageMethRepository curatorsStorageMethRepository) {
    this.curatorsStorageMethRepository = curatorsStorageMethRepository;
  }

  @Override
  protected List<Specification<CuratorsStorageMethEntity>> getSpecs(StorageMethodSearchParameters searchParameters) {
    List<Specification<CuratorsStorageMethEntity>> specs = new ArrayList<>();

    List<String> storageMethod = searchParameters.getStorageMethod();
    List<String> storageMethodCode = searchParameters.getStorageMethodCode();

    if (!storageMethod.isEmpty()) {
      specs.add(SearchUtils.contains(storageMethod, CuratorsStorageMethEntity_.STORAGE_METH));
    }
    if (!storageMethodCode.isEmpty()) {
      specs.add(SearchUtils.equal(storageMethodCode, CuratorsStorageMethEntity_.STORAGE_METH_CODE));
    }

    return specs;
  }

  @Override
  protected StorageMethodView toView(CuratorsStorageMethEntity entity) {
    StorageMethodView view = new StorageMethodView();
    view.setStorageMethod(entity.getStorageMeth());
    view.setStorageMethodCode(entity.getStorageMethCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsStorageMethEntity newEntityWithDefaultValues(String id) {
    CuratorsStorageMethEntity entity = new CuratorsStorageMethEntity();
    entity.setStorageMeth(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsStorageMethEntity entity, StorageMethodView view) {
    entity.setStorageMethCode(view.getStorageMethodCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsStorageMethRepository getRepository() {
    return curatorsStorageMethRepository;
  }

}
