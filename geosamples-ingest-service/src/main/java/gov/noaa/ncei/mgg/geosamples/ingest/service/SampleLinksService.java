package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleLinksView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleLinksRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleLinksService extends
    SearchServiceBase<CuratorsSampleLinksEntity, Long, SampleLinksSearchParameters, SampleLinksView, CuratorsSampleLinksRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("imlgs", String.format("%s.%s", CuratorsSampleLinksEntity_.SAMPLE, CuratorsSampleTsqpEntity_.IMLGS));
    map.put("dataLink", "datalink");
    map.put("publish", "publish");
    map.put("linkLevel", "linkLevel");
    map.put("linkSource", "linkSource");
    map.put("linkType", "linkType");
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

    List<Long> id = searchParameters.getId();
    List<String> publish = searchParameters.getPublish().stream().map(p -> p ? "Y" : "N").collect(Collectors.toList());
    List<String> imlgs = searchParameters.getImlgs();
    List<String> dataLink = searchParameters.getDataLink();
    List<String> linkLevel = searchParameters.getLinkLevel();
    List<String> linkSource = searchParameters.getLinkSource();
    List<String> linkType = searchParameters.getLinkType();

    if (!id.isEmpty()){
      specs.add(SearchUtils.equal(id, CuratorsSampleLinksEntity_.ID));
    }
    if (!publish.isEmpty()) {
      specs.add(SearchUtils.equal(publish, CuratorsSampleLinksEntity_.PUBLISH));
    }
    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, (e) -> e.join(CuratorsSampleLinksEntity_.SAMPLE)
          .get(CuratorsSampleTsqpEntity_.IMLGS)));
    }
    if (!dataLink.isEmpty()){
      specs.add(SearchUtils.equal(dataLink, CuratorsSampleLinksEntity_.DATALINK));
    }
    if (!linkLevel.isEmpty()){
      specs.add(SearchUtils.equal(linkLevel, CuratorsSampleLinksEntity_.LINK_LEVEL));
    }
    if (!linkSource.isEmpty()){
      specs.add(SearchUtils.equal(linkSource, CuratorsSampleLinksEntity_.LINK_SOURCE));
    }
    if (!linkType.isEmpty()){
      specs.add(SearchUtils.equal(linkType, CuratorsSampleLinksEntity_.LINK_TYPE));
    }
    return specs;
  }

  @Override
  protected SampleLinksView toView(CuratorsSampleLinksEntity entity) {
    SampleLinksView view = new SampleLinksView();
    view.setId(entity.getId());
    view.setImlgs(entity.getSample()== null ? null : entity.getSample().getImlgs());
    view.setDataLink(entity.getDatalink());
    view.setLinkLevel(entity.getLinkLevel());
    view.setLinkSource(entity.getLinkSource());
    view.setLinkType(entity.getLinkType());
    view.setPublish(entity.isPublish());
    return view;
  }

  @Override
  protected CuratorsSampleLinksEntity newEntityWithDefaultValues(SampleLinksView view) {
    return new CuratorsSampleLinksEntity();
  }

  @Override
  protected void updateEntity(CuratorsSampleLinksEntity entity, SampleLinksView view) {
    entity.setId(view.getId());
    entity.setSample(sampleDataUtils.getSample(view.getImlgs()));
    entity.setDatalink(view.getDataLink());
    entity.setLinkLevel(view.getLinkLevel());
    entity.setLinkSource(view.getLinkSource());
    entity.setLinkType(view.getLinkType());
    entity.setPublish(view.getPublish());
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
