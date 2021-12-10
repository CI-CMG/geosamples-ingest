package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleService extends
    SearchServiceBase<CuratorsSampleTsqpEntity, String, SampleSearchParameters, SampleView, CuratorsSampleTsqpRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("imlgs", "imlgs");
    map.put("sample", "sample");
    map.put("facilityCode", "facility.facilityCode");
    map.put("platform", "platform");
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final SampleDataUtils sampleDataUtils;
  private final ServiceProperties serviceProperties;

  @Autowired
  public SampleService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      CuratorsIntervalRepository curatorsIntervalRepository, SampleDataUtils sampleDataUtils,
      ServiceProperties serviceProperties) {
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.sampleDataUtils = sampleDataUtils;
    this.serviceProperties = serviceProperties;
  }

  public SimpleItemsView<SampleView> patch(SimpleItemsView<SampleView> patch) {
    List<SampleView> items = new ArrayList<>(patch.getItems().size());

    //TODO only delete is supported
    for (SampleView del : patch.getDelete()) {
      String imlgs = del.getImlgs();
      curatorsSampleTsqpRepository.findById(imlgs)
          .ifPresent(entity -> {
            curatorsIntervalRepository.deleteAll(curatorsIntervalRepository.findByImlgs(imlgs));
            curatorsIntervalRepository.flush();
            curatorsSampleTsqpRepository.delete(entity);
            curatorsSampleTsqpRepository.flush();
            items.removeIf(view -> del.getImlgs().equals(view.getImlgs()));
          });
    }
    SimpleItemsView<SampleView> result = new SimpleItemsView<>();
    result.setItems(items);
    result.setDelete(patch.getDelete());
    return result;
  }

  @Override
  protected List<Specification<CuratorsSampleTsqpEntity>> getSpecs(SampleSearchParameters searchParameters) {
    List<Specification<CuratorsSampleTsqpEntity>> specs = new ArrayList<>();

    List<String> imlgs = searchParameters.getImlgs();
    List<String> cruise = searchParameters.getCruise();
    List<String> sample = searchParameters.getSample();
    List<String> facilityCode = searchParameters.getFacilityCode();
    List<String> platform = searchParameters.getPlatform();
    List<String> deviceCode = searchParameters.getDeviceCode();
    List<String> igsn = searchParameters.getIgsn();


    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, CuratorsSampleTsqpEntity_.IMLGS));
    }
    if (!cruise.isEmpty()) {
      specs.add(SearchUtils.contains(cruise, CuratorsSampleTsqpEntity_.CRUISE));
    }
    if (!sample.isEmpty()) {
      specs.add(SearchUtils.contains(sample, CuratorsSampleTsqpEntity_.SAMPLE));
    }
    if (!igsn.isEmpty()) {
      specs.add(SearchUtils.contains(igsn, CuratorsSampleTsqpEntity_.IGSN));
    }
    if (!facilityCode.isEmpty()) {
      specs.add((Specification<CuratorsSampleTsqpEntity>) (e, cq, cb) ->
          cb.or(facilityCode.stream().map(v ->
              cb.equal(
                  e.get(CuratorsSampleTsqpEntity_.FACILITY).get(CuratorsFacilityEntity_.FACILITY_CODE),
                  v))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }
    if (!deviceCode.isEmpty()) {
      specs.add((Specification<CuratorsSampleTsqpEntity>) (e, cq, cb) ->
          cb.or(deviceCode.stream().map(v ->
              cb.equal(
                  e.get(CuratorsSampleTsqpEntity_.DEVICE).get(CuratorsDeviceEntity_.DEVICE_CODE),
                  v))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }
    if (!platform.isEmpty()) {
      specs.add((Specification<CuratorsSampleTsqpEntity>) (e, cq, cb) ->
          cb.or(platform.stream().map(v ->
              cb.like(
                  cb.lower(e.get(CuratorsSampleTsqpEntity_.PLATFORM).get(PlatformMasterEntity_.PLATFORM)),
                  SearchUtils.contains(v.toLowerCase(Locale.ENGLISH))))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }

    return specs;
  }

  @Override
  protected SampleView toView(CuratorsSampleTsqpEntity entity) {
    SampleView view = new SampleView();
    view.setImlgs(entity.getImlgs());
    view.setCruise(entity.getCruise());
    view.setSample(entity.getSample());
    view.setFacilityCode(entity.getFacility() == null ? null : entity.getFacility().getFacilityCode());
    view.setPlatform(entity.getPlatform() == null ? null : entity.getPlatform().getPlatform());
    view.setDeviceCode(entity.getDevice() == null ? null : entity.getDevice().getDeviceCode());
    view.setShipCode(entity.getShipCode());
    view.setBeginDate(entity.getBeginDate());
    view.setEndDate(entity.getEndDate());
    view.setLat(entity.getLat());
    view.setEndLat(entity.getEndLat());
    view.setLon(entity.getLon());
    view.setEndLon(entity.getEndLon());
    view.setLatLonOrig(entity.getLatLonOrig());
    view.setWaterDepth(entity.getWaterDepth());
    view.setEndWaterDepth(entity.getEndWaterDepth());
    view.setStorageMethCode(entity.getStorageMeth() == null ? null : entity.getStorageMeth().getStorageMethCode());

    Double coredLength = null;
    if(entity.getCoredLength() != null) {
      coredLength = Double.valueOf(entity.getCoredLength());
    }
    if(coredLength != null && entity.getCoredLengthMm() != null) {
      coredLength = coredLength + entity.getCoredLengthMm() / 10D;
    }
    view.setCoredLength(coredLength);

    Double coredDiam = null;
    if(entity.getCoredDiam() != null) {
      coredDiam = Double.valueOf(entity.getCoredDiam());
    }
    if(coredDiam != null && entity.getCoredDiamMm() != null) {
      coredDiam = coredDiam + entity.getCoredDiamMm() / 10D;
    }
    view.setCoredDiam(coredDiam);

    view.setPi(entity.getPi());
    view.setProvinceCode(entity.getProvince() == null ? null : entity.getProvince().getProvinceCode());
    view.setLake(entity.getLake());
    view.setOtherLink(entity.getOtherLink());
    view.setIgsn(entity.getIgsn());
    view.setLeg(entity.getLeg());
    view.setSampleComments(entity.getSampleComments());
    view.setPublish("Y".equals(entity.getPublish()));
    return view;
  }

  @Override
  protected CuratorsSampleTsqpEntity newEntityWithDefaultValues(SampleView view) {
    CuratorsSampleTsqpEntity entity = new CuratorsSampleTsqpEntity();
    entity.setObjectId(sampleDataUtils.getObjectId());
    entity.setImlgs(sampleDataUtils.getImlgs(entity.getObjectId()));
    entity.setShowSampl(serviceProperties.getShowSampleBaseUrl() + "?" + entity.getImlgs());
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsSampleTsqpEntity sample, SampleView view) {

    CuratorsFacilityEntity facility = sampleDataUtils.getFacility(view.getFacilityCode());
    PlatformMasterEntity platform = sampleDataUtils.getPlatform(view.getPlatform());
    CuratorsDeviceEntity device = sampleDataUtils.getDevice(view.getDeviceCode());

    sample.setCruise(view.getCruise());
    sample.setSample(view.getSample());
    sample.setFacility(facility);
    sample.setPlatform(platform);
    sample.setDevice(device);

    // TODO what is this - ask Kelly?
//      sample.setShipCode();
    sample.setBeginDate(view.getBeginDate());
    sample.setEndDate(view.getEndDate());

    PositionDim beginningLat = SampleDataUtils.getPositionDim(view.getLat(), true);
    sample.setLat(beginningLat.getValue());
    sample.setLatDeg(beginningLat.getDegrees());
    sample.setLatMin(beginningLat.getMinutes());
    sample.setNs(beginningLat.getDirection());

    PositionDim endingLat = SampleDataUtils.getPositionDim(view.getEndLat(), true);
    sample.setEndLon(endingLat.getValue());
    sample.setEndLatDeg(endingLat.getDegrees());
    sample.setEndLatMin(endingLat.getMinutes());
    sample.setEndNs(endingLat.getDirection());

    PositionDim beginningLon = SampleDataUtils.getPositionDim(view.getLon(), false);
    sample.setLon(beginningLon.getValue());
    sample.setLonDeg(beginningLon.getDegrees());
    sample.setLonMin(beginningLon.getMinutes());
    sample.setEw(beginningLon.getDirection());

    PositionDim endingLon = SampleDataUtils.getPositionDim(view.getEndLon(), false);
    sample.setEndLon(endingLon.getValue());
    sample.setEndLonDeg(endingLon.getDegrees());
    sample.setEndLonMin(endingLon.getMinutes());
    sample.setEndEw(endingLon.getDirection());

    sample.setLatLonOrig("D");

    sample.setWaterDepth(view.getWaterDepth());
    sample.setEndWaterDepth(view.getEndWaterDepth());

    sample.setStorageMeth(sampleDataUtils.getStorageMethod(view.getStorageMethCode()));

    CmConverter coredLength = new CmConverter(view.getCoredLength());
    sample.setCoredLength(coredLength.getCm());
    sample.setCoredLengthMm(coredLength.getMm());

    CmConverter coredDiam = new CmConverter(view.getCoredDiam());
    sample.setCoredDiam(coredDiam.getCm());
    sample.setCoredDiamMm(coredDiam.getMm());

    sample.setPi(view.getPi());
    sample.setProvince(sampleDataUtils.getProvince(view.getProvinceCode()));
    sample.setIgsn(view.getIgsn());
    // TODO add me ? - DOI per curator - pass in form?
//      sample.setOtherLink();
    sample.setLastUpdate(LocalDate.now(ZoneId.of("UTC")).format(SampleDataUtils.DTF));
    sample.setLeg(view.getLeg());
    sample.setPublish(view.getPublish() != null && view.getPublish() ? "Y" : "N");
    sample.setShape(sampleDataUtils.getShape(view.getLon(), view.getLat()));

  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsSampleTsqpRepository getRepository() {
    return curatorsSampleTsqpRepository;
  }

}
