package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleLinksRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.criteria.Predicate;
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
  private final SampleDataUtils sampleDataUtils;

  @Autowired
  public SampleLinksService(CuratorsSampleLinksRepository curatorsSampleLinksRepository,
      SampleDataUtils sampleDataUtils) {
    this.curatorsSampleLinksRepository = curatorsSampleLinksRepository;
    this.sampleDataUtils = sampleDataUtils;
  }

  @Override
  protected List<Specification<CuratorsSampleLinksEntity>> getSpecs(SampleLinksSearchParameters searchParameters) {
    List<Specification<CuratorsSampleLinksEntity>> specs = new ArrayList<>();

    List<String> imlgs = searchParameters.getImlgs();

    if (!imlgs.isEmpty()) {
      specs.add((Specification<CuratorsSampleLinksEntity>) (e, cq, cb) ->
          cb.or(imlgs.stream().map(v ->
                  cb.equal(
                      e.get(CuratorsSampleLinksEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.IMLGS),
                      v))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }

    return specs;
  }

  @Override
  protected SampleLinksView toView(CuratorsSampleLinksEntity entity) {
    SampleLinksView view = new SampleLinksView();
    view.setImlgs(entity.getSample()== null ? null : entity.getSample().getImlgs());
    return view;
  }

  @Override
  protected CuratorsSampleLinksEntity newEntityWithDefaultValues(SampleLinksView view) {
    CuratorsSampleLinksEntity entity = new CuratorsSampleLinksEntity();
    entity.setId(view.getId());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsSampleLinksEntity entity, SampleLinksView view) {
    entity.setId(view.getId());
    CuratorsSampleTsqpEntity sample = sampleDataUtils.getSample(view.getImlgs());
    entity.setSample(sample);
    entity.setDatalink(view.getDataLink());
    entity.setLinkLevel(view.getLinkLevel());
    entity.setLinkSource(view.getLinkSource());
    entity.setLinkType(view.getLinkType());
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
