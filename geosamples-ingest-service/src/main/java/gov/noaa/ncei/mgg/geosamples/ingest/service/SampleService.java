package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.ApprovalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SimpleItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruisePlatformRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.SampleIntervalUtils.Bbox;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class SampleService extends
    ApprovalResourceServiceBase<String, CuratorsSampleTsqpEntity, SampleSearchParameters, SampleView, CuratorsSampleTsqpRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {

    Map<String, String> map = new HashMap<>();
    map.put("imlgs", "imlgs");
    map.put("sample", "sample");
    map.put("facilityCode", String.format("%s.%s.%s", CuratorsSampleTsqpEntity_.CRUISE_FACILITY, CuratorsCruiseFacilityEntity_.FACILITY, CuratorsFacilityEntity_.FACILITY_CODE));
    map.put("cruise", "cruise.cruiseName");
    map.put("platform", "cruisePlatform.platform.platform");
    map.put("deviceCode", "device.deviceCode");
    map.put("approvalState", String.format("%s.%s", CuratorsSampleTsqpEntity_.APPROVAL, GeosamplesApprovalEntity_.APPROVAL_STATE));
    viewToEntitySortMapping = Collections.unmodifiableMap(map);
  }

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final SampleDataUtils sampleDataUtils;
  private final ServiceProperties serviceProperties;
  private final CuratorsCruisePlatformRepository curatorsCruisePlatformRepository;
  private final CuratorsCruiseFacilityRepository curatorsCruiseFacilityRepository;

  private final IntervalService intervalService;

  @Autowired
  public SampleService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      CuratorsIntervalRepository curatorsIntervalRepository, SampleDataUtils sampleDataUtils,
      ServiceProperties serviceProperties,
      CuratorsCruisePlatformRepository curatorsCruisePlatformRepository,
      CuratorsCruiseFacilityRepository curatorsCruiseFacilityRepository, IntervalService intervalService) {
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.sampleDataUtils = sampleDataUtils;
    this.serviceProperties = serviceProperties;
    this.curatorsCruisePlatformRepository = curatorsCruisePlatformRepository;
    this.curatorsCruiseFacilityRepository = curatorsCruiseFacilityRepository;
    this.intervalService = intervalService;
  }

  public SimpleItemsView<SampleView> patch(SimpleItemsView<SampleView> patch) {
    List<SampleView> items = new ArrayList<>(patch.getItems().size());

    //TODO only delete is supported
    for (SampleView del : patch.getDelete()) {
      String imlgs = del.getImlgs();
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findById(imlgs)
          .orElseThrow(() -> new ApiException(
              HttpStatus.NOT_FOUND,
              ApiError.builder().error("Sample " + imlgs + " was not found.")
                  .build()));

      curatorsSampleTsqpRepository.findById(imlgs)
          .ifPresent(entity -> {
            curatorsIntervalRepository.deleteAll(curatorsIntervalRepository.findBySample(sample));
            curatorsIntervalRepository.flush();
            curatorsSampleTsqpRepository.delete(entity);
            curatorsSampleTsqpRepository.flush();
            items.removeIf(view -> del.getImlgs().equals(view.getImlgs()));
          });
    }

    for (SampleView item : patch.getItems()) {
      String imlgs = item.getImlgs();
      CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findById(imlgs).orElseThrow(() -> new ApiException(
          HttpStatus.NOT_FOUND,
          ApiError.builder().error("Sample " + item.getImlgs() + "-" + item.getSample() + " was not found.")
              .build()));
      if (item.getPublish() != null) {
        if (item.getPublish()) {
          if (sample.getApproval() != null) {
            if (!sample.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
              throw new ApiException(
                  HttpStatus.BAD_REQUEST,
                  ApiError.builder().error(String.format("Sample %s is not approved", imlgs)).build()
              );
            }
          }
        }
        sample.setPublish(item.getPublish());
        sample = curatorsSampleTsqpRepository.saveAndFlush(sample);
      }
      items.add(toView(sample));
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
    List<Integer> cruiseYear = searchParameters.getCruiseYear();
    List<String> sample = searchParameters.getSample();
    List<String> facilityCode = searchParameters.getFacilityCode();
    List<String> platform = searchParameters.getPlatform();
    List<String> deviceCode = searchParameters.getDeviceCode();
    List<String> igsn = searchParameters.getIgsn();
    List<ApprovalState> approvalState = searchParameters.getApprovalState();
    List<String> publish = searchParameters.getPublish().stream().map(p -> p ? "Y" : "N").collect(Collectors.toList());

    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, CuratorsSampleTsqpEntity_.IMLGS));
    }
    if (!cruise.isEmpty()) {
      specs.add(SearchUtils.contains(cruise, e -> e.join(CuratorsSampleTsqpEntity_.CRUISE).get(CuratorsCruiseEntity_.CRUISE_NAME)));
    }
    if (!cruiseYear.isEmpty()) {
      specs.add(SearchUtils.equal(cruiseYear, e -> e.join(CuratorsSampleTsqpEntity_.CRUISE).get(CuratorsCruiseEntity_.YEAR)));
    }
    if (!sample.isEmpty()) {
      specs.add(SearchUtils.contains(sample, CuratorsSampleTsqpEntity_.SAMPLE));
    }
    if (!igsn.isEmpty()) {
      specs.add(SearchUtils.contains(igsn, CuratorsSampleTsqpEntity_.IGSN));
    }
    if (!facilityCode.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCode, e ->
          e.join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY)
              .join(CuratorsCruiseFacilityEntity_.FACILITY)
              .get(CuratorsFacilityEntity_.FACILITY_CODE)));
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
                      cb.lower(e.join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM)
                          .get(PlatformMasterEntity_.PLATFORM)),
                      SearchUtils.contains(v.toLowerCase(Locale.ENGLISH))))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }
    if (!approvalState.isEmpty()) {
      specs.add(SearchUtils.equal(
          approvalState.stream().map(ApprovalState::name).collect(Collectors.toList()),
          e -> e.join(CuratorsSampleTsqpEntity_.APPROVAL).get(GeosamplesApprovalEntity_.APPROVAL_STATE)
      ));
    }
    String bbox = searchParameters.getBbox();
    if (StringUtils.hasText(bbox)) {
      Bbox box = Bbox.parse(bbox);
      specs.add((e, cq, cb) -> cb.greaterThanOrEqualTo(e.get(CuratorsSampleTsqpEntity_.LON), box.getXMin()));
      specs.add((e, cq, cb) -> cb.greaterThanOrEqualTo(e.get(CuratorsSampleTsqpEntity_.LAT), box.getYMin()));
      specs.add((e, cq, cb) -> cb.lessThanOrEqualTo(e.get(CuratorsSampleTsqpEntity_.LON), box.getXMax()));
      specs.add((e, cq, cb) -> cb.lessThanOrEqualTo(e.get(CuratorsSampleTsqpEntity_.LAT), box.getYMax()));
    }
    if (!publish.isEmpty()) {
      specs.add(
          SearchUtils.equal(
              publish,
              CuratorsSampleTsqpEntity_.PUBLISH
          )
      );
    }

    return specs;
  }

  public static Specification<CuratorsSampleTsqpEntity> within(Geometry shape) {
    return (feature, cq, cb) -> cb.and(
        cb.isNotNull(feature.get(CuratorsSampleTsqpEntity_.SHAPE)),
        new WithinPredicate(cb, feature.get(CuratorsSampleTsqpEntity_.SHAPE), shape)
    );
  }

  private static Geometry normalizeArea(Geometry geometry) {
    if (geometry instanceof GeometryCollection && geometry.getGeometryType().equals("GeometryCollection")) {
      List<Polygon> polygons = new ArrayList<>();
      for (int n = 0; n < geometry.getNumGeometries(); n++) {
        Geometry g = geometry.getGeometryN(n);
        if (g instanceof Polygon) {
          polygons.add((Polygon) g);
        } else if (g instanceof MultiPolygon) {
          for (int n2 = 0; n2 < g.getNumGeometries(); n2++) {
            polygons.add((Polygon) g.getGeometryN(n));
          }
        }
      }
      return geometry.getFactory().createMultiPolygon(polygons.toArray(new Polygon[0]));
    }
    return geometry;
  }

  @Override
  protected SampleView toView(CuratorsSampleTsqpEntity entity) {
    SampleView view = new SampleView();
    view.setImlgs(entity.getImlgs());
    view.setCruise(entity.getCruise() == null ? null : entity.getCruise().getCruiseName());
    view.setCruiseYear(entity.getCruise() == null ? null : Integer.valueOf(entity.getCruise().getYear()));
    view.setSample(entity.getSample());
    view.setFacilityCode(entity.getCruiseFacility().getFacility().getFacilityCode());
    view.setPlatform(entity.getCruisePlatform().getPlatform().getPlatform());
    view.setDeviceCode(entity.getDevice() == null ? null : entity.getDevice().getDeviceCode());
    view.setBeginDate(entity.getBeginDate());
    view.setEndDate(entity.getEndDate());
    view.setLat(entity.getLat());
    view.setEndLat(entity.getEndLat());
    view.setLon(entity.getLon());
    view.setEndLon(entity.getEndLon());
    view.setWaterDepth(entity.getWaterDepth());
    view.setEndWaterDepth(entity.getEndWaterDepth());
    view.setStorageMethCode(entity.getStorageMeth() == null ? null : entity.getStorageMeth().getStorageMethCode());

    Double coredLength = null;
    if (entity.getCoredLength() != null) {
      coredLength = Double.valueOf(entity.getCoredLength());
    }
    Double coredLengthMm = null;
    if (entity.getCoredLengthMm() != null) {
      coredLengthMm = Double.valueOf(entity.getCoredLengthMm());
    }
    if (coredLength != null && coredLengthMm != null) {
      coredLength = coredLength + entity.getCoredLengthMm() / 10D;
    }
    view.setCoredLength(coredLength);
    view.setCoredLengthMm(coredLengthMm);

    Double coredDiam = null;
    if (entity.getCoredDiam() != null) {
      coredDiam = Double.valueOf(entity.getCoredDiam());
    }
    
    Double coredDiamMm = null;
    if (entity.getCoredDiamMm() != null) {
      coredDiamMm = Double.valueOf(entity.getCoredDiamMm());
    }
    
    if (coredDiam != null && coredDiamMm != null) {
      coredDiam = coredDiam + entity.getCoredDiamMm() / 10D;
    }
    view.setCoredDiam(coredDiam);
    view.setCoredDiamMm(coredDiamMm);

    view.setPi(entity.getPi());
    view.setProvinceCode(entity.getProvince() == null ? null : entity.getProvince().getProvinceCode());
    view.setLake(entity.getLake());
    view.setIgsn(entity.getIgsn());
    view.setLeg(entity.getLeg() == null ? null : entity.getLeg().getLegName());
    view.setSampleComments(entity.getSampleComments());
    view.setPublish(entity.isPublish());
    view.setShowSampl(entity.getShowSampl());
    view.setApprovalState(entity.getApproval() != null ? entity.getApproval().getApprovalState() : null);
    return view;
  }

  @Override
  protected CuratorsSampleTsqpEntity newEntityWithDefaultValues(SampleView view) {
    CuratorsSampleTsqpEntity entity = new CuratorsSampleTsqpEntity();
//    entity.setObjectId(sampleDataUtils.getObjectId());
    entity.setImlgs(sampleDataUtils.getImlgs(sampleDataUtils.getObjectId()));
    entity.setShowSampl(serviceProperties.getShowSampleBaseUrl() + entity.getImlgs());
    entity.setPublish(true);
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsSampleTsqpEntity sample, SampleView view) {

    CuratorsFacilityEntity facility = sampleDataUtils.getFacility(view.getFacilityCode());
    PlatformMasterEntity platform = sampleDataUtils.getPlatform(view.getPlatform());
    CuratorsDeviceEntity device = sampleDataUtils.getDevice(view.getDeviceCode());
    CuratorsCruiseEntity cruise = sampleDataUtils.getCruise(view.getCruise(), platform, facility);
    CuratorsLegEntity leg = view.getLeg() == null ? null : sampleDataUtils.getLeg(view.getLeg(), cruise);
    sample.setCruise(cruise);

    sample.setCruiseFacility(curatorsCruiseFacilityRepository.findByCruiseAndFacility(cruise, facility).orElseThrow(() ->
        new ApiException(HttpStatus.BAD_REQUEST,
            ApiError.builder().error("cruise " + cruise.getCruiseName() + " is not associated with facility " + facility.getFacility()).build())
    ));

    sample.setCruisePlatform(curatorsCruisePlatformRepository.findByCruiseAndPlatform(cruise, platform).orElseThrow(() ->
        new ApiException(HttpStatus.BAD_REQUEST,
            ApiError.builder().error("cruise " + cruise.getCruiseName() + " is not associated with platform " + platform.getPlatform()).build())
    ));

    sample.setSample(view.getSample());
//    sample.setFacility(facility);
//    sample.setPlatform(platform);
    sample.setLeg(leg);
    sample.setDevice(device);

    // TODO what is this - ask Kelly?
//      sample.setShipCode();
    sample.setBeginDate(view.getBeginDate());
    sample.setEndDate(view.getEndDate());

    PositionDim beginningLat = SampleDataUtils.getPositionDim(view.getLat(), true);
    sample.setLat(beginningLat.getValue());

    PositionDim endingLat = SampleDataUtils.getPositionDim(view.getEndLat(), true);
    sample.setEndLat(endingLat.getValue());

    PositionDim beginningLon = SampleDataUtils.getPositionDim(view.getLon(), false);
    sample.setLon(beginningLon.getValue());

    PositionDim endingLon = SampleDataUtils.getPositionDim(view.getEndLon(), false);
    sample.setEndLon(endingLon.getValue());

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
    sample.setLake(view.getLake());
    sample.setSampleComments(view.getSampleComments());
    sample.setLastUpdate(Instant.now());
    sample.setLeg(leg);
    final boolean publish = view.getPublish() != null && view.getPublish();
    if (publish) {
      if (sample.getApproval() != null) {
        if (!sample.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
          throw new ApiException(
              HttpStatus.BAD_REQUEST,
              ApiError.builder().error(String.format("Sample %s is not approved", sample.getImlgs())).build()
          );
        }
      }
    }
    sample.setPublish(publish);
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

  @Override
  protected void validateParentResourceApproval(CuratorsSampleTsqpEntity entity) {
    CuratorsCruiseEntity cruise = entity.getCruise();
    if (cruise.getApproval() != null) {
      if (!cruise.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ApiError.builder().error(String.format("Cruise %s (%s) is not approved", cruise.getCruiseName(), cruise.getYear())).build()
        );
      }
    }
  }

  @Override
  protected void revokeChildResourceApproval(CuratorsSampleTsqpEntity entity) {
    entity.getIntervals().forEach(interval -> {
      ApprovalView approvalView = new ApprovalView();
      approvalView.setApprovalState(ApprovalState.REJECTED);
      approvalView.setComment("Sample rejected");
      intervalService.updateApproval(approvalView, interval.getId());
    });
  }
}
