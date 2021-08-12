package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.IntervalView;
import gov.noaa.ncei.mgg.geosamples.ingest.config.ServiceProperties;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.IntervalPk;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntervalService extends
    SearchServiceBase<CuratorsIntervalEntity, IntervalPk, IntervalSearchParameters, IntervalView, CuratorsIntervalRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(IntervalView.class);


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
  private final SampleDataUtils sampleDataUtils;
  private final ServiceProperties serviceProperties;

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
      CuratorsMunsellRepository curatorsMunsellRepository, SampleDataUtils sampleDataUtils,
      ServiceProperties serviceProperties) {
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
    this.sampleDataUtils = sampleDataUtils;
    this.serviceProperties = serviceProperties;
  }

  @Override
  protected List<Specification<CuratorsIntervalEntity>> getSpecs(IntervalSearchParameters searchParameters) {
    List<Specification<CuratorsIntervalEntity>> specs = new ArrayList<>();

    List<Integer> interval = searchParameters.getInterval();

    if (!interval.isEmpty()) {
      specs.add(SearchUtils.equal(interval, CuratorsIntervalEntity_.INTERVAL));
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


    setCombinedCmMm(entity::getDepthTop, entity::getDepthTopMm, view::setDepthTop);
    setCombinedCmMm(entity::getDepthBot, entity::getDepthTopMm, view::setDepthBot);

    view.setDhCoreId(entity.getDhCoreId());

    setCombinedCmMm(entity::getDhCoreLength, entity::getDhCoreLengthMm, view::setDhCoreLength);

    view.setDhCoreInterval(entity.getDhCoreInterval());

    setCombinedCmMm(entity::getdTopInDhCore, entity::getdTopInDhCore, view::setdTopInDhCore);
    setCombinedCmMm(entity::getdBotInDhCore, entity::getdBotInDhCore, view::setdBotInDhCore);

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
    view.setAgeCode(entity.getAge() == null ? null : entity.getAge().getAgeCode());
    view.setAbsoluteAgeTop(entity.getAbsoluteAgeTop());
    view.setAbsoluteAgeBot(entity.getAbsoluteAgeBot());
    view.setWeight(entity.getWeight());
    view.setRockLithCode(entity.getRockLith() == null ? null : entity.getRockLith().getRockLithCode());
    view.setRockMinCode(entity.getRockMin() == null ? null : entity.getRockMin().getRockMinCode());
    view.setWeathMetaCode(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMetaCode());
    view.setRemarkCode(entity.getRemark() == null ? null : entity.getRemark().getRemarkCode());
    view.setMunsellCode(entity.getMunsellCode());
    view.setExhausted("X".equals(entity.getExhaustCode()));
    view.setPhotoLink(entity.getPhotoLink());
    view.setLake(entity.getLake());
    view.setUnitNumber(entity.getUnitNumber());
    view.setIntComments(entity.getIntComments());
    view.setDhDevice(entity.getDhDevice());

    setCombinedCmMm(entity::getCmcdTop, entity::getMmcdTop, view::setCdTop);
    setCombinedCmMm(entity::getCmcdBot, entity::getMmcdBot, view::setCdBot);

    view.setPublish("Y".equals(entity.getPublish()));
    view.setIgsn(entity.getIgsn());
    view.setImlgs(entity.getParentEntity().getImlgs());

    return view;
  }

  @Override
  protected CuratorsIntervalEntity newEntityWithDefaultValues(IntervalView view) {
    CuratorsSampleTsqpEntity sample = curatorsSampleTsqpRepository.findById(view.getImlgs())
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find sample parent sample").build()));
    CuratorsIntervalEntity entity = new CuratorsIntervalEntity();
    entity.setInterval(view.getInterval());
    entity.setParentEntity(sample);
    entity.setPublish("N");
    return entity;
  }

  private void setCmMm(Supplier<Double> getCombined, Consumer<Integer> setCm, Consumer<Integer> setMm) {
    CmConverter converter = new CmConverter(getCombined.get());
    setCm.accept(converter.getCm());
    setMm.accept(converter.getMm());
  }

  private <R> void setRelation(Supplier<String> codeGetter, Function<String, Optional<R>> find, Consumer<R> setter) {
    R relationEntity = null;
    String code = codeGetter.get();
    if(StringUtils.isNotBlank(code)) {
      relationEntity = find.apply(code)
          .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Relation not found: " + code).build()));
    }
    setter.accept(relationEntity);
  }

  @Override
  protected void updateEntity(CuratorsIntervalEntity entity, IntervalView view) {

    CuratorsSampleTsqpEntity sample = entity.getParentEntity();

    entity.setParentEntity(sample);

    setCmMm(view::getDepthTop, entity::setDepthTop, entity::setDepthTopMm);
    setCmMm(view::getDepthBot, entity::setDepthBot, entity::setDepthBotMm);

    entity.setDhCoreId(view.getDhCoreId());

    setCmMm(view::getDhCoreLength, entity::setDhCoreLength, entity::setDhCoreLengthMm);

    entity.setDhCoreInterval(view.getDhCoreInterval());

    setCmMm(view::getdTopInDhCore, entity::setdTopInDhCore, entity::setdTopMmInDhCore);
    setCmMm(view::getdBotInDhCore, entity::setdBotInDhCore, entity::setdBotMmInDhCore);

    setRelation(view::getLithCode1, curatorsLithologyRepository::findByLithologyCode, entity::setLith1);
    setRelation(view::getTextCode1, curatorsTextureRepository::findByTextureCode, entity::setText1);
    setRelation(view::getLithCode2, curatorsLithologyRepository::findByLithologyCode, entity::setLith2);
    setRelation(view::getTextCode2, curatorsTextureRepository::findByTextureCode, entity::setText2);
    setRelation(view::getCompCode1, curatorsLithologyRepository::findByLithologyCode, entity::setComp1);
    setRelation(view::getCompCode2, curatorsLithologyRepository::findByLithologyCode, entity::setComp2);
    setRelation(view::getCompCode3, curatorsLithologyRepository::findByLithologyCode, entity::setComp3);
    setRelation(view::getCompCode4, curatorsLithologyRepository::findByLithologyCode, entity::setComp4);
    setRelation(view::getCompCode5, curatorsLithologyRepository::findByLithologyCode, entity::setComp5);
    setRelation(view::getCompCode6, curatorsLithologyRepository::findByLithologyCode, entity::setComp6);

    entity.setDescription(view.getDescription());

    setRelation(view::getAgeCode, curatorsAgeRepository::findByAgeCode, entity::setAge);


    entity.setAbsoluteAgeTop(view.getAbsoluteAgeTop());
    entity.setAbsoluteAgeBot(view.getAbsoluteAgeBot());
    entity.setWeight(view.getWeight());

    setRelation(view::getRockLithCode, curatorsRockLithRepository::findByRockLithCode, entity::setRockLith);
    setRelation(view::getRockMinCode, curatorsRockMinRepository::findByRockMinCode, entity::setRockMin);
    setRelation(view::getWeathMetaCode, curatorsWeathMetaRepository::findByWeathMetaCode, entity::setWeathMeta);
    setRelation(view::getRemarkCode, curatorsRemarkRepository::findByRemarkCode, entity::setRemark);

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
    entity.setLake(view.getLake());
    entity.setUnitNumber(view.getUnitNumber());
    entity.setIntComments(view.getIntComments());
    entity.setDhDevice(entity.getDhDevice());


    setCmMm(view::getCdTop, entity::setCmcdTop, entity::setMmcdTop);
    setCmMm(view::getCdBot, entity::setCmcdBot, entity::setMmcdBot);


    String publish = view.getPublish() == null || !view.getPublish() ? "N" : "Y";
    if (view.getPublish() && !publish.equals(sample.getPublish())) {
      sample.setPublish(publish);
      curatorsSampleTsqpRepository.saveAndFlush(sample);
    }

    entity.setPublish(publish);
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

}
