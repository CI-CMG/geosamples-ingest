package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalBase;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.SampleBase;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcSampleEntity_;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public final class SampleIntervalUtils {

  public static final Map<String, String> SORT_MAPPING;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("cruise", "parentEntity.cruise");
    map.put("sample", "parentEntity.sample");
    map.put("facility", "parentEntity.facility");
    map.put("platform", "parentEntity.platform");
    SORT_MAPPING = Collections.unmodifiableMap(map);
  }

  public static <T extends IntervalBase<? extends SampleBase>> List<Specification<T>> getBaseSpecs(
      CombinedIntervalSampleSearchParameters searchParameters) {
    List<Specification<T>> specs = new ArrayList<>();

    List<String> cruise = searchParameters.getCruise();
    List<String> facilityCode = searchParameters.getFacilityCode();
    List<String> platform = searchParameters.getPlatform();

    if (!cruise.isEmpty()) {
      specs.add((Specification<T>) (e, cq, cb) ->
          cb.or(cruise.stream().map(v ->
              cb.equal(
                  cb.lower(e.get(TempQcIntervalEntity_.PARENT_ENTITY).get(TempQcSampleEntity_.CRUISE)),
                  v.toLowerCase(Locale.ENGLISH)))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }

    if (!facilityCode.isEmpty()) {
      specs.add((Specification<T>) (e, cq, cb) ->
          cb.or(facilityCode.stream().map(v ->
              cb.equal(
                  e.get(TempQcIntervalEntity_.PARENT_ENTITY).get(TempQcSampleEntity_.FACILITY).get(CuratorsFacilityEntity_.FACILITY_CODE),
                  v))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }

    if (!platform.isEmpty()) {
      specs.add((Specification<T>) (e, cq, cb) ->
          cb.or(platform.stream().map(v ->
              cb.like(
                  cb.lower(e.get(TempQcIntervalEntity_.PARENT_ENTITY).get(TempQcSampleEntity_.PLATFORM).get(PlatformMasterEntity_.PLATFORM)),
                  SearchUtils.contains(v.toLowerCase(Locale.ENGLISH))))
              .collect(Collectors.toList()).toArray(new Predicate[0])));
    }

    return specs;
  }

  public static <S extends SampleBase, T extends IntervalBase<S>> CombinedSampleIntervalView toViewBase(T entity) {
    CombinedSampleIntervalView view = new CombinedSampleIntervalView();
    S sampleEntity = entity.getParentEntity();
    view.setCruise(sampleEntity.getCruise());
    view.setSample(sampleEntity.getSample());
    view.setFacility(sampleEntity.getFacility().getFacility());
    view.setPlatform(sampleEntity.getPlatform().getPlatform());
    view.setDevice(sampleEntity.getDevice().getDevice());
    view.setShipCode(sampleEntity.getShipCode());
    view.setBeginDate(sampleEntity.getBeginDate());
    view.setEndDate(sampleEntity.getEndDate());
    view.setLat(sampleEntity.getLat());
    view.setLatDeg(sampleEntity.getLatDeg());
    view.setLatMin(sampleEntity.getLatMin());
    view.setNs(sampleEntity.getNs());
    view.setEndLat(sampleEntity.getEndLat());
    view.setEndLatDeg(sampleEntity.getEndLatDeg());
    view.setEndLatMin(sampleEntity.getEndLatMin());
    view.setEndNs(sampleEntity.getEndNs());
    view.setLon(sampleEntity.getLon());
    view.setLonDeg(sampleEntity.getLonDeg());
    view.setLonMin(sampleEntity.getLonMin());
    view.setEndEw(sampleEntity.getEndEw());
    view.setEndLon(sampleEntity.getEndLon());
    view.setEndLonDeg(sampleEntity.getEndLonDeg());
    view.setEndLonMin(sampleEntity.getEndLonMin());
    view.setEndEw(sampleEntity.getEndEw());
    view.setLatLonOrig(sampleEntity.getLatLonOrig());
    view.setWaterDepth(sampleEntity.getWaterDepth());
    view.setEndWaterDepth(sampleEntity.getEndWaterDepth());
    view.setStorageMeth(sampleEntity.getStorageMeth() == null ? null : sampleEntity.getStorageMeth().getStorageMeth());
    view.setCoredLength(sampleEntity.getCoredLength());
    view.setCoredLengthMm(sampleEntity.getCoredLengthMm());
    view.setCoredDiam(sampleEntity.getCoredDiam());
    view.setCoredDiamMm(sampleEntity.getCoredDiamMm());
    view.setPi(sampleEntity.getPi());
    view.setProvince(sampleEntity.getProvince() == null ? null : sampleEntity.getProvince().getProvince());
    view.setSampleLake(sampleEntity.getLake());
    view.setOtherLink(sampleEntity.getOtherLink());
    view.setLastUpdate(sampleEntity.getLastUpdate());
    view.setIgsn(sampleEntity.getIgsn());
    view.setLeg(sampleEntity.getLeg());
    view.setSample(sampleEntity.getSampleComments());
    view.setSamplePublish(sampleEntity.getPublish());
    view.setObjectId(sampleEntity.getObjectId());
    view.setShowSampl(sampleEntity.getShowSampl());
    view.setImlgs(sampleEntity.getImlgs());

    view.setInterval(entity.getInterval());
    view.setDepthTop(entity.getDepthTop());
    view.setDepthTopMm(entity.getDepthTopMm());
    view.setDepthBot(entity.getDepthBot());
    view.setDepthBotMm(entity.getDepthBotMm());
    view.setDhCoreId(entity.getDhCoreId());
    view.setDhCoreLength(entity.getDhCoreLength());
    view.setDhCoreLengthMm(entity.getDhCoreLengthMm());
    view.setDhCoreInterval(entity.getDhCoreInterval());
    view.setdTopInDhCore(entity.getdTopInDhCore());
    view.setdTopMmInDhCore(entity.getdTopMmInDhCore());
    view.setdBotInDhCore(entity.getdBotInDhCore());
    view.setdBotMmInDhCore(entity.getdBotMmInDhCore());
    view.setLith1(entity.getLith1() == null ? null : entity.getLith1().getLithology());
    view.setText1(entity.getText1() == null ? null : entity.getText1().getTexture());
    view.setLith1(entity.getLith2() == null ? null : entity.getLith2().getLithology());
    view.setText2(entity.getText2() == null ? null : entity.getText2().getTexture());
    view.setComp1(entity.getComp1() == null ? null : entity.getComp1().getLithology());
    view.setComp2(entity.getComp2() == null ? null : entity.getComp2().getLithology());
    view.setComp3(entity.getComp3() == null ? null : entity.getComp3().getLithology());
    view.setComp4(entity.getComp4() == null ? null : entity.getComp4().getLithology());
    view.setComp5(entity.getComp5() == null ? null : entity.getComp5().getLithology());
    view.setComp6(entity.getComp6() == null ? null : entity.getComp6().getLithology());
    view.setDescription(entity.getDescription());
    view.setAge(entity.getAge() == null ? null : entity.getAge().getAge());
    view.setAbsoluteAgeTop(entity.getAbsoluteAgeTop());
    view.setAbsoluteAgeBot(entity.getAbsoluteAgeBot());
    view.setWeight(entity.getWeight());
    view.setRockLith(entity.getRockLith() == null ? null : entity.getRockLith().getRockLith());
    view.setRockMin(entity.getRockMin() == null ? null : entity.getRockMin().getRockMin());
    view.setWeathMeta(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMeta());
    view.setRemark(entity.getRemark() == null ? null : entity.getRemark().getRemark());
    view.setMunsellCode(entity.getMunsellCode());
    view.setMunsell(entity.getMunsell());
    view.setExhaustCode(entity.getExhaustCode());
    view.setPhotoLink(entity.getPhotoLink());
    view.setIntervalLake(entity.getLake());
    view.setUnitNumber(entity.getUnitNumber());
    view.setIntComments(entity.getIntComments());
    view.setDhDevice(entity.getDhDevice());
    view.setCmcdTop(entity.getCmcdTop());
    view.setMmcdTop(entity.getMmcdTop());
    view.setCmcdBot(entity.getCmcdBot());
    view.setMmcdBot(entity.getMmcdBot());
    view.setIntervalPublish(entity.getPublish());
    view.setIntervalIgsn(entity.getIgsn());
    view.setIntervalImlgs(entity.getImlgs());
    view.setIntervalParentIsgn(entity.getParentIgsn());

    return view;
  }


  private SampleIntervalUtils() {

  }
}
