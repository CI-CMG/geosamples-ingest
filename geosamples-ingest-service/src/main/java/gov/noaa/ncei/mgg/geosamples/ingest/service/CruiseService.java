package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.CruiseView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.FacilityView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLegRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.PlatformMasterRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CruiseService extends
    SearchServiceBase<CuratorsCruiseEntity, Long, CruiseSearchParameters, CruiseView, CuratorsCruiseRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(CruiseView.class);

  private final CuratorsCruiseRepository curatorsCruiseRepository;
  private final PlatformMasterRepository platformMasterRepository;
  private final CuratorsFacilityRepository curatorsFacilityRepository;

  @Autowired
  public CruiseService(CuratorsCruiseRepository curatorsCruiseRepository,
      PlatformMasterRepository platformMasterRepository,
      CuratorsFacilityRepository curatorsFacilityRepository,
      CuratorsLegRepository curatorsLegRepository) {
    this.curatorsCruiseRepository = curatorsCruiseRepository;
    this.platformMasterRepository = platformMasterRepository;
    this.curatorsFacilityRepository = curatorsFacilityRepository;
  }

  @Override
  protected List<Specification<CuratorsCruiseEntity>> getSpecs(CruiseSearchParameters searchParameters) {
    List<Specification<CuratorsCruiseEntity>> specs = new ArrayList<>();

    List<String> cruiseNameContains = searchParameters.getCruiseNameContains();
    List<String> cruiseNameEquals = searchParameters.getCruiseNameEquals().stream().map(s -> s.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
    List<Long> year = searchParameters.getYear();
    List<String> publish = searchParameters.getPublish().stream().map(p -> p ? "Y" : "N").collect(Collectors.toList());
    List<String> facilityCodesEquals = searchParameters.getFacilityCodeEquals().stream().map(s -> s.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
    List<String> platformsEquals = searchParameters.getPlatformEquals().stream().map(p -> p.trim().toUpperCase(Locale.ENGLISH)).collect(Collectors.toList());
    List<Long> id = searchParameters.getId();


    if (!cruiseNameContains.isEmpty()) {
      specs.add(SearchUtils.contains(cruiseNameContains, CuratorsCruiseEntity_.CRUISE_NAME));
    }
    if (!cruiseNameEquals.isEmpty()) {
      specs.add(SearchUtils.equal(cruiseNameEquals, CuratorsCruiseEntity_.CRUISE_NAME));
    }
    if (!year.isEmpty()) {
      specs.add(SearchUtils.equal(year, CuratorsCruiseEntity_.YEAR));
    }
    if (!publish.isEmpty()) {
      specs.add(SearchUtils.equal(publish, CuratorsCruiseEntity_.PUBLISH));
    }
    if (!facilityCodesEquals.isEmpty()) {
      specs.add(SearchUtils.equal(facilityCodesEquals, (e) -> e.join(CuratorsCruiseEntity_.FACILITY_MAPPINGS)
          .join(CuratorsCruiseFacilityEntity_.FACILITY).get(CuratorsFacilityEntity_.FACILITY_CODE)));
    }
    if (!platformsEquals.isEmpty()) {
      specs.add(SearchUtils.equal(platformsEquals, (e) -> e.join(CuratorsCruiseEntity_.PLATFORM_MAPPINGS)
          .join(CuratorsCruisePlatformEntity_.PLATFORM).get(PlatformMasterEntity_.PLATFORM_NORMALIZED)));
    }
    if (!id.isEmpty()) {
      specs.add(SearchUtils.equal(id, CuratorsCruiseEntity_.ID));
    }

    return specs;
  }

  @Override
  protected CruiseView toView(CuratorsCruiseEntity entity) {
    CruiseView view = new CruiseView();
    view.setId(entity.getId());
    view.setCruiseName(entity.getCruiseName());
    view.setYear(entity.getYear().intValue());
    view.setPublish(entity.isPublish());
    view.setPlatforms(entity.getPlatformMappings().stream().map(e -> e.getPlatform().getPlatformNormalized()).sorted().collect(Collectors.toList()));
    view.setFacilityCodes(entity.getFacilityMappings().stream().map(e -> e.getFacility().getFacilityCode()).sorted().collect(Collectors.toList()));
    view.setLegs(entity.getLegs().stream().map(CuratorsLegEntity::getLegName).sorted().collect(Collectors.toList()));
    return view;
  }

  @Override
  protected CuratorsCruiseEntity newEntityWithDefaultValues(CruiseView view) {
    return new CuratorsCruiseEntity();
  }

  private void mergePlatforms(CuratorsCruiseEntity entity, List<String> platformNames) {
    Map<String, CuratorsCruisePlatformEntity> existingPlatforms = new HashMap<>();
    for (CuratorsCruisePlatformEntity existingPlatform : entity.getPlatformMappings()) {
      String platformName = existingPlatform.getPlatform().getPlatformNormalized();
      existingPlatforms.put(platformName, existingPlatform);
    }
    Set<CuratorsCruisePlatformEntity> toRemove = new HashSet<>();
    Set<CuratorsCruisePlatformEntity> toAdd = new HashSet<>();
    for(String platformNormalized : platformNames) {
      if(!existingPlatforms.containsKey(platformNormalized)) {
        CuratorsCruisePlatformEntity newEntity = new CuratorsCruisePlatformEntity();
        newEntity.setPlatform(platformMasterRepository
            .findByPlatformNormalized(platformNormalized)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("platform not found: " + platformNormalized).build())));
        toAdd.add(newEntity);
      }
    }
    for (Entry<String, CuratorsCruisePlatformEntity> existing : existingPlatforms.entrySet()) {
      if (!platformNames.contains(existing.getKey())) {
        toRemove.add(existing.getValue());
      }
    }
    toRemove.forEach(entity::removePlatformMapping);
    toAdd.forEach(entity::addPlatformMapping);
  }

  private void mergeFacilities(CuratorsCruiseEntity entity, List<String> facilityCodes) {
    Map<String, CuratorsCruiseFacilityEntity> existingFacilities = new HashMap<>();
    for (CuratorsCruiseFacilityEntity existingFacility : entity.getFacilityMappings()) {
      String facilityCode = existingFacility.getFacility().getFacilityCode();
      existingFacilities.put(facilityCode, existingFacility);
    }
    Set<CuratorsCruiseFacilityEntity> toRemove = new HashSet<>();
    Set<CuratorsCruiseFacilityEntity> toAdd = new HashSet<>();
    for(String facilityCode : facilityCodes) {
      if(!existingFacilities.containsKey(facilityCode)) {
        CuratorsCruiseFacilityEntity newEntity = new CuratorsCruiseFacilityEntity();
        newEntity.setFacility(curatorsFacilityRepository
            .findByFacilityCode(facilityCode)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("facility not found: " + facilityCode).build())));
        toAdd.add(newEntity);
      }
    }
    for (Entry<String, CuratorsCruiseFacilityEntity> existing : existingFacilities.entrySet()) {
      if (!facilityCodes.contains(existing.getKey())) {
        toRemove.add(existing.getValue());
      }
    }
    toRemove.forEach(entity::removeFacilityMapping);
    toAdd.forEach(entity::addFacilityMapping);
  }

  private void mergeLegs(CuratorsCruiseEntity entity, List<String> legs) {
    Map<String, CuratorsLegEntity> existingLegs = new HashMap<>();
    for (CuratorsLegEntity existingLeg : entity.getLegs()) {
      String legName = existingLeg.getLegName();
      existingLegs.put(legName, existingLeg);
    }
    Set<CuratorsLegEntity> toRemove = new HashSet<>();
    Set<CuratorsLegEntity> toAdd = new HashSet<>();
    for(String legName : legs) {
      if(!existingLegs.containsKey(legName)) {
        CuratorsLegEntity newEntity = new CuratorsLegEntity();
        newEntity.setLegName(legName);
        toAdd.add(newEntity);
      }
    }
    for (Entry<String, CuratorsLegEntity> existing : existingLegs.entrySet()) {
      if (!legs.contains(existing.getKey())) {
        toRemove.add(existing.getValue());
      }
    }
    toRemove.forEach(entity::removeLeg);
    toAdd.forEach(entity::addLeg);
  }

  @Override
  protected void updateEntity(CuratorsCruiseEntity entity, CruiseView view) {
    entity.setCruiseName(view.getCruiseName().trim().toUpperCase(Locale.ENGLISH));
    entity.setYear(view.getYear().shortValue());
    entity.setPublish(view.getPublish());
    mergeFacilities(entity, view.getFacilityCodes());
    mergePlatforms(entity, view.getPlatforms());
    mergeLegs(entity, view.getLegs());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsCruiseRepository getRepository() {
    return curatorsCruiseRepository;
  }

}
