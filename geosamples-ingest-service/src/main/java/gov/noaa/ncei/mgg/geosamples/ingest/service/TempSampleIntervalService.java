package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedIntervalSampleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CombinedSampleIntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalIdView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.SampleIntervalAcceptanceView;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcSampleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcSampleRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TempSampleIntervalService extends
    SearchServiceBase<TempQcIntervalEntity, IntervalPk, CombinedIntervalSampleSearchParameters, CombinedSampleIntervalView, TempQcIntervalRepository> {

  private final TempQcIntervalRepository tempQcIntervalRepository;
  private final TempQcSampleRepository tempQcSampleRepository;
  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final ServiceProperties serviceProperties;

  @Autowired
  public TempSampleIntervalService(TempQcIntervalRepository tempQcIntervalRepository,
      TempQcSampleRepository tempQcSampleRepository,
      CuratorsIntervalRepository curatorsIntervalRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      ServiceProperties serviceProperties) {
    this.tempQcIntervalRepository = tempQcIntervalRepository;
    this.tempQcSampleRepository = tempQcSampleRepository;
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.serviceProperties = serviceProperties;
  }

  private CuratorsSampleTsqpEntity saveCopy(TempQcSampleEntity sample, String imlgs) {
    CuratorsSampleTsqpEntity entity = new CuratorsSampleTsqpEntity();
    entity.setImlgs(imlgs);
    entity.setCruise(sample.getCruise());
    entity.setSample(sample.getSample());
    entity.setFacility(sample.getFacility());
    entity.setPlatform(sample.getPlatform());
    entity.setDevice(sample.getDevice());
    entity.setShipCode(sample.getShipCode());
    entity.setBeginDate(sample.getBeginDate());
    entity.setEndDate(sample.getEndDate());
    entity.setLat(sample.getLat());
    entity.setLatDeg(sample.getLatDeg());
    entity.setLatMin(sample.getLatMin());
    entity.setNs(sample.getNs());
    entity.setEndLat(sample.getEndLat());
    entity.setEndLatDeg(sample.getEndLatDeg());
    entity.setEndLatMin(sample.getEndLatMin());
    entity.setEndNs(sample.getEndNs());
    entity.setLon(sample.getLon());
    entity.setLonDeg(sample.getLonDeg());
    entity.setLonMin(sample.getLonMin());
    entity.setEw(sample.getEw());
    entity.setEndLon(sample.getEndLon());
    entity.setEndLonDeg(sample.getEndLonDeg());
    entity.setEndLonMin(sample.getEndLonMin());
    entity.setEndEw(sample.getEndEw());
    entity.setLatLonOrig(sample.getLatLonOrig());
    entity.setWaterDepth(sample.getWaterDepth());
    entity.setEndWaterDepth(sample.getEndWaterDepth());
    entity.setStorageMeth(sample.getStorageMeth());
    entity.setCoredLength(sample.getCoredLength());
    entity.setCoredLengthMm(sample.getCoredLengthMm());
    entity.setCoredDiam(sample.getCoredDiam());
    entity.setCoredDiamMm(sample.getCoredDiamMm());
    entity.setPi(sample.getPi());
    entity.setProvince(sample.getProvince());
    entity.setLake(sample.getLake());
    entity.setOtherLink(sample.getOtherLink());
    entity.setLastUpdate(sample.getLastUpdate());
    entity.setIgsn(sample.getIgsn());
    entity.setLeg(sample.getLeg());
    entity.setSampleComments(sample.getSampleComments());
    entity.setPublish(sample.getPublish());
    entity.setPreviousState(sample.getPreviousState());
    entity.setObjectId(sample.getObjectId());
    entity.setShape(sample.getShape());
    entity.setShowSampl(serviceProperties.getShowSampleBaseUrl() + "?" + imlgs);
    return curatorsSampleTsqpRepository.saveAndFlush(entity);
  }

  private CuratorsSampleTsqpEntity resolveSample(String imlgs) {
    if (imlgs.startsWith("temp_")) {
      String acceptedImlgs = imlgs.replaceAll("^temp_", "");
      Optional<CuratorsSampleTsqpEntity> maybeSample = curatorsSampleTsqpRepository.findById(acceptedImlgs);
      return maybeSample.orElseGet(
          () -> tempQcSampleRepository.findById(imlgs)
              .map(s -> saveCopy(s, acceptedImlgs))
              .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find " + imlgs).build()))
      );
    } else {
      return curatorsSampleTsqpRepository.findById(imlgs)
          .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find " + imlgs).build()));
    }
  }

  private void saveCopyInterval(TempQcIntervalEntity interval, CuratorsSampleTsqpEntity sample) {
    CuratorsIntervalEntity entity = new CuratorsIntervalEntity();
    entity.setParentEntity(sample);
    entity.setInterval(interval.getInterval());
    entity.setDepthTop(interval.getDepthTop());
    entity.setDepthTopMm(interval.getDepthTopMm());
    entity.setDepthBot(interval.getDepthBot());
    entity.setDepthBotMm(interval.getDepthBotMm());
    entity.setDhCoreId(interval.getDhCoreId());
    entity.setDhCoreLength(interval.getDhCoreLength());
    entity.setDhCoreLengthMm(interval.getDhCoreLengthMm());
    entity.setDhCoreInterval(interval.getDhCoreInterval());
    entity.setdTopInDhCore(interval.getdTopInDhCore());
    entity.setdTopMmInDhCore(interval.getdTopMmInDhCore());
    entity.setdBotInDhCore(interval.getdBotInDhCore());
    entity.setdBotMmInDhCore(interval.getdBotMmInDhCore());
    entity.setLith1(interval.getLith1());
    entity.setText1(interval.getText1());
    entity.setLith2(interval.getLith2());
    entity.setText2(interval.getText2());
    entity.setComp1(interval.getComp1());
    entity.setComp2(interval.getComp2());
    entity.setComp3(interval.getComp3());
    entity.setComp4(interval.getComp4());
    entity.setComp5(interval.getComp5());
    entity.setComp6(interval.getComp6());
    entity.setDescription(interval.getDescription());
    entity.setAge(interval.getAge());
    entity.setAbsoluteAgeTop(interval.getAbsoluteAgeTop());
    entity.setAbsoluteAgeBot(interval.getAbsoluteAgeBot());
    entity.setWeight(interval.getWeight());
    entity.setRockLith(interval.getRockLith());
    entity.setRockMin(interval.getRockMin());
    entity.setWeathMeta(interval.getWeathMeta());
    entity.setRemark(interval.getRemark());
    entity.setMunsellCode(interval.getMunsellCode());
    entity.setMunsell(interval.getMunsell());
    entity.setExhaustCode(interval.getExhaustCode());
    entity.setPhotoLink(interval.getPhotoLink());
    entity.setLake(interval.getLake());
    entity.setUnitNumber(interval.getUnitNumber());
    entity.setIntComments(interval.getIntComments());
    entity.setDhDevice(interval.getDhDevice());
    entity.setCmcdTop(interval.getCmcdTop());
    entity.setMmcdTop(interval.getMmcdTop());
    entity.setCmcdBot(interval.getCmcdBot());
    entity.setMmcdBot(interval.getMmcdBot());
    entity.setPublish(interval.getPublish());
    entity.setPreviousState(interval.getPreviousState());
    entity.setIgsn(interval.getIgsn());
    curatorsIntervalRepository.saveAndFlush(entity);
  }

  public SampleIntervalAcceptanceView accept(SampleIntervalAcceptanceView acceptance) {
    Set<String> tempImlgsToDelete = new HashSet<>();
    for (IntervalIdView intervalId : acceptance.getIntervals()) {
      tempImlgsToDelete.add(intervalId.getImlgs());
      CuratorsSampleTsqpEntity sample = resolveSample(intervalId.getImlgs());
      IntervalPk pk = new IntervalPk();
      pk.setImlgs(intervalId.getImlgs());
      pk.setInterval(intervalId.getInterval());
      TempQcIntervalEntity interval = tempQcIntervalRepository.findById(pk)
          .orElseThrow(() -> new ApiException(
              HttpStatus.BAD_REQUEST,
              ApiError.builder().error("Unable to find interval: " + intervalId.getImlgs() + " + " + intervalId.getInterval()).build()));
      saveCopyInterval(interval, sample);
      tempQcIntervalRepository.delete(interval);
      tempQcIntervalRepository.flush();
    }
    for (String imlgs : tempImlgsToDelete) {
      if (tempQcIntervalRepository.countByImlgs(imlgs) == 0) {
        tempQcSampleRepository.deleteById(imlgs);
      }
    }
    return acceptance;
  }


  @Override
  protected List<Specification<TempQcIntervalEntity>> getSpecs(CombinedIntervalSampleSearchParameters searchParameters) {
    return SampleIntervalUtils.getBaseSpecs(searchParameters);
  }

  @Override
  protected CombinedSampleIntervalView toView(TempQcIntervalEntity entity) {
    return SampleIntervalUtils.toViewBase(entity);
  }

  @Override
  protected TempQcIntervalEntity newEntityWithDefaultValues(CombinedSampleIntervalView view) {
    throw new UnsupportedOperationException("Update of CombinedSampleIntervalView is not supported");
  }

  @Override
  protected void updateEntity(TempQcIntervalEntity entity, CombinedSampleIntervalView view) {
    throw new UnsupportedOperationException("Update of CombinedSampleIntervalView is not supported");
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return SampleIntervalUtils.SORT_MAPPING;
  }

  @Override
  protected TempQcIntervalRepository getRepository() {
    return tempQcIntervalRepository;
  }

}
