package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.config.FormatUtils;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
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

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);

  private static final String sequenceName = "CURATORS_SEQ";

  private final TempQcSampleRepository tempQcSampleRepository;
  private final TempQcIntervalRepository tempQcIntervalRepository;
  private final CuratorsFacilityRepository curatorsFacilityRepository;
  private final PlatformMasterRepository platformMasterRepository;
  private final CuratorsDeviceRepository curatorsDeviceRepository;
  private final CuratorsStorageMethRepository curatorsStorageMethRepository;
  private final CuratorsProvinceRepository curatorsProvinceRepository;
  private final CuratorsLithologyRepository curatorsLithologyRepository;
  private final CuratorsTextureRepository curatorsTextureRepository;
  private final CuratorsAgeRepository curatorsAgeRepository;
  private final CuratorsWeathMetaRepository curatorsWeathMetaRepository;
  private final CuratorsRemarkRepository curatorsRemarkRepository;
  private final CuratorsRockMinRepository curatorsRockMinRepository;
  private final CuratorsRockLithRepository curatorsRockLithRepository;
  private final CuratorsMunsellRepository curatorsMunsellRepository;
  private final GeometryFactory geometryFactory;
  private final ServiceProperties serviceProperties;
  private final DataSource dataSource;
  private final String schema;


  @Autowired
  public CuratorPreviewPersistenceService(TempQcSampleRepository tempQcSampleRepository,
      TempQcIntervalRepository tempQcIntervalRepository,
      CuratorsFacilityRepository curatorsFacilityRepository,
      PlatformMasterRepository platformMasterRepository,
      CuratorsDeviceRepository curatorsDeviceRepository,
      CuratorsStorageMethRepository curatorsStorageMethRepository,
      CuratorsProvinceRepository curatorsProvinceRepository,
      CuratorsLithologyRepository curatorsLithologyRepository,
      CuratorsTextureRepository curatorsTextureRepository,
      CuratorsAgeRepository curatorsAgeRepository,
      CuratorsWeathMetaRepository curatorsWeathMetaRepository,
      CuratorsRemarkRepository curatorsRemarkRepository,
      CuratorsRockMinRepository curatorsRockMinRepository,
      CuratorsRockLithRepository curatorsRockLithRepository,
      CuratorsMunsellRepository curatorsMunsellRepository, GeometryFactory geometryFactory,
      ServiceProperties serviceProperties,
      DataSource dataSource,
      @Value("${spring.jpa.properties.hibernate.default_schema}") String schema) {
    this.tempQcSampleRepository = tempQcSampleRepository;
    this.tempQcIntervalRepository = tempQcIntervalRepository;
    this.curatorsFacilityRepository = curatorsFacilityRepository;
    this.platformMasterRepository = platformMasterRepository;
    this.curatorsDeviceRepository = curatorsDeviceRepository;
    this.curatorsStorageMethRepository = curatorsStorageMethRepository;
    this.curatorsProvinceRepository = curatorsProvinceRepository;
    this.curatorsLithologyRepository = curatorsLithologyRepository;
    this.curatorsTextureRepository = curatorsTextureRepository;
    this.curatorsAgeRepository = curatorsAgeRepository;
    this.curatorsWeathMetaRepository = curatorsWeathMetaRepository;
    this.curatorsRemarkRepository = curatorsRemarkRepository;
    this.curatorsRockMinRepository = curatorsRockMinRepository;
    this.curatorsRockLithRepository = curatorsRockLithRepository;
    this.curatorsMunsellRepository = curatorsMunsellRepository;
    this.geometryFactory = geometryFactory;
    this.serviceProperties = serviceProperties;
    this.dataSource = dataSource;
    this.schema = schema;
  }

  private CuratorsFacilityEntity getFacility(String facility) {
    if(facility == null) {
      return null;
    }
    return curatorsFacilityRepository
        .findById(facility)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find facility: " + facility).build()));
  }

  private PlatformMasterEntity getPlatform(String platformName) {
    if(platformName == null) {
      return null;
    }
    return platformMasterRepository
        .findById(platformName)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find platform: " + platformName).build()));
  }

  private CuratorsDeviceEntity getDevice(String code) {
    if(code == null) {
      return null;
    }
    return curatorsDeviceRepository
        .findByDeviceCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find device code: " + code).build()));
  }

  private CuratorsStorageMethEntity getStorageMethod(String code) {
    if(code == null) {
      return null;
    }
    return curatorsStorageMethRepository
        .findByStorageMethCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find storage method code: " + code).build()));
  }

  private CuratorsProvinceEntity getProvince(String code) {
    if(code == null) {
      return null;
    }
    return curatorsProvinceRepository
        .findByProvinceCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find province code: " + code).build()));
  }

  private CuratorsLithologyEntity getLithology(String code) {
    if(code == null) {
      return null;
    }
    return curatorsLithologyRepository
        .findByLithologyCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find lithology code: " + code).build()));
  }

  private CuratorsTextureEntity getTexture(String code) {
    if(code == null) {
      return null;
    }
    return curatorsTextureRepository
        .findByTextureCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find texture code: " + code).build()));
  }

  private CuratorsAgeEntity getAge(String code) {
    if(code == null) {
      return null;
    }
    return curatorsAgeRepository
        .findByAgeCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find age code: " + code).build()));
  }

  private CuratorsWeathMetaEntity getWeathering(String code) {
    if(code == null) {
      return null;
    }
    return curatorsWeathMetaRepository
        .findByWeathMetaCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find weathering code: " + code).build()));
  }

  private CuratorsRemarkEntity getGlassRemark(String code) {
    if(code == null) {
      return null;
    }
    return curatorsRemarkRepository
        .findByRemarkCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find remark code: " + code).build()));
  }

  private CuratorsMunsellEntity getMunsell(String munsell) {
    if(munsell == null) {
      return null;
    }
    return curatorsMunsellRepository
        .findByMunsell(munsell)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find munsell: " + munsell).build()));
  }

  private CuratorsRockMinEntity getMineralogy(String code) {
    if(code == null) {
      return null;
    }
    return curatorsRockMinRepository
        .findByRockMinCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find rock mineral code: " + code).build()));
  }

  private CuratorsRockLithEntity getRockLithology(String code) {
    if(code == null) {
      return null;
    }
    return curatorsRockLithRepository
        .findByRockLithCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find rock lithology code: " + code).build()));
  }

//  private static String formatDate(String value) {
//    if(value == null) {
//      return null;
//    }
//    return value.format(DTF);
//  }

  private static PositionDim getPositionDim(Double value, boolean lat){
    if(value == null) {
      return new PositionDim();
    }
    int degrees = (int) Math.floor(value);
    double minutes = (value - degrees) * 60D;
    String dir;
    if(lat) {
      dir = value >= 0D ? "N" : "S";
    } else {
      dir = value >= 0D ? "E" : "W";
    }
    return new PositionDim(value, degrees, minutes, dir);
  }

  private static class PositionDim {
    private final Double value;
    private final Integer degrees;
    private final String minutes;
    private final String direction;

    public PositionDim() {
      this(null, null, null, null);
    }

    public PositionDim(Double value, Integer degrees, Double minutes, String direction) {
      this.value = value;
      this.degrees = degrees;
      this.minutes = minutes == null ? null : FormatUtils.doubleToString2LeadingZeros2Decimal(minutes);
      this.direction = direction;
    }

    public Double getValue() {
      return value;
    }

    public Integer getDegrees() {
      return degrees;
    }

    public String getMinutes() {
      return minutes;
    }

    public String getDirection() {
      return direction;
    }
  }

  private static class CmConverter {
    private final Double cm;

    private CmConverter(Double cm) {
      this.cm = cm;
    }

    public Integer getCm() {
      return truncate(cm);
    }

    public Integer getMm() {
      if(cm == null) {
        return null;
      }
      return round((cm - getCm()) * 10D);
    }
  }

  private static Integer truncate(Double d) {
    if(d == null) {
      return null;
    }
    return (int) Math.floor(d);
  }

  private static Integer round(Double d) {
    if(d == null) {
      return null;
    }
    return (int) Math.round(d);
  }

  private Geometry getShape(Double lon, Double lat) {
    if(lon == null || lat == null) {
      return null;
    }
    return geometryFactory.createPoint(new CoordinateXY(lon, lat));
  }

  private String resolveSeq() throws SQLException {
    if(!StringUtils.hasText(schema)) {
      return sequenceName;
    }
    return String.format("%s.%s", schema, sequenceName);
  }

  private long getObjectId() {
    try (Connection connection = dataSource.getConnection()) {
      Dialect dialect = new StandardDialectResolver()
          .resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
      try (
          PreparedStatement preparedStatement = connection.prepareStatement(dialect.getSequenceNextValString(resolveSeq()));
          ResultSet resultSet = preparedStatement.executeQuery();
      ) {
        resultSet.next();
        return resultSet.getLong(1);
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Unable to get connection", e);
    }
  }

  private String getImlgs(long objectId) {
    return String.format("imlgs%07d", objectId);
  }

  public void save(List<SampleRow> samples) {

    String lastUpdated = LocalDate.now(ZoneId.of("UTC")).format(DTF);

    for (SampleRow row : samples) {

      TempQcSampleEntity sample = new TempQcSampleEntity();

      sample.setObjectId(getObjectId());
      sample.setImlgs(getImlgs(sample.getObjectId()));

      sample.setCruise(row.getCruiseId());
      sample.setSample(row.getSampleId());
      sample.setFacility(getFacility(row.getFacilityCode()));
      sample.setPlatform(getPlatform(row.getShipName()));
      sample.setDevice(getDevice(row.getSamplingDeviceCode()));
      // TODO what is this - ask Kelly?
//      sample.setShipCode();
      sample.setBeginDate(row.getDateCollected());
      sample.setEndDate(row.getEndDate());

      PositionDim beginningLat = getPositionDim(row.getBeginningLatitude(), true);
      sample.setLat(beginningLat.getValue());
      sample.setLatDeg(beginningLat.getDegrees());
      sample.setLatMin(beginningLat.getMinutes());
      sample.setNs(beginningLat.getDirection());

      PositionDim endingLat = getPositionDim(row.getEndingLatitude(), true);
      sample.setEndLon(endingLat.getValue());
      sample.setEndLatDeg(endingLat.getDegrees());
      sample.setEndLatMin(endingLat.getMinutes());
      sample.setEndNs(endingLat.getDirection());

      PositionDim beginningLon = getPositionDim(row.getBeginningLongitude(), false);
      sample.setLon(beginningLon.getValue());
      sample.setLonDeg(beginningLon.getDegrees());
      sample.setLonMin(beginningLon.getMinutes());
      sample.setEw(beginningLon.getDirection());

      PositionDim endingLon = getPositionDim(row.getEndingLongitude(), false);
      sample.setEndLon(endingLon.getValue());
      sample.setEndLonDeg(endingLon.getDegrees());
      sample.setEndLonMin(endingLon.getMinutes());
      sample.setEndEw(endingLon.getDirection());

      sample.setLatLonOrig("D");

      sample.setWaterDepth(round(row.getBeginningWaterDepth()));
      sample.setEndWaterDepth(round(row.getEndingWaterDepth()));

      sample.setStorageMeth(getStorageMethod(row.getStorageMethodCode()));
      sample.setCoredLength(round(row.getCoreLength()));

      CmConverter coredLength = new CmConverter(row.getCoreLength());
      sample.setCoredLength(coredLength.getCm());
      sample.setCoredLengthMm(coredLength.getMm());

      CmConverter coredDiam = new CmConverter(row.getCoreDiameter());
      sample.setCoredDiam(coredDiam.getCm());
      sample.setCoredDiamMm(coredDiam.getMm());

      sample.setPi(row.getPrincipalInvestigator());
      sample.setProvince(getProvince(row.getPhysiographicProvinceCode()));

      sample.setIgsn(row.getIgsn());

      // TODO add me ? - DOI per curator - pass in form?
//      sample.setOtherLink();
      sample.setLastUpdate(lastUpdated);
      sample.setLeg(row.getAlternateCruise());

      sample.setPublish("Y");


      sample.setShape(getShape(row.getBeginningLongitude(), row.getBeginningLatitude()));

      sample.setShowSampl(serviceProperties.getShowSampleBaseUrl() + "?" + sample.getImlgs());


      sample = tempQcSampleRepository.saveAndFlush(sample);




      TempQcIntervalEntity interval = new TempQcIntervalEntity();
      interval.setParentEntity(sample);
      /*
          parentIsgn = parentEntity.getIsgn();
    imlgs = parentEntity.getImlgs();
    facility = parentEntity.getFacility();
    shipCode = parentEntity.getShipCode();
    platform = parentEntity.getPlatform();
    cruise = parentEntity.getCruise();
    sample = parentEntity.getSample();
    device = parentEntity.getDevice();
       */
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

      interval.setLith1(getLithology(row.getPrimaryLithologicCompositionCode()));
      interval.setText1(getTexture(row.getPrimaryTextureCode()));
      interval.setLith2(getLithology(row.getSecondaryLithologicCompositionCode()));
      interval.setText2(getTexture(row.getSecondaryTextureCode()));


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
        setter.accept(getLithology(row.getOtherComponentCodes().get(i)));
      }

      interval.setDescription(row.getDescription());
      interval.setAge(getAge(row.getGeologicAgeCode()));

      interval.setWeight(row.getBulkWeight());
      interval.setRockLith(getRockLithology(row.getSampleLithologyCode()));
      interval.setRockMin(getMineralogy(row.getSampleMineralogyCode()));
      interval.setWeathMeta(getWeathering(row.getSampleWeatheringOrMetamorphismCode()));
      interval.setRemark(getGlassRemark(row.getGlassRemarksCode()));

      //TODO Munsell colors can be duplicated, sholuld the spreadsheet use the code?

//      interval.setMunsell(row.getMunsellColor());
//      CuratorsMunsellEntity munsell = getMunsell(row.getMunsellColor());
//      if(munsell != null) {
//        interval.setMunsellCode(munsell.getMunsellCode());
//      }

      interval.setExhaustCode(row.getSampleNotAvailable());

      interval.setIntComments(row.getComments());

      interval.setPublish("Y");

      tempQcIntervalRepository.saveAndFlush(interval);


    }




  }

}
