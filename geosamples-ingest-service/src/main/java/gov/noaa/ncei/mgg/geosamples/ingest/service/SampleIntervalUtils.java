package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalBase;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.SampleBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public final class SampleIntervalUtils {

  public static final Map<String, String> SORT_MAPPING;

  static {
    Map<String, String> map = new HashMap<>();
    map.put("cruise", "parentEntity.cruise");
    map.put("sample", "parentEntity.sample");
    map.put("interval", "interval");
    map.put("facility", "parentEntity.facility");
    map.put("platform", "parentEntity.platform");
    SORT_MAPPING = Collections.unmodifiableMap(map);
  }

  public static <T extends IntervalBase<? extends SampleBase>> List<Specification<T>> getBaseSpecs(
      CombinedIntervalSampleSearchParameters searchParameters) {
    List<Specification<T>> specs = new ArrayList<>();

    List<String> munsellCode = searchParameters.getMunsellCode();
    if (!munsellCode.isEmpty()) {
      specs.add(SearchUtils.equal(munsellCode, e -> e.join(CuratorsIntervalEntity_.MUNSELL).get(CuratorsMunsellEntity_.MUNSELL_CODE)));
    }

    List<String> remarkCode = searchParameters.getRemarkCode();
    if (!remarkCode.isEmpty()) {
      specs.add(SearchUtils.equal(remarkCode, e -> e.join(CuratorsIntervalEntity_.REMARK).get(CuratorsRemarkEntity_.REMARK_CODE)));
    }

    List<String> weathMetaCode = searchParameters.getWeathMetaCode();
    if (!weathMetaCode.isEmpty()) {
      specs.add(SearchUtils.equal(weathMetaCode, e -> e.join(CuratorsIntervalEntity_.WEATH_META).get(CuratorsWeathMetaEntity_.WEATH_META_CODE)));
    }

    List<String> rockMinCode = searchParameters.getRockMinCode();
    if (!rockMinCode.isEmpty()) {
      specs.add(SearchUtils.equal(rockMinCode, e -> e.join(CuratorsIntervalEntity_.ROCK_MIN).get(CuratorsRockMinEntity_.ROCK_MIN_CODE)));
    }

    List<String> rockLithCode = searchParameters.getRockLithCode();
    if (!rockLithCode.isEmpty()) {
      specs.add(SearchUtils.equal(rockLithCode, e -> e.join(CuratorsIntervalEntity_.ROCK_LITH).get(CuratorsRockLithEntity_.ROCK_LITH_CODE)));
    }

    List<String> ageCode = searchParameters.getAgeCode();
    if (!ageCode.isEmpty()) {
      specs.add(SearchUtils.equal(ageCode, e -> e.join(CuratorsIntervalEntity_.AGE).get(CuratorsAgeEntity_.AGE_CODE)));
    }

    List<String> textCode = searchParameters.getTextCode();
    if (!textCode.isEmpty()) {
      specs.add(SearchUtils.equal(textCode, e -> e.join(CuratorsIntervalEntity_.TEXT1).get(CuratorsTextureEntity_.TEXTURE_CODE)));
      specs.add(SearchUtils.equal(textCode, e -> e.join(CuratorsIntervalEntity_.TEXT2).get(CuratorsTextureEntity_.TEXTURE_CODE)));
    }

    List<String> lithCode = searchParameters.getLithCode();
    if (!lithCode.isEmpty()) {
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.LITH1).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.LITH2).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP1).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP2).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP3).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP4).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP5).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
      specs.add(SearchUtils.equal(lithCode, e -> e.join(CuratorsIntervalEntity_.COMP6).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)));
    }

    Boolean publish = searchParameters.getPublish();
    if(publish != null) {
      specs.add((Specification<T>) (e, cq, cb) -> cb.and(
          cb.equal(e.get(CuratorsIntervalEntity_.PUBLISH), publish ? "Y" : "N"),
          cb.equal(e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.PUBLISH), publish ? "Y" : "N")));
    }

    List<Integer> interval = searchParameters.getInterval();
    if (!interval.isEmpty()) {
      specs.add(SearchUtils.equal(interval, CuratorsIntervalEntity_.INTERVAL));
    }

    List<String> imlgs = searchParameters.getImlgs();
    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.IMLGS)));
    }

    List<String> igsn = searchParameters.getIgsn();
    if (!igsn.isEmpty()) {
      specs.add(SearchUtils.equal(igsn, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.IGSN)));
      specs.add(SearchUtils.equal(igsn, CuratorsIntervalEntity_.IGSN));
    }

    List<String> provinceCode = searchParameters.getProvinceCode();
    if (!provinceCode.isEmpty()) {
      specs.add(SearchUtils.equal(provinceCode, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
          .join(CuratorsSampleTsqpEntity_.PROVINCE)
          .get(CuratorsProvinceEntity_.PROVINCE_CODE)));
    }

    List<String> piContains = searchParameters.getPiContains();
    if (!piContains.isEmpty()) {
      specs.add(SearchUtils.contains(piContains, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.PI)));
    }

    List<String> storageMethodCode = searchParameters.getStorageMethodCode();
    if (!storageMethodCode.isEmpty()) {
      specs.add(SearchUtils.equal(storageMethodCode, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
          .join(CuratorsSampleTsqpEntity_.STORAGE_METH)
          .get(CuratorsStorageMethEntity_.STORAGE_METH_CODE)));
    }

    List<String> date = searchParameters.getDate();
    if (!date.isEmpty()) {

      specs.add((Specification<T>) (e, cq, cb) -> {

        List<Predicate> pairs = new ArrayList<>();
        for (String dateStr : date) {
          Expression<Boolean> start = cb.greaterThanOrEqualTo(e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.BEGIN_DATE), dateStr);
          Expression<Boolean> end =
              cb.or(
                  cb.isNull(e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.END_DATE)),
                  cb.lessThanOrEqualTo(e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.END_DATE), dateStr)
              );
          Predicate pair = cb.and(start, end);
          pairs.add(pair);
        }

        return cb.or(pairs.toArray(new Predicate[0]));
      });
    }

    List<String> deviceCode = searchParameters.getDeviceCode();
    if (!deviceCode.isEmpty()) {
      specs.add(SearchUtils.equal(deviceCode, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
          .join(CuratorsSampleTsqpEntity_.DEVICE)
          .get(CuratorsDeviceEntity_.DEVICE_CODE)));
    }

    List<String> platform = searchParameters.getPlatformContains();
    if (!platform.isEmpty()) {
      specs.add(SearchUtils.contains(platform, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
          .join(CuratorsSampleTsqpEntity_.PLATFORM)
          .get(PlatformMasterEntity_.PLATFORM)));
    }

    List<String> facilityCode = searchParameters.getFacilityCode();
    if (!facilityCode.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCode, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
          .join(CuratorsSampleTsqpEntity_.FACILITY)
          .get(CuratorsFacilityEntity_.FACILITY_CODE)));
    }

    List<String> sampleContains = searchParameters.getSampleContains();
    if (!sampleContains.isEmpty()) {
      specs.add(SearchUtils.contains(sampleContains, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.SAMPLE)));
    }


    List<String> cruise = searchParameters.getCruiseContains();
    if (!cruise.isEmpty()) {
      specs.add(SearchUtils.contains(cruise, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY).get(CuratorsSampleTsqpEntity_.CRUISE)));
    }

    return specs;
  }


  public static <S extends SampleBase, T extends IntervalBase<S>> CombinedSampleIntervalView toViewBase(T entity) {
    CombinedSampleIntervalView view = new CombinedSampleIntervalView();
    S sampleEntity = entity.getParentEntity();
    view.setCruise(sampleEntity.getCruise());
    view.setSample(sampleEntity.getSample());
    view.setFacility(sampleEntity.getFacility().getFacilityCode());
    view.setPlatform(sampleEntity.getPlatform().getPlatform());
    view.setDevice(sampleEntity.getDevice().getDeviceCode());
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
    view.setEw(sampleEntity.getEw());
    view.setEndLon(sampleEntity.getEndLon());
    view.setEndLonDeg(sampleEntity.getEndLonDeg());
    view.setEndLonMin(sampleEntity.getEndLonMin());
    view.setEndEw(sampleEntity.getEndEw());
    view.setLatLonOrig(sampleEntity.getLatLonOrig());
    view.setWaterDepth(sampleEntity.getWaterDepth());
    view.setEndWaterDepth(sampleEntity.getEndWaterDepth());
    view.setStorageMeth(sampleEntity.getStorageMeth() == null ? null : sampleEntity.getStorageMeth().getStorageMethCode());
    view.setCoredLength(sampleEntity.getCoredLength());
    view.setCoredLengthMm(sampleEntity.getCoredLengthMm());
    view.setCoredDiam(sampleEntity.getCoredDiam());
    view.setCoredDiamMm(sampleEntity.getCoredDiamMm());
    view.setPi(sampleEntity.getPi());
    view.setProvince(sampleEntity.getProvince() == null ? null : sampleEntity.getProvince().getProvinceCode());
    view.setSampleLake(sampleEntity.getLake());
    view.setOtherLink(sampleEntity.getOtherLink());
    view.setLastUpdate(sampleEntity.getLastUpdate());
    view.setIgsn(sampleEntity.getIgsn());
    view.setLeg(sampleEntity.getLeg());
    view.setSampleComments(sampleEntity.getSampleComments());
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
    view.setLith1(entity.getLith1() == null ? null : entity.getLith1().getLithologyCode());
    view.setText1(entity.getText1() == null ? null : entity.getText1().getTextureCode());
    view.setLith2(entity.getLith2() == null ? null : entity.getLith2().getLithologyCode());
    view.setText2(entity.getText2() == null ? null : entity.getText2().getTextureCode());
    view.setComp1(entity.getComp1() == null ? null : entity.getComp1().getLithologyCode());
    view.setComp2(entity.getComp2() == null ? null : entity.getComp2().getLithologyCode());
    view.setComp3(entity.getComp3() == null ? null : entity.getComp3().getLithologyCode());
    view.setComp4(entity.getComp4() == null ? null : entity.getComp4().getLithologyCode());
    view.setComp5(entity.getComp5() == null ? null : entity.getComp5().getLithologyCode());
    view.setComp6(entity.getComp6() == null ? null : entity.getComp6().getLithologyCode());
    view.setDescription(entity.getDescription());
    view.setAge(entity.getAge() == null ? null : entity.getAge().getAgeCode());
    view.setAbsoluteAgeTop(entity.getAbsoluteAgeTop());
    view.setAbsoluteAgeBot(entity.getAbsoluteAgeBot());
    view.setWeight(entity.getWeight());
    view.setRockLith(entity.getRockLith() == null ? null : entity.getRockLith().getRockLithCode());
    view.setRockMin(entity.getRockMin() == null ? null : entity.getRockMin().getRockMinCode());
    view.setWeathMeta(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMetaCode());
    view.setRemark(entity.getRemark() == null ? null : entity.getRemark().getRemarkCode());
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
    view.setIntervalIgsn(entity.getIgsn());
    view.setIntervalParentIsgn(entity.getParentIgsn());

    view.setPublish("Y".equals(sampleEntity.getPublish()) && "Y".equals(entity.getPublish()));

    return view;
  }


  private SampleIntervalUtils() {

  }
}
