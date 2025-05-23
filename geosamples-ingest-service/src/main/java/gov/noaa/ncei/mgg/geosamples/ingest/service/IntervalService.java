package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.ApprovalState;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesApprovalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsAgeRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsMunsellRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsWeathMetaRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntervalService extends
    ApprovalResourceServiceBase<Long, CuratorsIntervalEntity, IntervalSearchParameters, IntervalView, CuratorsIntervalRepository> {

  private static final Map<String, String> viewToEntitySortMapping;

  static {

    Map<String, String> map = new HashMap<>();
    map.put("interval", CuratorsIntervalEntity_.INTERVAL);
    map.put("approvalState", String.format("%s.%s", CuratorsIntervalEntity_.APPROVAL, GeosamplesApprovalEntity_.APPROVAL_STATE));
    viewToEntitySortMapping = Collections.unmodifiableMap(map);

  }


  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final CuratorsLithologyRepository curatorsLithologyRepository;
  private final CuratorsTextureRepository curatorsTextureRepository;
  private final CuratorsAgeRepository curatorsAgeRepository;
  private final CuratorsRockLithRepository curatorsRockLithRepository;
  private final CuratorsRockMinRepository curatorsRockMinRepository;
  private final CuratorsWeathMetaRepository curatorsWeathMetaRepository;
  private final CuratorsRemarkRepository curatorsRemarkRepository;
  private final CuratorsMunsellRepository curatorsMunsellRepository;

  @Autowired
  public IntervalService(CuratorsIntervalRepository curatorsIntervalRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      CuratorsLithologyRepository curatorsLithologyRepository,
      CuratorsTextureRepository curatorsTextureRepository,
      CuratorsAgeRepository curatorsAgeRepository,
      CuratorsRockLithRepository curatorsRockLithRepository,
      CuratorsRockMinRepository curatorsRockMinRepository,
      CuratorsWeathMetaRepository curatorsWeathMetaRepository,
      CuratorsRemarkRepository curatorsRemarkRepository,
      CuratorsMunsellRepository curatorsMunsellRepository) {
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.curatorsLithologyRepository = curatorsLithologyRepository;
    this.curatorsTextureRepository = curatorsTextureRepository;
    this.curatorsAgeRepository = curatorsAgeRepository;
    this.curatorsRockLithRepository = curatorsRockLithRepository;
    this.curatorsRockMinRepository = curatorsRockMinRepository;
    this.curatorsWeathMetaRepository = curatorsWeathMetaRepository;
    this.curatorsRemarkRepository = curatorsRemarkRepository;
    this.curatorsMunsellRepository = curatorsMunsellRepository;
  }

  @Override
  protected List<Specification<CuratorsIntervalEntity>> getSpecs(IntervalSearchParameters searchParameters) {
    List<Specification<CuratorsIntervalEntity>> specs = new ArrayList<>();

    List<Integer> interval = searchParameters.getInterval();
    List<String> imlgs = searchParameters.getImlgs().stream().map(s -> s.trim().toLowerCase(Locale.ENGLISH)).collect(Collectors.toList());
    List<String> facilityCode = searchParameters.getFacilityCode();
    List<ApprovalState> approvalState = searchParameters.getApprovalState();
    List<Boolean> publish = searchParameters.getPublish();

    if (!interval.isEmpty()) {
      specs.add(SearchUtils.equal(interval, CuratorsIntervalEntity_.INTERVAL));
    }
    if (!imlgs.isEmpty()) {
      specs.add(SearchUtils.equal(imlgs, (e) -> e.join(CuratorsIntervalEntity_.SAMPLE).get(CuratorsSampleTsqpEntity_.IMLGS)));
    }
    if (!facilityCode.isEmpty()) {
      specs.add(
          SearchUtils.equal(
              facilityCode,
              (e) -> e.join(CuratorsIntervalEntity_.SAMPLE)
                  .join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY)
                  .join(CuratorsCruiseFacilityEntity_.FACILITY)
                  .get(CuratorsFacilityEntity_.FACILITY_CODE)
          )
      );
    }
    if (!approvalState.isEmpty()) {
      specs.add(SearchUtils.equal(
          approvalState.stream().map(ApprovalState::name).collect(Collectors.toList()),
          e -> e.join(CuratorsIntervalEntity_.APPROVAL).get(GeosamplesApprovalEntity_.APPROVAL_STATE)
      ));
    }
    if (!publish.isEmpty()) {
      specs.add(
          SearchUtils.equal(
              publish.stream().map((p) -> p ? "Y" : "N").collect(Collectors.toList()),
              CuratorsIntervalEntity_.PUBLISH
          )
      );
    }
    return specs;
  }

  private void setCombinedCmMm(Supplier<Integer> getCm, Supplier<Integer> getMm, Consumer<Double> setter) {
    Double combined = null;
    Integer cm = getCm.get();
    Integer mm = getMm.get();
    if(cm != null) {
      combined = Double.valueOf(cm);
    }
    if(combined != null && mm != null) {
      combined = combined + mm / 10D;
    }
    setter.accept(combined);
  }

  @Override
  protected IntervalView toView(CuratorsIntervalEntity entity) {
    IntervalView view = new IntervalView();

    view.setId(entity.getId());
    view.setInterval(entity.getInterval());

    setCombinedCmMm(entity::getDepthTop, entity::getDepthTopMm, view::setDepthTop);
    setCombinedCmMm(entity::getDepthBot, entity::getDepthTopMm, view::setDepthBot);

    view.setLithCode1(entity.getLith1() == null ? null : entity.getLith1().getLithologyCode());
    view.setTextCode1(entity.getText1() == null ? null : entity.getText1().getTextureCode());
    view.setLithCode2(entity.getLith2() == null ? null : entity.getLith2().getLithologyCode());
    view.setTextCode2(entity.getText2() == null ? null : entity.getText2().getTextureCode());
    view.setCompCode1(entity.getComp1() == null ? null : entity.getComp1().getLithologyCode());
    view.setCompCode2(entity.getComp2() == null ? null : entity.getComp2().getLithologyCode());
    view.setCompCode3(entity.getComp3() == null ? null : entity.getComp3().getLithologyCode());
    view.setCompCode4(entity.getComp4() == null ? null : entity.getComp4().getLithologyCode());
    view.setCompCode5(entity.getComp5() == null ? null : entity.getComp5().getLithologyCode());
    view.setCompCode6(entity.getComp6() == null ? null : entity.getComp6().getLithologyCode());
    view.setDescription(entity.getDescription());
    view.setAgeCodes(entity.getAges() == null || entity.getAges().isEmpty() ? Collections.emptyList() :
        entity.getAges().stream()
            .map(CuratorsAgeEntity::getAgeCode)
            .sorted()
            .collect(Collectors.toList())
    );
    view.setWeight(entity.getWeight());
    view.setRockLithCode(entity.getRockLith() == null ? null : entity.getRockLith().getRockLithCode());
    view.setRockMinCode(entity.getRockMin() == null ? null : entity.getRockMin().getRockMinCode());
    view.setWeathMetaCode(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMetaCode());
    view.setRemarkCode(entity.getRemark() == null ? null : entity.getRemark().getRemarkCode());
    view.setMunsellCode(entity.getMunsellCode());
    view.setExhausted("X".equals(entity.getExhaustCode()));
    view.setPhotoLink(entity.getPhotoLink());
    view.setIntComments(entity.getIntComments());

    view.setPublish(entity.isPublish());
    view.setIgsn(entity.getIgsn());
    view.setImlgs(entity.getSample().getImlgs());

    view.setApprovalState(entity.getApproval() != null ? entity.getApproval().getApprovalState() : null);

    return view;
  }

  @Override
  protected CuratorsIntervalEntity newEntityWithDefaultValues(IntervalView view) {
    CuratorsIntervalEntity entity = new CuratorsIntervalEntity();
    entity.setPublish(false);
    return entity;
  }

  private void setCmMm(Supplier<Double> getCombined, Consumer<Integer> setCm, Consumer<Integer> setMm) {
    CmConverter converter = new CmConverter(getCombined.get());
    setCm.accept(converter.getCm());
    setMm.accept(converter.getMm());
  }

  private <R> void setRelation(Supplier<String> codeGetter, Function<String, Optional<R>> find, Consumer<R> setter, String apiField) {
    R relationEntity = null;
    String code = codeGetter.get();
    if(StringUtils.isNotBlank(code)) {
      relationEntity = find.apply(code)
          .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().fieldError(apiField, String.format("Relation not found: %s", code)).build()));
    }
    setter.accept(relationEntity);
  }

  @Override
  protected void updateEntity(CuratorsIntervalEntity entity, IntervalView view) {

    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findById(view.getImlgs())
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find sample parent sample").build()));

    entity.setInterval(view.getInterval());
    entity.setSample(sample);


    setCmMm(view::getDepthTop, entity::setDepthTop, entity::setDepthTopMm);
    setCmMm(view::getDepthBot, entity::setDepthBot, entity::setDepthBotMm);

    setRelation(view::getLithCode1, curatorsLithologyRepository::findByLithologyCode, entity::setLith1, "lithCode1");
    setRelation(view::getTextCode1, curatorsTextureRepository::findByTextureCode, entity::setText1, "textCode1");
    setRelation(view::getLithCode2, curatorsLithologyRepository::findByLithologyCode, entity::setLith2, "lithCode2");
    setRelation(view::getTextCode2, curatorsTextureRepository::findByTextureCode, entity::setText2, "textCode2");
    setRelation(view::getCompCode1, curatorsLithologyRepository::findByLithologyCode, entity::setComp1, "compCode1");
    setRelation(view::getCompCode2, curatorsLithologyRepository::findByLithologyCode, entity::setComp2, "compCode2");
    setRelation(view::getCompCode3, curatorsLithologyRepository::findByLithologyCode, entity::setComp3, "compCode3");
    setRelation(view::getCompCode4, curatorsLithologyRepository::findByLithologyCode, entity::setComp4, "compCode4");
    setRelation(view::getCompCode5, curatorsLithologyRepository::findByLithologyCode, entity::setComp5, "compCode5");
    setRelation(view::getCompCode6, curatorsLithologyRepository::findByLithologyCode, entity::setComp6, "compCode6");

    entity.setDescription(view.getDescription());

    entity.setAges(
        view.getAgeCodes().stream()
            .map(a -> curatorsAgeRepository.findByAgeCode(a).orElseThrow(
                    () -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        ApiError.builder().error(String.format("Age not found for code: %s", a)).build()
                    )
            ))
            .collect(Collectors.toList())
    );


    entity.setWeight(view.getWeight());

    setRelation(view::getRockLithCode, curatorsRockLithRepository::findByRockLithCode, entity::setRockLith, "rockLithCode");
    setRelation(view::getRockMinCode, curatorsRockMinRepository::findByRockMinCode, entity::setRockMin, "rockMinCode");
    setRelation(view::getWeathMetaCode, curatorsWeathMetaRepository::findByWeathMetaCode, entity::setWeathMeta, "weathMetaCode");
    setRelation(view::getRemarkCode, curatorsRemarkRepository::findByRemarkCode, entity::setRemark, "remarkCode");

    String munsell;
      String munsellCode = view.getMunsellCode();
      if(StringUtils.isEmpty(munsellCode)) {
        munsellCode = null;
        munsell = null;
      } else {
        munsell = curatorsMunsellRepository.findById(munsellCode).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Munsell code not found").build()))
            .getMunsell();
      }

    entity.setMunsell(munsell);
      entity.setMunsellCode(munsellCode);

    entity.setExhaustCode(view.getExhausted() == null || !view.getExhausted() ? null : "X");
    entity.setPhotoLink(view.getPhotoLink());
    entity.setIntComments(view.getIntComments());


    boolean publish = view.getPublish() != null && view.getPublish();
    if (publish) {
      if (entity.getApproval() != null) {
        if (!entity.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
          throw new ApiException(
              HttpStatus.BAD_REQUEST,
              ApiError.builder().error(String.format("Interval %s (%s) is not approved", entity.getInterval(), sample.getImlgs())).build()
          );
        }
      }
    }
    if (view.getPublish() && publish != sample.isPublish()) {
      sample.setPublish(publish);
      curatorsSampleTsqpRepository.saveAndFlush(sample);
    }

    entity.setPublish(publish);
    if (view.getIgsn() != null) {
      if (curatorsSampleTsqpRepository.existsByIgsn(view.getIgsn())) {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ApiError.builder().fieldError("igsn", "Interval IGSN must not match sample IGSN").build()
        );
      }
      if (!view.getIgsn().equals(entity.getIgsn())) { // IGSN has changed
        if (curatorsIntervalRepository.existsByIgsn(view.getIgsn())) {
          throw new ApiException(
              HttpStatus.BAD_REQUEST,
              ApiError.builder().fieldError("igsn", "Interval IGSN must not match another interval's IGSN").build()
          );
        }
      }
    }
    entity.setIgsn(view.getIgsn());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsIntervalRepository getRepository() {
    return curatorsIntervalRepository;
  }

  @Override
  protected void validateParentResourceApproval(CuratorsIntervalEntity entity) {
    CuratorsSampleTsqpEntity sample = entity.getSample();
    if (sample.getApproval() != null) {
      if (!sample.getApproval().getApprovalState().equals(ApprovalState.APPROVED)) {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ApiError.builder().error(String.format("Sample %s is not approved", sample.getImlgs())).build()
        );
      }
    }
  }

  @Override
  protected void revokeChildResourceApproval(CuratorsIntervalEntity entity) {
    // intervals have no child resources
  }
}
