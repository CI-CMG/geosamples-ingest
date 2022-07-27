package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.FacilitySearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.FacilityView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacilityService extends
    SearchServiceBase<CuratorsFacilityEntity, Long, FacilitySearchParameters, FacilityView, CuratorsFacilityRepository> {

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd");

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(FacilityView.class);

  private final CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  public FacilityService(CuratorsFacilityRepository curatorsFacilityRepository) {
    this.curatorsFacilityRepository = curatorsFacilityRepository;
  }

  @Override
  protected List<Specification<CuratorsFacilityEntity>> getSpecs(FacilitySearchParameters searchParameters) {
    List<Specification<CuratorsFacilityEntity>> specs = new ArrayList<>();

    List<String> facility = searchParameters.getFacility();
    List<String> facilityCode = searchParameters.getFacilityCode();
    List<String> instCode = searchParameters.getInstCode();
    List<String> facilityComment = searchParameters.getFacilityComment();

    if (!facility.isEmpty()) {
      specs.add(SearchUtils.contains(facility, CuratorsFacilityEntity_.FACILITY));
    }
    if (!facilityCode.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCode, CuratorsFacilityEntity_.FACILITY_CODE));
    }
    if (!instCode.isEmpty()) {
      specs.add(SearchUtils.equal(instCode, CuratorsFacilityEntity_.INST_CODE));
    }
    if (!facilityComment.isEmpty()) {
      specs.add(SearchUtils.contains(facilityComment, CuratorsFacilityEntity_.FACILITY_COMMENT));
    }

    return specs;
  }

  @Override
  protected FacilityView toView(CuratorsFacilityEntity entity) {
    FacilityView view = new FacilityView();
    view.setFacilityCode(entity.getFacilityCode());
    view.setInstCode(entity.getInstCode());
    view.setFacility(entity.getFacility());
    view.setAddr1(entity.getAddr1());
    view.setAddr2(entity.getAddr2());
    view.setAddr3(entity.getAddr3());
    view.setAddr4(entity.getAddr4());
    view.setEmailLink(entity.getEmailLink());
    view.setUrlLink(entity.getUrlLink());
    view.setFtpLink(entity.getFtpLink());
    view.setDoiLink(entity.getDoiLink());
    view.setFacilityComment(entity.getFacilityComment());
    return view;
  }

  @Override
  protected CuratorsFacilityEntity newEntityWithDefaultValues(FacilityView view) {
    CuratorsFacilityEntity entity = new CuratorsFacilityEntity();
    entity.setFacilityCode(view.getFacilityCode());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsFacilityEntity entity, FacilityView view) {
    entity.setInstCode(view.getInstCode());
    entity.setFacility(view.getFacility());
    entity.setAddr1(view.getAddr1());
    entity.setAddr2(view.getAddr2());
    entity.setAddr3(view.getAddr3());
    entity.setAddr4(view.getAddr4());
    entity.setEmailLink(view.getEmailLink());
    entity.setUrlLink(view.getUrlLink());
    entity.setFtpLink(view.getFtpLink());
    entity.setDoiLink(view.getDoiLink());
    entity.setFacilityComment(view.getFacilityComment());
    entity.setLastUpdate(Instant.now());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsFacilityRepository getRepository() {
    return curatorsFacilityRepository;
  }

}
