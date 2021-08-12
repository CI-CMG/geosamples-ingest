package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import java.util.ArrayList;
import java.util.List;

public class EntitySignificantFields {

  private static List<SignificantField<?>> getFields(CuratorsSampleTsqpEntity sample, CuratorsSampleTsqpEntity entity) {
    List<SignificantField<?>> fields = new ArrayList<>();
    fields.add(new SignificantField<>(sample::getCruise, sample::setCruise, entity::getCruise, entity::setCruise));
    fields.add(new SignificantField<>(sample::getSample, sample::setSample, entity::getSample, entity::setSample));
    fields.add(new SignificantField<>(sample::getFacility, sample::setFacility, entity::getFacility, entity::setFacility));
    fields.add(new SignificantField<>(sample::getPlatform, sample::setPlatform, entity::getPlatform, entity::setPlatform));
    fields.add(new SignificantField<>(sample::getDevice, sample::setDevice, entity::getDevice, entity::setDevice));
    fields.add(new SignificantField<>(sample::getShipCode, sample::setShipCode, entity::getShipCode, entity::setShipCode));
    fields.add(new SignificantField<>(sample::getBeginDate, sample::setBeginDate, entity::getBeginDate, entity::setBeginDate));
    fields.add(new SignificantField<>(sample::getEndDate, sample::setEndDate, entity::getEndDate, entity::setEndDate));
    fields.add(new SignificantField<>(sample::getLat, sample::setLat, entity::getLat, entity::setLat));
    fields.add(new SignificantField<>(sample::getLatDeg, sample::setLatDeg, entity::getLatDeg, entity::setLatDeg));
    fields.add(new SignificantField<>(sample::getLatMin, sample::setLatMin, entity::getLatMin, entity::setLatMin));
    fields.add(new SignificantField<>(sample::getNs, sample::setNs, entity::getNs, entity::setNs));
    fields.add(new SignificantField<>(sample::getEndLat, sample::setEndLat, entity::getEndLat, entity::setEndLat));
    fields.add(new SignificantField<>(sample::getEndLatDeg, sample::setEndLatDeg, entity::getEndLatDeg, entity::setEndLatDeg));
    fields.add(new SignificantField<>(sample::getEndLatMin, sample::setEndLatMin, entity::getEndLatMin, entity::setEndLatMin));
    fields.add(new SignificantField<>(sample::getEndNs, sample::setEndNs, entity::getEndNs, entity::setEndNs));
    fields.add(new SignificantField<>(sample::getLon, sample::setLon, entity::getLon, entity::setLon));
    fields.add(new SignificantField<>(sample::getLonDeg, sample::setLonDeg, entity::getLonDeg, entity::setLonDeg));
    fields.add(new SignificantField<>(sample::getLonMin, sample::setLonMin, entity::getLonMin, entity::setLonMin));
    fields.add(new SignificantField<>(sample::getEw, sample::setEw, entity::getEw, entity::setEw));
    fields.add(new SignificantField<>(sample::getEndLon, sample::setEndLon, entity::getEndLon, entity::setEndLon));
    fields.add(new SignificantField<>(sample::getEndLonDeg, sample::setEndLonDeg, entity::getEndLonDeg, entity::setEndLonDeg));
    fields.add(new SignificantField<>(sample::getEndLonMin, sample::setEndLonMin, entity::getEndLonMin, entity::setEndLonMin));
    fields.add(new SignificantField<>(sample::getEndEw, sample::setEndEw, entity::getEndEw, entity::setEndEw));
    fields.add(new SignificantField<>(sample::getLatLonOrig, sample::setLatLonOrig, entity::getLatLonOrig, entity::setLatLonOrig));
    fields.add(new SignificantField<>(sample::getWaterDepth, sample::setWaterDepth, entity::getWaterDepth, entity::setWaterDepth));
    fields.add(new SignificantField<>(sample::getEndWaterDepth, sample::setEndWaterDepth, entity::getEndWaterDepth, entity::setEndWaterDepth));
    fields.add(new SignificantField<>(sample::getStorageMeth, sample::setStorageMeth, entity::getStorageMeth, entity::setStorageMeth));
    fields.add(new SignificantField<>(sample::getCoredLength, sample::setCoredLength, entity::getCoredLength, entity::setCoredLength));
    fields.add(new SignificantField<>(sample::getCoredLengthMm, sample::setCoredLengthMm, entity::getCoredLengthMm, entity::setCoredLengthMm));
    fields.add(new SignificantField<>(sample::getCoredDiam, sample::setCoredDiam, entity::getCoredDiam, entity::setCoredDiam));
    fields.add(new SignificantField<>(sample::getCoredDiamMm, sample::setCoredDiamMm, entity::getCoredDiamMm, entity::setCoredDiamMm));
    fields.add(new SignificantField<>(sample::getPi, sample::setPi, entity::getPi, entity::setPi));
    fields.add(new SignificantField<>(sample::getProvince, sample::setProvince, entity::getProvince, entity::setProvince));
    fields.add(new SignificantField<>(sample::getLake, sample::setLake, entity::getLake, entity::setLake));
    fields.add(new SignificantField<>(sample::getOtherLink, sample::setOtherLink, entity::getOtherLink, entity::setOtherLink));
    fields.add(new SignificantField<>(sample::getLastUpdate, sample::setLastUpdate, entity::getLastUpdate, entity::setLastUpdate));
    fields.add(new SignificantField<>(sample::getIgsn, sample::setIgsn, entity::getIgsn, entity::setIgsn));
    fields.add(new SignificantField<>(sample::getLeg, sample::setLeg, entity::getLeg, entity::setLeg));
    fields.add(new SignificantField<>(sample::getSampleComments, sample::setSampleComments, entity::getSampleComments, entity::setSampleComments));
    fields.add(new SignificantField<>(sample::getPublish, sample::setPublish, entity::getPublish, entity::setPublish));
    fields.add(new SignificantField<>(sample::getPreviousState, sample::setPreviousState, entity::getPreviousState, entity::setPreviousState));
    fields.add(new SignificantField<>(sample::getObjectId, sample::setObjectId, entity::getObjectId, entity::setObjectId));
    fields.add(new SignificantField<>(sample::getShape, sample::setShape, entity::getShape, entity::setShape));
    fields.add(new SignificantField<>(sample::getShowSampl, sample::setShowSampl, entity::getShowSampl, entity::setShowSampl));
    fields.add(new SignificantField<>(sample::getImlgs, sample::setImlgs, entity::getImlgs, entity::setImlgs));
    return fields;
  }

  private static List<SignificantField<?>> getFields(CuratorsIntervalEntity source, CuratorsIntervalEntity dest) {
    List<SignificantField<?>> fields = new ArrayList<>();

    fields.add(new SignificantField<>(source::getParentEntity, source::setParentEntity, dest::getParentEntity, dest::setParentEntity));
    fields.add(new SignificantField<>(source::getInterval, source::setInterval, dest::getInterval, dest::setInterval));
    fields.add(new SignificantField<>(source::getDepthTop, source::setDepthTop, dest::getDepthTop, dest::setDepthTop));
    fields.add(new SignificantField<>(source::getDepthTopMm, source::setDepthTopMm, dest::getDepthTopMm, dest::setDepthTopMm));
    fields.add(new SignificantField<>(source::getDepthBot, source::setDepthBot, dest::getDepthBot, dest::setDepthBot));
    fields.add(new SignificantField<>(source::getDepthBotMm, source::setDepthBotMm, dest::getDepthBotMm, dest::setDepthBotMm));
    fields.add(new SignificantField<>(source::getDhCoreId, source::setDhCoreId, dest::getDhCoreId, dest::setDhCoreId));
    fields.add(new SignificantField<>(source::getDhCoreLength, source::setDhCoreLength, dest::getDhCoreLength, dest::setDhCoreLength));
    fields.add(new SignificantField<>(source::getDhCoreLengthMm, source::setDhCoreLengthMm, dest::getDhCoreLengthMm, dest::setDhCoreLengthMm));
    fields.add(new SignificantField<>(source::getDhCoreInterval, source::setDhCoreInterval, dest::getDhCoreInterval, dest::setDhCoreInterval));
    fields.add(new SignificantField<>(source::getdTopInDhCore, source::setdTopInDhCore, dest::getdTopInDhCore, dest::setdTopInDhCore));
    fields.add(new SignificantField<>(source::getdTopMmInDhCore, source::setdTopMmInDhCore, dest::getdTopMmInDhCore, dest::setdTopMmInDhCore));
    fields.add(new SignificantField<>(source::getdBotInDhCore, source::setdBotInDhCore, dest::getdBotInDhCore, dest::setdBotInDhCore));
    fields.add(new SignificantField<>(source::getdBotMmInDhCore, source::setdBotMmInDhCore, dest::getdBotMmInDhCore, dest::setdBotMmInDhCore));
    fields.add(new SignificantField<>(source::getLith1, source::setLith1, dest::getLith1, dest::setLith1));
    fields.add(new SignificantField<>(source::getText1, source::setText1, dest::getText1, dest::setText1));
    fields.add(new SignificantField<>(source::getLith2, source::setLith2, dest::getLith2, dest::setLith2));
    fields.add(new SignificantField<>(source::getText2, source::setText2, dest::getText2, dest::setText2));
    fields.add(new SignificantField<>(source::getComp1, source::setComp1, dest::getComp1, dest::setComp1));
    fields.add(new SignificantField<>(source::getComp2, source::setComp2, dest::getComp2, dest::setComp2));
    fields.add(new SignificantField<>(source::getComp3, source::setComp3, dest::getComp3, dest::setComp3));
    fields.add(new SignificantField<>(source::getComp4, source::setComp4, dest::getComp4, dest::setComp4));
    fields.add(new SignificantField<>(source::getComp5, source::setComp5, dest::getComp5, dest::setComp5));
    fields.add(new SignificantField<>(source::getComp6, source::setComp6, dest::getComp6, dest::setComp6));
    fields.add(new SignificantField<>(source::getDescription, source::setDescription, dest::getDescription, dest::setDescription));
    fields.add(new SignificantField<>(source::getAge, source::setAge, dest::getAge, dest::setAge));
    fields.add(new SignificantField<>(source::getAbsoluteAgeTop, source::setAbsoluteAgeTop, dest::getAbsoluteAgeTop, dest::setAbsoluteAgeTop));
    fields.add(new SignificantField<>(source::getAbsoluteAgeBot, source::setAbsoluteAgeBot, dest::getAbsoluteAgeBot, dest::setAbsoluteAgeBot));
    fields.add(new SignificantField<>(source::getWeight, source::setWeight, dest::getWeight, dest::setWeight));
    fields.add(new SignificantField<>(source::getRockLith, source::setRockLith, dest::getRockLith, dest::setRockLith));
    fields.add(new SignificantField<>(source::getRockMin, source::setRockMin, dest::getRockMin, dest::setRockMin));
    fields.add(new SignificantField<>(source::getWeathMeta, source::setWeathMeta, dest::getWeathMeta, dest::setWeathMeta));
    fields.add(new SignificantField<>(source::getRemark, source::setRemark, dest::getRemark, dest::setRemark));
    fields.add(new SignificantField<>(source::getMunsellCode, source::setMunsellCode, dest::getMunsellCode, dest::setMunsellCode));
    fields.add(new SignificantField<>(source::getMunsell, source::setMunsell, dest::getMunsell, dest::setMunsell));
    fields.add(new SignificantField<>(source::getExhaustCode, source::setExhaustCode, dest::getExhaustCode, dest::setExhaustCode));
    fields.add(new SignificantField<>(source::getPhotoLink, source::setPhotoLink, dest::getPhotoLink, dest::setPhotoLink));
    fields.add(new SignificantField<>(source::getLake, source::setLake, dest::getLake, dest::setLake));
    fields.add(new SignificantField<>(source::getUnitNumber, source::setUnitNumber, dest::getUnitNumber, dest::setUnitNumber));
    fields.add(new SignificantField<>(source::getIntComments, source::setIntComments, dest::getIntComments, dest::setIntComments));
    fields.add(new SignificantField<>(source::getDhDevice, source::setDhDevice, dest::getDhDevice, dest::setDhDevice));
    fields.add(new SignificantField<>(source::getCmcdTop, source::setCmcdTop, dest::getCmcdTop, dest::setCmcdTop));
    fields.add(new SignificantField<>(source::getMmcdTop, source::setMmcdTop, dest::getMmcdTop, dest::setMmcdTop));
    fields.add(new SignificantField<>(source::getCmcdBot, source::setCmcdBot, dest::getCmcdBot, dest::setCmcdBot));
    fields.add(new SignificantField<>(source::getMmcdBot, source::setMmcdBot, dest::getMmcdBot, dest::setMmcdBot));
    fields.add(new SignificantField<>(source::getPublish, source::setPublish, dest::getPublish, dest::setPublish));
    fields.add(new SignificantField<>(source::getPreviousState, source::setPreviousState, dest::getPreviousState, dest::setPreviousState));
    fields.add(new SignificantField<>(source::getIgsn, source::setIgsn, dest::getIgsn, dest::setIgsn));
    return fields;
  }

  public static void copy(CuratorsSampleTsqpEntity source, CuratorsSampleTsqpEntity dest) {
    for (SignificantField<?> field : getFields(source, dest)) {
      field.copy1To2();
    }
  }

  public static void copy(CuratorsIntervalEntity source, CuratorsIntervalEntity dest) {
    for (SignificantField<?> field :  getFields(source, dest)) {
      field.copy1To2();
    }
  }

  public static boolean equals(CuratorsSampleTsqpEntity s1, CuratorsSampleTsqpEntity s2) {
    for (SignificantField<?> field : getFields(s1, s2)) {
      if (!field.isEqual()) {
        return false;
      }
    }
    return true;
  }

  public static boolean equals(CuratorsIntervalEntity s1, CuratorsIntervalEntity s2) {
    for (SignificantField<?> field : getFields(s1, s2)) {
      if (!field.isEqual()) {
        return false;
      }
    }
    return true;
  }

}
