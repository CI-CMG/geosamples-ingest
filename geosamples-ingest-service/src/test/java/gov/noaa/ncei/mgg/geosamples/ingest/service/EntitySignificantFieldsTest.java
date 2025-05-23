package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.time.Instant;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class EntitySignificantFieldsTest {

  @Test
  public void testSampleCopy() {

    Instant now = Instant.now();

    CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
    facility.setFacilityCode("facility");
    facility.setId(1L);

    PlatformMasterEntity platform = new PlatformMasterEntity();
    platform.setPlatform("platform");
    platform.setId(2L);

    CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
    platformMapping.setId(3L);
    platformMapping.setPlatform(platform);

    CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
    facilityMapping.setId(4L);
    facilityMapping.setFacility(facility);

    CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
    cruise.setId(5L);
    cruise.setCruiseName("cruise");
    cruise.addFacilityMapping(facilityMapping);
    cruise.addPlatformMapping(platformMapping);

    CuratorsLegEntity leg = new CuratorsLegEntity();
    leg.setLegName("leg");
    leg.setCruise(cruise);

    CuratorsDeviceEntity device = new CuratorsDeviceEntity();
    device.setDevice("device");

    CuratorsStorageMethEntity storageMeth = new CuratorsStorageMethEntity();
    storageMeth.setStorageMeth("storageMeth");

    CuratorsProvinceEntity province = new CuratorsProvinceEntity();
    province.setProvince("province");

    Geometry shape = new GeometryFactory().createPoint();

    CuratorsSampleTsqpEntity src = new CuratorsSampleTsqpEntity();
    src.setCruise(cruise);
    src.setSample("sample");
    src.setCruiseFacility(facilityMapping);
    src.setCruisePlatform(platformMapping);
    src.setDevice(device);
    src.setBeginDate("beginDate");
    src.setEndDate("endDate");
    src.setLat(1.1);
    src.setEndLat(2.2);
    src.setLon(3.3);
    src.setEndLon(4.4);
    src.setLatLonOrig("latLonOrig");
    src.setWaterDepth(5);
    src.setEndWaterDepth(6);
    src.setStorageMeth(storageMeth);
    src.setCoredLength(7);
    src.setCoredLengthMm(8);
    src.setCoredDiam(9);
    src.setCoredDiamMm(10);
    src.setPi("pi");
    src.setProvince(province);
    src.setLake("lake");
    src.setOtherLink("otherLink");
    src.setLastUpdate(now);
    src.setIgsn("igsn");
    src.setLeg(leg);
    src.setSampleComments("sampleComments");
    src.setPublish(true);
    src.setPreviousState("previousState");
    src.setShape(shape);
    src.setShowSampl("showSample");
    src.setImlgs("imlgs");

    CuratorsSampleTsqpEntity dest = new CuratorsSampleTsqpEntity();

    assertFalse(EntitySignificantFields.equals(src, dest));
    EntitySignificantFields.copy(src, dest);
    assertTrue(EntitySignificantFields.equals(src, dest));

    assertEquals("cruise", dest.getCruise().getCruiseName());
    assertEquals("sample", dest.getSample());
    assertEquals(facilityMapping, dest.getCruiseFacility());
    assertEquals(platformMapping, dest.getCruisePlatform());
    assertEquals(device, dest.getDevice());
    assertEquals("beginDate", dest.getBeginDate());
    assertEquals("endDate", dest.getEndDate());
    assertEquals(1.1, dest.getLat());
    assertEquals(2.2, dest.getEndLat());
    assertEquals(3.3, dest.getLon());
    assertEquals(4.4, dest.getEndLon());
    assertEquals("latLonOrig", dest.getLatLonOrig());
    assertEquals(5, dest.getWaterDepth());
    assertEquals(6, dest.getEndWaterDepth());
    assertEquals(storageMeth, dest.getStorageMeth());
    assertEquals(7, dest.getCoredLength());
    assertEquals(8, dest.getCoredLengthMm());
    assertEquals(9, dest.getCoredDiam());
    assertEquals(10, dest.getCoredDiamMm());
    assertEquals("pi", dest.getPi());
    assertEquals(province, dest.getProvince());
    assertEquals("lake", dest.getLake());
    assertEquals("otherLink", dest.getOtherLink());
    assertEquals(now, dest.getLastUpdate());
    assertEquals("igsn", dest.getIgsn());
    assertEquals("leg", dest.getLeg().getLegName());
    assertEquals("sampleComments", dest.getSampleComments());
    assertTrue(dest.isPublish());
    assertEquals("previousState", dest.getPreviousState());
    assertEquals(shape, dest.getShape());
    assertEquals("showSample", dest.getShowSampl());
    assertEquals("imlgs", dest.getImlgs());
  }

  @Test
  public void testIntervalCopy() {

    CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
    facility.setFacilityCode("facility");
    facility.setId(1L);

    PlatformMasterEntity platform = new PlatformMasterEntity();
    platform.setPlatform("platform");
    platform.setId(2L);

    CuratorsCruisePlatformEntity platformMapping = new CuratorsCruisePlatformEntity();
    platformMapping.setId(3L);
    platformMapping.setPlatform(platform);

    CuratorsCruiseFacilityEntity facilityMapping = new CuratorsCruiseFacilityEntity();
    facilityMapping.setId(4L);
    facilityMapping.setFacility(facility);

    CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
    cruise.setCruiseName("cruise");
    cruise.setId(5L);
    cruise.addFacilityMapping(facilityMapping);
    cruise.addPlatformMapping(platformMapping);

    CuratorsLegEntity leg = new CuratorsLegEntity();
    leg.setLegName("leg");
    leg.setCruise(cruise);

    CuratorsDeviceEntity device = new CuratorsDeviceEntity();
    device.setDevice("device");

    CuratorsSampleTsqpEntity parent = new CuratorsSampleTsqpEntity();

    parent.setCruise(cruise);
    parent.setSample("sample");
    parent.setCruiseFacility(facilityMapping);
    parent.setCruisePlatform(platformMapping);
    parent.setDevice(device);
    parent.setIgsn("igsn");
    parent.setImlgs("imlgs");


    CuratorsIntervalEntity src = new CuratorsIntervalEntity();
    src.setSample(parent);

    CuratorsLithologyEntity lith1 = new CuratorsLithologyEntity();
    lith1.setLithology("lith1");

    CuratorsLithologyEntity lith2 = new CuratorsLithologyEntity();
    lith2.setLithology("lith2");

    CuratorsTextureEntity text1 = new CuratorsTextureEntity();
    text1.setTexture("text1");

    CuratorsTextureEntity text2 = new CuratorsTextureEntity();
    text2.setTexture("text2");

    CuratorsLithologyEntity comp1 = new CuratorsLithologyEntity();
    comp1.setLithology("comp1");
    CuratorsLithologyEntity comp2 = new CuratorsLithologyEntity();
    comp2.setLithology("comp2");
    CuratorsLithologyEntity comp3 = new CuratorsLithologyEntity();
    comp3.setLithology("comp3");
    CuratorsLithologyEntity comp4 = new CuratorsLithologyEntity();
    comp4.setLithology("comp4");
    CuratorsLithologyEntity comp5 = new CuratorsLithologyEntity();
    comp5.setLithology("comp5");
    CuratorsLithologyEntity comp6 = new CuratorsLithologyEntity();
    comp6.setLithology("comp6");

    CuratorsAgeEntity age = new CuratorsAgeEntity();
    age.setAge("age");

    CuratorsRockLithEntity rockLith = new CuratorsRockLithEntity();
    rockLith.setRockLith("rockLith");

    CuratorsRockMinEntity rockMin = new CuratorsRockMinEntity();
    rockMin.setRockMin("rockMin");

    CuratorsWeathMetaEntity weathMeta = new CuratorsWeathMetaEntity();
    weathMeta.setWeathMeta("weathMeta");

    CuratorsRemarkEntity remark = new CuratorsRemarkEntity();
    remark.setRemark("remark");

    src.setInterval(1);
    src.setDepthTop(2);
    src.setDepthTopMm(3);
    src.setDepthBot(4);
    src.setDepthBotMm(5);
    src.setDhCoreId("dhCoreId");
    src.setDhCoreLength(6);
    src.setDhCoreLengthMm(7);
    src.setDhCoreInterval(8);
    src.setdTopInDhCore(9);
    src.setdTopMmInDhCore(10);
    src.setdBotInDhCore(11);
    src.setdBotMmInDhCore(12);
    src.setLith1(lith1);
    src.setText1(text1);
    src.setLith2(lith2);
    src.setText2(text2);
    src.setComp1(comp1);
    src.setComp2(comp2);
    src.setComp3(comp3);
    src.setComp4(comp4);
    src.setComp5(comp5);
    src.setComp6(comp6);
    src.setDescription("description");
    src.setAges(Collections.singletonList(age));
    src.setAbsoluteAgeTop("absoluteAgeTop");
    src.setAbsoluteAgeBot("absoluteAgeBot");
    src.setWeight(13.1);
    src.setRockLith(rockLith);
    src.setRockMin(rockMin);
    src.setWeathMeta(weathMeta);
    src.setRemark(remark);
    src.setMunsellCode("munsellCode");
    src.setMunsell("munsell");
    src.setExhaustCode("exhaustCode");
    src.setPhotoLink("photoLink");
    src.setLake("lake");
    src.setUnitNumber("unitNumber");
    src.setIntComments("intComments");
    src.setDhDevice("dhDevice");
    src.setCmcdTop(14);
    src.setMmcdTop(15);
    src.setCmcdBot(16);
    src.setMmcdBot(17);
    src.setPublish(true);
    src.setPreviousState("previousState");
    src.setIgsn("igsn2");



    CuratorsIntervalEntity dest = new CuratorsIntervalEntity();

    assertFalse(EntitySignificantFields.equals(src, dest));
    EntitySignificantFields.copy(src, dest);
    assertTrue(EntitySignificantFields.equals(src, dest));


    assertEquals(1, dest.getInterval());
    assertEquals(2, dest.getDepthTop());
    assertEquals(3, dest.getDepthTopMm());
    assertEquals(4, dest.getDepthBot());
    assertEquals(5, dest.getDepthBotMm());
    assertEquals("dhCoreId", dest.getDhCoreId());
    assertEquals(6, dest.getDhCoreLength());
    assertEquals(7, dest.getDhCoreLengthMm());
    assertEquals(8, dest.getDhCoreInterval());
    assertEquals(9, dest.getdTopInDhCore());
    assertEquals(10, dest.getdTopMmInDhCore());
    assertEquals(11, dest.getdBotInDhCore());
    assertEquals(12, dest.getdBotMmInDhCore());
    assertEquals(lith1, dest.getLith1());
    assertEquals(text1, dest.getText1());
    assertEquals(lith2, dest.getLith2());
    assertEquals(text2, dest.getText2());
    assertEquals(comp1, dest.getComp1());
    assertEquals(comp2, dest.getComp2());
    assertEquals(comp3, dest.getComp3());
    assertEquals(comp4, dest.getComp4());
    assertEquals(comp5, dest.getComp5());
    assertEquals(comp6, dest.getComp6());
    assertEquals("description", dest.getDescription());
    assertEquals(1, dest.getAges().size());
    assertTrue(dest.getAges().contains(age));
    assertEquals("absoluteAgeTop", dest.getAbsoluteAgeTop());
    assertEquals("absoluteAgeBot", dest.getAbsoluteAgeBot());
    assertEquals(13.1, dest.getWeight());
    assertEquals(rockLith, dest.getRockLith());
    assertEquals(rockMin, dest.getRockMin());
    assertEquals(weathMeta, dest.getWeathMeta());
    assertEquals(remark, dest.getRemark());
    assertEquals("munsellCode", dest.getMunsellCode());
    assertEquals("munsell", dest.getMunsell());
    assertEquals("exhaustCode", dest.getExhaustCode());
    assertEquals("photoLink", dest.getPhotoLink());
    assertEquals("lake", dest.getLake());
    assertEquals("unitNumber", dest.getUnitNumber());
    assertEquals("intComments", dest.getIntComments());
    assertEquals("dhDevice", dest.getDhDevice());
    assertEquals(14, dest.getCmcdTop());
    assertEquals(15, dest.getMmcdTop());
    assertEquals(16, dest.getCmcdBot());
    assertEquals(17, dest.getMmcdBot());
    assertTrue(dest.isPublish());
    assertEquals("previousState", dest.getPreviousState());

    assertEquals(parent, dest.getSample());
    assertEquals(device, dest.getSample().getDevice());
    assertEquals("sample", dest.getSample().getSample());
    assertEquals("cruise", dest.getSample().getCruise().getCruiseName());
    assertEquals(platformMapping, dest.getSample().getCruisePlatform());
    assertEquals(facilityMapping, dest.getSample().getCruiseFacility());
    assertEquals("igsn2", dest.getIgsn());
    assertEquals("igsn", dest.getSample().getIgsn());

  }

}