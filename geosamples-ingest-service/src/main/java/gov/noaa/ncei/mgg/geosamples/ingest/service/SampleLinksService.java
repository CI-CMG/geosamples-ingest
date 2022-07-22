package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleLinksRepository;
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
public class SampleLinksService extends
    SearchServiceBase<CuratorsSampleLinksEntity, String, SampleLinksSearchParameters, SampleLinksView, CuratorsSampleLinksRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("imlgs", "imlgs");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsSampleLinksRepository curatorsSampleLinksRepository;

  @Autowired
  public SampleLinksService(CuratorsSampleLinksRepository curatorsSampleLinksRepository) {
    this.curatorsSampleLinksRepository = curatorsSampleLinksRepository;
  }

  @Override
  protected List<Specification<CuratorsSampleLinksEntity>> getSpecs(SampleLinksSearchParameters searchParameters) {
    List<Specification<CuratorsSampleLinksEntity>> specs = new ArrayList<>();

    List<String> imlgs = searchParameters.getImlgs();

    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.contains(imlgs, CuratorsSampleLinksEntity_.IMLGS));
    }

    return specs;
  }

  @Override
  protected SampleLinksView toView(CuratorsSampleLinksEntity entity) {
    SampleLinksView view = new SampleLinksView();
    view.setImlgs(entity.getImlgs());
    return view;
  }

  @Override
  protected CuratorsSampleLinksEntity newEntityWithDefaultValues(SampleLinksView view) {
    CuratorsSampleLinksEntity entity = new CuratorsSampleLinksEntity();
    entity.setImlgs(view.getImlgs());
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsSampleLinksEntity entity, SampleLinksView view) {
    entity.setImlgs(view.getImlgs());
  }

  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsSampleLinksRepository getRepository() {
    return curatorsSampleLinksRepository;
  }

}
