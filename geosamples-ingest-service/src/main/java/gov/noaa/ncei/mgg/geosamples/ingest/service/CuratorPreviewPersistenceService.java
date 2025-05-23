package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLegRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRow;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SampleRowHolder;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.SpreadsheetValidationException;
import gov.noaa.ncei.mgg.geosamples.ingest.service.model.validation.CustomConstraintViolation;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CuratorPreviewPersistenceService {

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final ServiceProperties serviceProperties;
  private final SampleDataUtils sampleDataUtils;


  @Autowired
  public CuratorPreviewPersistenceService(
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      CuratorsIntervalRepository curatorsIntervalRepository, ServiceProperties serviceProperties,
      SampleDataUtils sampleDataUtils,
      CuratorsLegRepository curatorsLegRepository) {
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.serviceProperties = serviceProperties;
    this.sampleDataUtils = sampleDataUtils;
  }





  private Optional<CuratorsSampleTsqpEntity> resolveSample(CuratorsSampleTsqpEntity potSample) {
    return curatorsSampleTsqpRepository.findOne(SearchUtils.findExistingSample(potSample));
  }

  private Optional<CuratorsIntervalEntity> resolveInterval(CuratorsIntervalEntity potInterval) {
    return curatorsIntervalRepository.findBySampleAndInterval(potInterval.getSample(), potInterval.getInterval());
  }

  private CuratorsSampleTsqpEntity createSample(
      CuratorsCruiseFacilityEntity facility,
      CuratorsCruisePlatformEntity platform,
      CuratorsDeviceEntity device,
      CuratorsCruiseEntity cruise,
      String sampleId,
      SampleRow row) {

    CuratorsSampleTsqpEntity sample = new CuratorsSampleTsqpEntity();

    sample.setCruise(cruise);
    sample.setSample(sampleId);
    sample.setDevice(device);

    sample.setCruisePlatform(platform);
    sample.setCruiseFacility(facility);

//    sample.setObjectId(getObjectId());
//    sample.setImlgs(getImlgs(sample.getObjectId()));

    // TODO what is this - ask Kelly?
//      sample.setShipCode();
    sample.setBeginDate(row.getDateCollected());
    sample.setEndDate(row.getEndDate());

    PositionDim beginningLat = SampleDataUtils.getPositionDim(row.getBeginningLatitude(), true);
    sample.setLat(beginningLat.getValue());

    PositionDim endingLat = SampleDataUtils.getPositionDim(row.getEndingLatitude(), true);
    sample.setEndLat(endingLat.getValue());

    PositionDim beginningLon = SampleDataUtils.getPositionDim(row.getBeginningLongitude(), false);
    sample.setLon(beginningLon.getValue());

    PositionDim endingLon = SampleDataUtils.getPositionDim(row.getEndingLongitude(), false);
    sample.setEndLon(endingLon.getValue());

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
    sample.setLastUpdate(Instant.now());
    if(StringUtils.isNotBlank(row.getAlternateCruise())){
      sample.setLeg(cruise.getLegs().stream().filter(l -> l.getLegName().equals(row.getAlternateCruise())).findFirst()
          .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("leg not found: " + row.getAlternateCruise()).build())));
    }

    sample.setPublish(false);

    sample.setShape(sampleDataUtils.getShape(row.getBeginningLongitude(), row.getBeginningLatitude()));

    sample.setShowSampl(serviceProperties.getShowSampleBaseUrl() + sample.getImlgs());
    sample.setLake(row.getSampleLake());
    sample.setSampleComments(row.getSampleComments());
    return sample;
  }

  private CuratorsIntervalEntity createInterval(
      CuratorsSampleTsqpEntity sample,
      SampleRow row) {
    CuratorsIntervalEntity interval = new CuratorsIntervalEntity();
    interval.setSample(sample);

    interval.setInterval(row.getIntervalNumber());

    CmConverter depthTop = new CmConverter(row.getDepthToTopOfInterval());
    interval.setDepthTop(depthTop.getCm());
    interval.setDepthTopMm(depthTop.getMm());

    CmConverter depthBottom = new CmConverter(row.getDepthToBottomOfInterval());
    interval.setDepthBot(depthBottom.getCm());
    interval.setDepthBotMm(depthBottom.getMm());

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
    interval.setAges(
        row.getGeologicAgeCode().stream().sorted()
            .map(sampleDataUtils::getAge)
            .collect(Collectors.toList())
    );

    interval.setWeight(row.getBulkWeight());
    interval.setRockLith(sampleDataUtils.getRockLithology(row.getSampleLithologyCode()));
    interval.setRockMin(sampleDataUtils.getMineralogy(row.getSampleMineralogyCode()));
    interval.setWeathMeta(sampleDataUtils.getWeathering(row.getSampleWeatheringOrMetamorphismCode()));
    interval.setRemark(sampleDataUtils.getGlassRemark(row.getGlassRemarksCode()));

    //TODO Munsell colors can be duplicated, should the spreadsheet use the code?

    interval.setMunsellCode(row.getMunsellColor());
    CuratorsMunsellEntity munsell = sampleDataUtils.getMunsell(row.getMunsellColor());
    if (munsell != null) {
      interval.setMunsell(munsell.getMunsell());
    }

    interval.setExhaustCode(row.getSampleNotAvailable());

    interval.setIntComments(row.getComments());
    interval.setIgsn(row.getIntervalIgsn());

    interval.setPublish(false);
    return interval;
  }

  public void save(SampleRowHolder sampleRowHolder) {

    Map<String, CuratorsSampleTsqpEntity> touchedSamples = new HashMap<>();
    Map<Long, CuratorsIntervalEntity> touchedIntervals = new HashMap<>();


    List<SampleRow> samples = sampleRowHolder.getRows();

    Set<ConstraintViolation<?>> violations = new HashSet<>();
    for (int index = 0; index < samples.size(); index++) {

      SampleRow row = samples.get(index);

      CuratorsFacilityEntity facility = sampleDataUtils.getFacility(row.getFacilityCode());
      PlatformMasterEntity platform = sampleDataUtils.getPlatform(row.getShipName());
      CuratorsDeviceEntity device = sampleDataUtils.getDevice(row.getSamplingDeviceCode());
      CuratorsCruiseEntity cruise = sampleDataUtils.getCruise(row.getCruiseId(), platform, facility);
//      Long cruiseId = row.getCruiseId();
      String sampleId = row.getSampleId();

      CuratorsSampleTsqpEntity potSample = createSample(
          cruise.getFacilityMappings().stream().filter(e -> e.getFacility().getFacilityCode().equals(row.getFacilityCode())).findFirst().orElse(null),
          cruise.getPlatformMappings().stream().filter(e -> e.getPlatform().getPlatformNormalized().equals(row.getShipName())).findFirst().orElse(null),
          device,
          cruise,
          sampleId,
          row);

      Optional<CuratorsSampleTsqpEntity> maybeSample = resolveSample(potSample);

      CuratorsSampleTsqpEntity sample;
      if (!maybeSample.isPresent()) {
        if (potSample.getIgsn() != null) {
          if (curatorsSampleTsqpRepository.existsByIgsn(potSample.getIgsn())) {
            violations.add(
                new CustomConstraintViolation("A sample with this IGSN already exists", String.format("rows[%s].igsn", index))
            );
            continue;
          }
        }
//        potSample.setObjectId(sampleDataUtils.getObjectId());
        potSample.setImlgs(sampleDataUtils.getImlgs(sampleDataUtils.getObjectId()));
        potSample.setShowSampl(serviceProperties.getShowSampleBaseUrl() + potSample.getImlgs());
        sample = curatorsSampleTsqpRepository.saveAndFlush(potSample);
        touchedSamples.put(sample.getImlgs(), sample);
      } else {
        CuratorsSampleTsqpEntity existing = maybeSample.get();
//        potSample.setObjectId(existing.getObjectId());

        if (existing.getIgsn() != null || row.getIgsn() != null) {

          if (touchedSamples.get(existing.getImlgs()) != null) {
            if ((existing.getIgsn() != null && row.getIgsn() == null) || (existing.getIgsn() == null && row.getIgsn() != null) || !existing.getIgsn().equals(row.getIgsn())) {
              violations.add(
                  new CustomConstraintViolation("Conflicting IGSN value specified for sample", String.format("rows[%s].igsn", index))
              );
              continue;
            }
          }

          if ((existing.getIgsn() != null && row.getIgsn() == null)) {
            violations.add(
                new CustomConstraintViolation("Existing sample has an IGSN defined", String.format("rows[%s].igsn", index))
            );
            continue;
          }
          if (existing.getIgsn() == null && row.getIgsn() != null) {
            violations.add(
                new CustomConstraintViolation("Existing sample does not have an IGSN defined", String.format("rows[%s].igsn", index))
            );
            continue;
          }

          if (existing.getIgsn() != null && row.getIgsn() != null && !existing.getIgsn().equals(row.getIgsn())) {
            violations.add(
                new CustomConstraintViolation("IGSN does not match existing sample IGSN", String.format("rows[%s].igsn", index))
            );
            continue;
          }
        }


        potSample.setImlgs(existing.getImlgs());
        potSample.setShowSampl(existing.getShowSampl());
        if (existing.isPublish() || EntitySignificantFields.equals(existing, potSample)) {
          sample = existing;
          touchedSamples.put(sample.getImlgs(), sample);
        } else {
          EntitySignificantFields.copy(potSample, existing);
          sample = curatorsSampleTsqpRepository.saveAndFlush(existing);
          touchedSamples.put(sample.getImlgs(), sample);
        }
      }

      CuratorsIntervalEntity potInterval = createInterval(sample, row);
      Optional<CuratorsIntervalEntity> maybeInterval = resolveInterval(potInterval);

      if (!maybeInterval.isPresent()) {
        CuratorsIntervalEntity interval = curatorsIntervalRepository.saveAndFlush(potInterval);
        touchedIntervals.put(interval.getId(), interval);
      } else {
        CuratorsIntervalEntity existing = maybeInterval.get();

        boolean hasViolation = false;
        if (existing.isPublish()) {
          violations.add(
              new CustomConstraintViolation("Cannot update published interval", String.format("rows[%s].intervalNumber", index))
          );
          hasViolation = true;
        }
        if (touchedIntervals.get(existing.getId()) != null) {
          violations.add(
              new CustomConstraintViolation("Duplicate interval number detected", String.format("rows[%s].intervalNumber", index))
          );
          hasViolation = true;
        }
        if (hasViolation) {
          continue;
        }
        
        potInterval.setDhCoreId(existing.getDhCoreId());
        potInterval.setDhCoreLength(existing.getDhCoreLength());
        potInterval.setDhCoreLengthMm(existing.getDhCoreLengthMm());
        potInterval.setDhCoreInterval(existing.getDhCoreInterval());
        potInterval.setdTopInDhCore(existing.getdTopInDhCore());
        potInterval.setdTopMmInDhCore(existing.getdTopMmInDhCore());
        potInterval.setdBotInDhCore(existing.getdBotInDhCore());
        potInterval.setdBotMmInDhCore(existing.getdBotMmInDhCore());
        potInterval.setDhDevice(existing.getDhDevice());
        potInterval.setCmcdTop(existing.getCmcdTop());
        potInterval.setMmcdTop(existing.getMmcdTop());
        potInterval.setCmcdBot(existing.getCmcdBot());
        potInterval.setMmcdBot(existing.getMmcdBot());
        potInterval.setAbsoluteAgeTop(existing.getAbsoluteAgeTop());
        potInterval.setAbsoluteAgeBot(existing.getAbsoluteAgeBot());
        
        if (!existing.isPublish() && !EntitySignificantFields.equals(existing, potInterval)) {
          EntitySignificantFields.copy(potInterval, existing);
          existing = curatorsIntervalRepository.saveAndFlush(existing);
        }
        touchedIntervals.put(existing.getId(), existing);
      }


    }

    if (!violations.isEmpty()) {
      throw new SpreadsheetValidationException(sampleRowHolder, violations);
    }


  }

}
