package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruisePlatformRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLegRepository;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.sql.DataSource;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SampleDataUtils {


  public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);

  private static final String sequenceName = "CURATORS_SEQ";

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final CuratorsFacilityRepository curatorsFacilityRepository;
  private final PlatformMasterRepository platformMasterRepository;
  private final CuratorsDeviceRepository curatorsDeviceRepository;
  private final CuratorsCruiseRepository curatorsCruiseRepository;
  private final CuratorsCruisePlatformRepository curatorsCruisePlatformRepository;
  private final CuratorsLegRepository curatorsLegRepository;
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

  public SampleDataUtils(
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      CuratorsFacilityRepository curatorsFacilityRepository,
      PlatformMasterRepository platformMasterRepository,
      CuratorsDeviceRepository curatorsDeviceRepository,
      CuratorsCruiseRepository curatorsCruiseRepository,
      CuratorsCruisePlatformRepository curatorsCruisePlatformRepository,
      CuratorsLegRepository curatorsLegRepository,
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
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.curatorsFacilityRepository = curatorsFacilityRepository;
    this.platformMasterRepository = platformMasterRepository;
    this.curatorsDeviceRepository = curatorsDeviceRepository;
    this.curatorsCruiseRepository = curatorsCruiseRepository;
    this.curatorsCruisePlatformRepository = curatorsCruisePlatformRepository;
    this.curatorsLegRepository = curatorsLegRepository;
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

  public CuratorsFacilityEntity getFacility(String facility) {
    if (facility == null) {
      return null;
    }
    return curatorsFacilityRepository
        .findByFacilityCode(facility)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find facility: " + facility).build()));
  }

  public PlatformMasterEntity getPlatform(String platformName) {
    if (platformName == null) {
      return null;
    }
    return platformMasterRepository
        .findByPlatformNormalized(platformName)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find platform: " + platformName).build()));
  }

  public CuratorsSampleTsqpEntity getSample(String imlgs) {
    if (imlgs == null) {
      return null;
    }
    return curatorsSampleTsqpRepository
        .findByImlgs(imlgs)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find imlgs: " + imlgs).build()));
  }

  public CuratorsDeviceEntity getDevice(String code) {
    if (code == null) {
      return null;
    }
    return curatorsDeviceRepository
        .findByDeviceCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find device code: " + code).build()));
  }

  public CuratorsCruiseEntity getCruise(String cruiseName, PlatformMasterEntity platform, CuratorsFacilityEntity facilityName) {
    //TODO update template to accept optional year
    List<CuratorsCruiseEntity> cruises = curatorsCruiseRepository.findByCruiseNameAndPlatformAndFacility(cruiseName,platform,facilityName);
    if(cruises.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find cruise: " + cruiseName).build());
    }
    if (cruises.size() > 1) {
      throw new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Multiple cruises found with name: " + cruiseName).build());
    }
    return cruises.get(0);
  }

  public CuratorsCruiseEntity getCruise(String cruiseName, Short year) {
    if (cruiseName == null || year == null){
      return null;
    }
    return curatorsCruiseRepository
        .findByCruiseNameAndYear(cruiseName, year)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find cruise: " + cruiseName).build()));
  }


  public CuratorsCruisePlatformEntity getCruisePlatform(String cruiseName, Short year, String platform){
    CuratorsCruiseEntity cruiseEntity = getCruise(cruiseName, year);
    PlatformMasterEntity platformEntity = getPlatform(platform);
    if (cruiseEntity == null || platformEntity == null){
      return null;
    }
    return curatorsCruisePlatformRepository
        .findByCruiseAndPlatform(cruiseEntity, platformEntity)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find cruise_platform: " + cruiseName).build()));
  }

  public CuratorsLegEntity getLeg(String legName, CuratorsCruiseEntity cruise) {
    return curatorsLegRepository
        .findByLegNameAndCruise(legName,cruise)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find leg: " + legName).build()));
  }

  public CuratorsLegEntity getLeg(Long id) {
    return curatorsLegRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find leg: " + id).build()));
  }

  public CuratorsStorageMethEntity getStorageMethod(String code) {
    if (code == null) {
      return null;
    }
    return curatorsStorageMethRepository
        .findByStorageMethCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find storage method code: " + code).build()));
  }

  public CuratorsProvinceEntity getProvince(String code) {
    if (code == null) {
      return null;
    }
    return curatorsProvinceRepository
        .findByProvinceCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find province code: " + code).build()));
  }

  public CuratorsLithologyEntity getLithology(String code) {
    if (code == null) {
      return null;
    }
    return curatorsLithologyRepository
        .findByLithologyCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find lithology code: " + code).build()));
  }

  public CuratorsTextureEntity getTexture(String code) {
    if (code == null) {
      return null;
    }
    return curatorsTextureRepository
        .findByTextureCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find texture code: " + code).build()));
  }

  public CuratorsAgeEntity getAge(String code) {
    if (code == null) {
      return null;
    }
    return curatorsAgeRepository
        .findByAgeCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find age code: " + code).build()));
  }

  public CuratorsWeathMetaEntity getWeathering(String code) {
    if (code == null) {
      return null;
    }
    return curatorsWeathMetaRepository
        .findByWeathMetaCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find weathering code: " + code).build()));
  }

  public CuratorsRemarkEntity getGlassRemark(String code) {
    if (code == null) {
      return null;
    }
    return curatorsRemarkRepository
        .findByRemarkCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find remark code: " + code).build()));
  }

  public CuratorsMunsellEntity getMunsell(String munsellCode) {
    if (munsellCode == null) {
      return null;
    }
    return curatorsMunsellRepository
        .findById(munsellCode)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find munsell: " + munsellCode).build()));
  }

  public CuratorsRockMinEntity getMineralogy(String code) {
    if (code == null) {
      return null;
    }
    return curatorsRockMinRepository
        .findByRockMinCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find rock mineral code: " + code).build()));
  }

  public CuratorsRockLithEntity getRockLithology(String code) {
    if (code == null) {
      return null;
    }
    return curatorsRockLithRepository
        .findByRockLithCode(code)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ApiError.builder().error("Unable to find rock lithology code: " + code).build()));
  }


  private String resolveSeq() throws SQLException {
    if (!StringUtils.hasText(schema)) {
      return sequenceName;
    }
    return String.format("%s.%s", schema, sequenceName);
  }

  public long getObjectId() {
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

  public Geometry getShape(Double lon, Double lat) {
    if (lon == null || lat == null) {
      return null;
    }
    return geometryFactory.createPoint(new CoordinateXY(lon, lat));
  }

  public static String getImlgs(long objectId) {
    return String.format("imlgs%07d", objectId);
  }

  public static PositionDim getPositionDim(Double value, boolean lat) {
    if (value == null) {
      return new PositionDim();
    }
    int degrees = (int) Math.floor(value);
    double minutes = (value - degrees) * 60D;
    String dir;
    if (lat) {
      dir = value >= 0D ? "N" : "S";
    } else {
      dir = value >= 0D ? "E" : "W";
    }
    return new PositionDim(value, degrees, minutes, dir);
  }


  public static Integer truncate(Double d) {
    if (d == null) {
      return null;
    }
    return (int) Math.floor(d);
  }

  public static Integer round(Double d) {
    if (d == null) {
      return null;
    }
    return (int) Math.round(d);
  }

}
