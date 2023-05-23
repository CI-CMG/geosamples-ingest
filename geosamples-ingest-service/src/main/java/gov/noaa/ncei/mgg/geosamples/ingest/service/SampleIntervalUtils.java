package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public final class SampleIntervalUtils {

  public static final Map<String, String> SORT_MAPPING;


  static {
    Map<String, String> map = new HashMap<>();
    map.put("publish", "publish");
    map.put("imlgs", "sample.imlgs");
    map.put("interval", "interval");
    map.put("igsn", "sample.igsn");
    map.put("intervalIgsn", "igsn");
    map.put("cruise", "sample.cruise.cruiseName");
    map.put("sample", "sample.sample");
    map.put("facility", "sample.cruiseFacility.facility.facilityCode");
    map.put("platform", "sample.cruisePlatform.platform.platformNormalized");
    map.put("beginDate", "sample.beginDate");
    SORT_MAPPING = Collections.unmodifiableMap(map);
  }

  public static List<Specification<CuratorsIntervalEntity>> getBaseSpecs(
      CombinedIntervalSampleSearchParameters searchParameters) {
    List<Specification<CuratorsIntervalEntity>> specs = new ArrayList<>();

    List<String> munsellCode = searchParameters.getMunsellCode();
    if (!munsellCode.isEmpty()) {
      specs.add(SearchUtils.equal(munsellCode, e -> e.get(CuratorsMunsellEntity_.MUNSELL_CODE)));
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
      specs.add(
          (Specification<CuratorsIntervalEntity>) (e1, cq, cb) -> {
            Subquery<CuratorsAgeEntity> subquery = cq.subquery(CuratorsAgeEntity.class);
            Root<CuratorsAgeEntity> subqueryRoot = subquery.from(CuratorsAgeEntity.class);
            subquery.select(subqueryRoot);

            subquery.where(cb.and(cb.equal(e1.get(CuratorsIntervalEntity_.ID), subqueryRoot.join(CuratorsAgeEntity_.INTERVALS).get(CuratorsIntervalEntity_.ID)),
                subqueryRoot.get(CuratorsAgeEntity_.AGE_CODE).in(ageCode)));

            return cb.exists(subquery);
          }
      );
    }

    List<String> textCode = searchParameters.getTextCode();
    if (!textCode.isEmpty()) {
      specs.add((Specification<CuratorsIntervalEntity>) (e1, cq, cb) -> cb.or(
          SearchUtils.equalOne(e1, cb, textCode, e -> e.join(CuratorsIntervalEntity_.TEXT1).get(CuratorsTextureEntity_.TEXTURE_CODE)),
          SearchUtils.equalOne(e1, cb, textCode, e -> e.join(CuratorsIntervalEntity_.TEXT2).get(CuratorsTextureEntity_.TEXTURE_CODE))
      ));
    }

    List<String> lithCode = searchParameters.getLithCode();
    if (!lithCode.isEmpty()) {
      specs.add((Specification<CuratorsIntervalEntity>) (e1, cq, cb) -> cb.or(
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.LITH1).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.LITH2).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP1).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP2).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP3).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP4).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP5).get(CuratorsLithologyEntity_.LITHOLOGY_CODE)),
          SearchUtils.equalOne(e1, cb, lithCode, e -> e.join(CuratorsIntervalEntity_.COMP6).get(CuratorsLithologyEntity_.LITHOLOGY_CODE))
      ));
    }

    Boolean publish = searchParameters.getPublish();
    if (publish != null) {
      if (publish) {
        specs.add((Specification<CuratorsIntervalEntity>) (e, cq, cb) -> cb.and(
            cb.equal(e.get(CuratorsIntervalEntity_.PUBLISH), "Y"),
            cb.equal(e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.PUBLISH), "Y")));
      } else {
        specs.add((Specification<CuratorsIntervalEntity>) (e, cq, cb) -> cb.or(
            cb.not(cb.equal(e.get(CuratorsIntervalEntity_.PUBLISH), "Y")),
            cb.not(cb.equal(e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.PUBLISH), "Y"))));
      }
    }

    List<Integer> interval = searchParameters.getInterval();
    if (!interval.isEmpty()) {
      specs.add(SearchUtils.equal(interval, CuratorsIntervalEntity_.INTERVAL));
    }

    List<String> imlgs = searchParameters.getImlgs();
    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, e -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.IMLGS)));
    }

    List<String> igsn = searchParameters.getIgsn();
    if (!igsn.isEmpty()) {
      specs.add((Specification<CuratorsIntervalEntity>) (e1, cq, cb) -> cb.or(
          SearchUtils.equalOne(e1, cb, igsn, e -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.IGSN)),
          SearchUtils.equalOne(e1, cb, igsn, e -> e.get(CuratorsIntervalEntity_.IGSN))
      ));
    }

    List<String> provinceCode = searchParameters.getProvinceCode();
    if (!provinceCode.isEmpty()) {
      specs.add(SearchUtils.equal(provinceCode, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
          .join(CuratorsSampleTsqpEntity_.PROVINCE)
          .get(CuratorsProvinceEntity_.PROVINCE_CODE)));
    }

    List<String> piContains = searchParameters.getPiContains();
    if (!piContains.isEmpty()) {
      specs.add(SearchUtils.contains(piContains, e -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.PI)));
    }

    List<String> storageMethodCode = searchParameters.getStorageMethodCode();
    if (!storageMethodCode.isEmpty()) {
      specs.add(SearchUtils.equal(storageMethodCode, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
          .join(CuratorsSampleTsqpEntity_.STORAGE_METH)
          .get(CuratorsStorageMethEntity_.STORAGE_METH_CODE)));
    }

    List<String> date = searchParameters.getDate();
    if (!date.isEmpty()) {

      specs.add(SearchUtils.contains(date, e -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.BEGIN_DATE)));

    }

    List<String> deviceCode = searchParameters.getDeviceCode();
    if (!deviceCode.isEmpty()) {
      specs.add(SearchUtils.equal(deviceCode, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
          .join(CuratorsSampleTsqpEntity_.DEVICE)
          .get(CuratorsDeviceEntity_.DEVICE_CODE)));
    }

    List<String> platform = searchParameters.getPlatformContains();
    if (!platform.isEmpty()) {
//      specs.add(SearchUtils.contains(platform, e -> e.join(CuratorsIntervalEntity_.PARENT_ENTITY)
//          .join(CuratorsSampleTsqpEntity_.PLATFORM)
//          .get(PlatformMasterEntity_.PLATFORM)));
      specs.add(SearchUtils.contains(platform, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
          .join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM)
          .join(CuratorsCruisePlatformEntity_.PLATFORM)
          .get(PlatformMasterEntity_.PLATFORM)));
    }

    List<String> facilityCode = searchParameters.getFacilityCode();
    if (!facilityCode.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCode, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
//          .join(CuratorsSampleTsqpEntity_.FACILITY)
          .join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY)
          .join(CuratorsCruiseFacilityEntity_.FACILITY)
          .get(CuratorsFacilityEntity_.FACILITY_CODE)));
    }

    List<String> sampleContains = searchParameters.getSampleContains();
    if (!sampleContains.isEmpty()) {
      specs.add(SearchUtils.contains(sampleContains, e -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.SAMPLE)));
    }

    List<String> cruise = searchParameters.getCruiseContains();
    if (!cruise.isEmpty()) {
      specs.add(SearchUtils.contains(cruise, e -> e.join(CuratorsIntervalEntity_.SAMPLE)
          .join(CuratorsSampleTsqpEntity_.CRUISE)
          .get(CuratorsCruiseEntity_.CRUISE_NAME)));
    }

    Geometry area = searchParameters.getArea();
    if (area != null) {
      specs.add(within(normalizeArea(area)));
    }

    return specs;
  }

  public static Specification<CuratorsIntervalEntity> within(Geometry shape) {
    return (feature, cq, cb) -> cb.and(
        cb.isNotNull(feature.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.SHAPE)),
        new WithinPredicate(cb, feature.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.SHAPE), shape)
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

  public static CombinedSampleIntervalView toViewBase(CuratorsIntervalEntity entity) {
    CombinedSampleIntervalView view = new CombinedSampleIntervalView();
    CuratorsSampleTsqpEntity sampleEntity = entity.getSample();
    view.setCruise(sampleEntity.getCruise().getCruiseName());
    view.setSample(sampleEntity.getSample());
    view.setFacility(sampleEntity.getCruiseFacility().getFacility().getFacilityCode());
    view.setPlatform(sampleEntity.getCruisePlatform().getPlatform().getPlatform());
    view.setDevice(sampleEntity.getDevice().getDeviceCode());
    view.setBeginDate(sampleEntity.getBeginDate());
    view.setEndDate(sampleEntity.getEndDate());
    view.setLat(sampleEntity.getLat());
    view.setEndLat(sampleEntity.getEndLat());
    view.setLon(sampleEntity.getLon());
    view.setEndLon(sampleEntity.getEndLon());
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
    view.setLastUpdate(sampleEntity.getLastUpdate());
    view.setIgsn(sampleEntity.getIgsn());
    view.setLeg(sampleEntity.getLeg() == null ? null : sampleEntity.getLeg().getLegName());
    view.setSampleComments(sampleEntity.getSampleComments());
    view.setShowSampl(sampleEntity.getShowSampl());
    view.setImlgs(sampleEntity.getImlgs());

    view.setInterval(entity.getInterval());
    view.setIntervalId(entity.getId());
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
    view.setAges(entity.getAges() == null || entity.getAges().isEmpty() ? Collections.emptyList() :
        entity.getAges().stream()
            .map(CuratorsAgeEntity::getAgeCode)
            .sorted()
            .collect(Collectors.toList())
    );
    view.setWeight(entity.getWeight());
    view.setRockLith(entity.getRockLith() == null ? null : entity.getRockLith().getRockLithCode());
    view.setRockMin(entity.getRockMin() == null ? null : entity.getRockMin().getRockMinCode());
    view.setWeathMeta(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMetaCode());
    view.setRemark(entity.getRemark() == null ? null : entity.getRemark().getRemarkCode());
    view.setMunsellCode(entity.getMunsellCode());
    view.setMunsell(entity.getMunsell());
    view.setExhaustCode(entity.getExhaustCode());
    view.setPhotoLink(entity.getPhotoLink());
    view.setIntComments(entity.getIntComments());
    view.setCmcdTop(entity.getCmcdTop());
    view.setMmcdTop(entity.getMmcdTop());
    view.setCmcdBot(entity.getCmcdBot());
    view.setMmcdBot(entity.getMmcdBot());
    view.setIntervalIgsn(entity.getIgsn());
    view.setIntervalParentIsgn(sampleEntity.getIgsn());

    if (entity.getApproval() != null) {
      view.setApprovalState(entity.getApproval().getApprovalState());
    }

    view.setPublish(sampleEntity.isPublish() && entity.isPublish());

    return view;
  }


  private SampleIntervalUtils() {

  }
}
