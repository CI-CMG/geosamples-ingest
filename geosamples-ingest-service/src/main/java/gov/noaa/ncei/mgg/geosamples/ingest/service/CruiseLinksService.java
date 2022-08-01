package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseLinksSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseLinksView;;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseLinksEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseLinksRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruisePlatformRepository;
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
public class CruiseLinksService extends
    SearchServiceBase<CuratorsCruiseLinksEntity, Long, CruiseLinksSearchParameters, CruiseLinksView, CuratorsCruiseLinksRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
//    map.put("cruiseName", "cruise.cruiseName");
//    map.put("platform", "platform.platform");
    map.put("id", "id");
    map.put("linkType", "linkType");
    map.put("linkLevel", "linkLevel");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }
  private final CuratorsCruiseLinksRepository curatorsCruiseLinksRepository;
  private final SampleDataUtils sampleDataUtils;
  @Autowired
  public CruiseLinksService(CuratorsCruiseLinksRepository curatorsCruiseLinksRepository,
      SampleDataUtils sampleDataUtils) {
    this.curatorsCruiseLinksRepository = curatorsCruiseLinksRepository;
    this.sampleDataUtils = sampleDataUtils;
  }

  @Override
  protected List<Specification<CuratorsCruiseLinksEntity>> getSpecs(CruiseLinksSearchParameters searchParameters) {
    List<Specification<CuratorsCruiseLinksEntity>> specs = new ArrayList<>();

    List<Long> id = searchParameters.getId();
    List<String> publish = searchParameters.getPublish().stream().map(p -> p ? "Y" : "N").collect(Collectors.toList());
    List<Long> cruisePlatform = searchParameters.getCruisePlatform();
    List<String> cruiseName = searchParameters.getCruiseName();
    List<Long> cruiseYear = searchParameters.getYear();
    List<String> platform = searchParameters.getPlatform();
    List<Long> leg = searchParameters.getLeg();
    List<String> legName = searchParameters.getLeg_Name();
    List<String> dataLink = searchParameters.getDataLink();
    List<String> linkLevel = searchParameters.getLinkLevel();
    List<String> linkSource = searchParameters.getLinkSource();
    List<String> linkType = searchParameters.getLinkType();

    if (!id.isEmpty()){
      specs.add(SearchUtils.equal(id, CuratorsCruiseLinksEntity_.ID));
    }
    if (!publish.isEmpty()) {
      specs.add(SearchUtils.equal(publish, CuratorsCruiseLinksEntity_.PUBLISH));
    }
    if (!cruisePlatform.isEmpty()) {
      specs.add(SearchUtils.equal(cruisePlatform, (e) -> e.join(CuratorsCruiseLinksEntity_.CRUISE_PLATFORM)
          .get(CuratorsCruisePlatformEntity_.ID)));
    }
    if(!cruiseName.isEmpty()){
      specs.add(SearchUtils.equal(cruisePlatform, (e) -> e.join(CuratorsCruiseLinksEntity_.CRUISE_PLATFORM)
          .join(CuratorsCruisePlatformEntity_.CRUISE)
          .get(CuratorsCruiseEntity_.CRUISE_NAME)));
    }
    if(!cruiseYear.isEmpty()){
      specs.add(SearchUtils.equal(cruisePlatform, (e) -> e.join(CuratorsCruiseLinksEntity_.CRUISE_PLATFORM)
          .join(CuratorsCruisePlatformEntity_.CRUISE)
          .get(CuratorsCruiseEntity_.YEAR)));
    }
    if(!platform.isEmpty()){
      specs.add(SearchUtils.equal(cruisePlatform, (e) -> e.join(CuratorsCruiseLinksEntity_.CRUISE_PLATFORM)
          .join(CuratorsCruisePlatformEntity_.PLATFORM)
          .get(PlatformMasterEntity_.PLATFORM)));
    }
    if (!leg.isEmpty()) {
      specs.add(SearchUtils.equal(leg, (e) -> e.join(CuratorsCruiseLinksEntity_.LEG)
          .get(CuratorsLegEntity_.ID)));
    }
    if (!legName.isEmpty()) {
      specs.add(SearchUtils.equal(legName, (e) -> e.join(CuratorsCruiseLinksEntity_.LEG)
          .get(CuratorsLegEntity_.LEG_NAME)));
    }
    if (!dataLink.isEmpty()){
      specs.add(SearchUtils.equal(dataLink, CuratorsCruiseLinksEntity_.DATALINK));
    }
    if (!linkLevel.isEmpty()){
      specs.add(SearchUtils.equal(linkLevel, CuratorsCruiseLinksEntity_.LINK_LEVEL));
    }
    if (!linkSource.isEmpty()){
      specs.add(SearchUtils.equal(linkSource, CuratorsCruiseLinksEntity_.LINK_SOURCE));
    }
    if (!linkType.isEmpty()){
      specs.add(SearchUtils.equal(linkType, CuratorsCruiseLinksEntity_.LINK_TYPE));
    }
    return specs;
  }

  @Override
  protected CruiseLinksView toView(CuratorsCruiseLinksEntity entity) {
    CruiseLinksView view = new CruiseLinksView();
    view.setId(entity.getId());
    view.setCruisePlatform(entity.getCruisePlatform()== null ? null : entity.getCruisePlatform().getId());
    view.setCruiseName(entity.getCruisePlatform()== null ? null : entity.getCruisePlatform().getCruise().getCruiseName());
    view.setCruiseYear(entity.getCruisePlatform()== null ? null : entity.getCruisePlatform().getCruise().getYear());
    view.setPlatform(entity.getCruisePlatform()== null ? null : entity.getCruisePlatform().getPlatform().getPlatform());
    view.setLeg(entity.getLeg()== null ? null : entity.getLeg().getId());
    view.setLegName(entity.getLeg()== null ? null : entity.getLeg().getLegName());
    view.setDataLink(entity.getDatalink());
    view.setLinkLevel(entity.getLinkLevel());
    view.setLinkSource(entity.getLinkSource());
    view.setLinkType(entity.getLinkType());
    view.setPublish(entity.isPublish());
    return view;
  }

  @Override
  protected CuratorsCruiseLinksEntity newEntityWithDefaultValues(CruiseLinksView view) {
    return new CuratorsCruiseLinksEntity();
  }

  @Override
  protected void updateEntity(CuratorsCruiseLinksEntity entity, CruiseLinksView view) {
    entity.setId(view.getId());
    entity.setCruisePlatform(sampleDataUtils.getCruisePlatform(view.getCruiseName(),view.getCruiseYear(), view.getPlatform()));
    entity.setLeg(view.getLegName()==null ? null : sampleDataUtils.getLeg(view.getLegName(), entity.getCruisePlatform().getCruise()));
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
  protected CuratorsCruiseLinksRepository getRepository() {
    return curatorsCruiseLinksRepository;
  }

}
