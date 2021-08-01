package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.TempQcSampleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsMunsellRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsProvinceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsStorageMethRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TempQcSampleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class CuratorPreviewPersistenceService {

  private final TempQcSampleRepository tempQcSampleRepository;
  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final TempQcIntervalRepository tempQcIntervalRepository;
  private final ServiceProperties serviceProperties;
  private final SampleDataUtils sampleDataUtils;


  @Autowired
  public CuratorPreviewPersistenceService(TempQcSampleRepository tempQcSampleRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      TempQcIntervalRepository tempQcIntervalRepository,
      ServiceProperties serviceProperties,
      SampleDataUtils sampleDataUtils) {
    this.tempQcSampleRepository = tempQcSampleRepository;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.tempQcIntervalRepository = tempQcIntervalRepository;
    this.serviceProperties = serviceProperties;
    this.sampleDataUtils = sampleDataUtils;
  }

  private TempQcSampleEntity saveCopy(CuratorsSampleTsqpEntity sample) {
    TempQcSampleEntity entity = new TempQcSampleEntity();
    entity.setCruise(sample.getCruise());
    entity.setSample(entity.getSample());
    entity.setFacility(entity.getFacility());
    entity.setPlatform(sample.getPlatform());
    entity.setDevice(sample.getDevice());
    entity.setShipCode(entity.getShipCode());
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
    entity.setShowSampl(sample.getShowSampl());
    entity.setImlgs(sample.getImlgs());
    return tempQcSampleRepository.saveAndFlush(entity);
  }

  private Optional<TempQcSampleEntity> resolveSample(TempQcSampleEntity potSample) {
    Optional<CuratorsSampleTsqpEntity> maybeSample = curatorsSampleTsqpRepository.findOne(SearchUtils.findExistingSample(potSample));
    return maybeSample.map(curatorsSampleTsqpEntity -> Optional.of(saveCopy(curatorsSampleTsqpEntity)))
        .orElseGet(() -> tempQcSampleRepository.findOne(SearchUtils.findExistingSample(potSample)));
  }

  private TempQcSampleEntity createSample(
      CuratorsFacilityEntity facility,
      PlatformMasterEntity platform,
      CuratorsDeviceEntity device,
      String cruiseId,
      String sampleId,
      SampleRow row,
      String lastUpdated) {

    TempQcSampleEntity sample = new TempQcSampleEntity();

    sample.setCruise(cruiseId);
    sample.setSample(sampleId);
    sample.setFacility(facility);
    sample.setPlatform(platform);
    sample.setDevice(device);

//    sample.setObjectId(getObjectId());
//    sample.setImlgs(getImlgs(sample.getObjectId()));

    // TODO what is this - ask Kelly?
//      sample.setShipCode();
    sample.setBeginDate(row.getDateCollected());
    sample.setEndDate(row.getEndDate());

    PositionDim beginningLat = SampleDataUtils.getPositionDim(row.getBeginningLatitude(), true);
    sample.setLat(beginningLat.getValue());
    sample.setLatDeg(beginningLat.getDegrees());
    sample.setLatMin(beginningLat.getMinutes());
    sample.setNs(beginningLat.getDirection());

    PositionDim endingLat = SampleDataUtils.getPositionDim(row.getEndingLatitude(), true);
    sample.setEndLon(endingLat.getValue());
    sample.setEndLatDeg(endingLat.getDegrees());
    sample.setEndLatMin(endingLat.getMinutes());
    sample.setEndNs(endingLat.getDirection());

    PositionDim beginningLon = SampleDataUtils.getPositionDim(row.getBeginningLongitude(), false);
    sample.setLon(beginningLon.getValue());
    sample.setLonDeg(beginningLon.getDegrees());
    sample.setLonMin(beginningLon.getMinutes());
    sample.setEw(beginningLon.getDirection());

    PositionDim endingLon = SampleDataUtils.getPositionDim(row.getEndingLongitude(), false);
    sample.setEndLon(endingLon.getValue());
    sample.setEndLonDeg(endingLon.getDegrees());
    sample.setEndLonMin(endingLon.getMinutes());
    sample.setEndEw(endingLon.getDirection());

    sample.setLatLonOrig("D");

    sample.setWaterDepth(SampleDataUtils.round(row.getBeginningWaterDepth()));
    sample.setEndWaterDepth(SampleDataUtils.round(row.getEndingWaterDepth()));

    sample.setStorageMeth(sampleDataUtils.getStorageMethod(row.getStorageMethodCode()));

    CmConverter coredLength = new CmConverter(row.getCoreLength());
    sample.setCoredLength(coredLength.getCm());
    sample.setCoredLengthMm(coredLength.getMm());

    CmConverter coredDiam = new CmConverter(row.getCoreDiameter());
    sample.setCoredDiam(coredDiam.getCm());
    sample.setCoredDiamMm(coredDiam.getMm());

    sample.setPi(row.getPrincipalInvestigator());
    sample.setProvince(sampleDataUtils.getProvince(row.getPhysiographicProvinceCode()));

    sample.setIgsn(row.getIgsn());

    // TODO add me ? - DOI per curator - pass in form?
//      sample.setOtherLink();
    sample.setLastUpdate(lastUpdated);
    sample.setLeg(row.getAlternateCruise());

    sample.setPublish("Y");


    sample.setShape(sampleDataUtils.getShape(row.getBeginningLongitude(), row.getBeginningLatitude()));

    sample.setShowSampl(serviceProperties.getShowSampleBaseUrl() + "?" + sample.getImlgs());
    return sample;
  }

  public void save(List<SampleRow> samples) {

    String lastUpdated = LocalDate.now(ZoneId.of("UTC")).format(SampleDataUtils.DTF);

    for (SampleRow row : samples) {

      CuratorsFacilityEntity facility = sampleDataUtils.getFacility(row.getFacilityCode());
      PlatformMasterEntity platform = sampleDataUtils.getPlatform(row.getShipName());
      CuratorsDeviceEntity device = sampleDataUtils.getDevice(row.getSamplingDeviceCode());
      String cruiseId = row.getCruiseId();
      String sampleId = row.getSampleId();

      TempQcSampleEntity potSample = createSample(facility, platform, device, cruiseId, sampleId, row, lastUpdated);

      TempQcSampleEntity sample = resolveSample(potSample)
          .orElseGet(() -> {
            potSample.setObjectId(sampleDataUtils.getObjectId());
            potSample.setImlgs(sampleDataUtils.getTempImlgs(potSample.getObjectId()));
            potSample.setShowSampl(serviceProperties.getShowSampleBaseUrl() + "?" + potSample.getImlgs());
            return tempQcSampleRepository.saveAndFlush(potSample);
          });


      TempQcIntervalEntity interval = new TempQcIntervalEntity();
      interval.setParentEntity(sample);

      interval.setInterval(row.getIntervalNumber());

      CmConverter depthTop = new CmConverter(row.getDepthToTopOfInterval());
      interval.setDepthTop(depthTop.getCm());
      interval.setDepthTopMm(depthTop.getMm());

      CmConverter depthBottom = new CmConverter(row.getDepthToBottomOfInterval());
      interval.setDepthBot(depthBottom.getCm());
      interval.setDepthBotMm(depthBottom.getMm());

      CmConverter coreLength = new CmConverter(row.getCoreLength());
      interval.setDhCoreLength(coreLength.getCm());
      interval.setDhCoreLengthMm(coreLength.getMm());

      interval.setLith1(sampleDataUtils.getLithology(row.getPrimaryLithologicCompositionCode()));
      interval.setText1(sampleDataUtils.getTexture(row.getPrimaryTextureCode()));
      interval.setLith2(sampleDataUtils.getLithology(row.getSecondaryLithologicCompositionCode()));
      interval.setText2(sampleDataUtils.getTexture(row.getSecondaryTextureCode()));


      for (int i = 0; i < row.getOtherComponentCodes().size(); i++) {
        Consumer<CuratorsLithologyEntity> setter;
        switch (i) {
          case 0:
            setter = interval::setComp1;
            break;
            case 1:
            setter = interval::setComp2;
            break;
          case 2:
            setter = interval::setComp3;
            break;
          case 3:
            setter = interval::setComp4;
            break;
          case 4:
            setter = interval::setComp5;
            break;
          case 5:
            setter = interval::setComp6;
            break;
          default:
            throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("A maximum of 6 other component codes is supported").build());
        }
        setter.accept(sampleDataUtils.getLithology(row.getOtherComponentCodes().get(i)));
      }

      interval.setDescription(row.getDescription());
      interval.setAge(sampleDataUtils.getAge(row.getGeologicAgeCode()));

      interval.setWeight(row.getBulkWeight());
      interval.setRockLith(sampleDataUtils.getRockLithology(row.getSampleLithologyCode()));
      interval.setRockMin(sampleDataUtils.getMineralogy(row.getSampleMineralogyCode()));
      interval.setWeathMeta(sampleDataUtils.getWeathering(row.getSampleWeatheringOrMetamorphismCode()));
      interval.setRemark(sampleDataUtils.getGlassRemark(row.getGlassRemarksCode()));

      //TODO Munsell colors can be duplicated, should the spreadsheet use the code?

      interval.setMunsellCode(row.getMunsellColor());
      CuratorsMunsellEntity munsell = sampleDataUtils.getMunsell(row.getMunsellColor());
      if(munsell != null) {
        interval.setMunsell(munsell.getMunsell());
      }

      interval.setExhaustCode(row.getSampleNotAvailable());

      interval.setIntComments(row.getComments());

      interval.setPublish("Y");

      tempQcIntervalRepository.saveAndFlush(interval);


    }




  }

}
