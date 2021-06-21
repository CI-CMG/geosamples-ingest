package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RemarkSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RemarkView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsRemarkRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RemarkService extends
    SearchServiceBase<CuratorsRemarkEntity, String, RemarkSearchParameters, RemarkView, CuratorsRemarkRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(RemarkView.class);

  private final CuratorsRemarkRepository curatorsRemarkRepository;

  @Autowired
  public RemarkService(CuratorsRemarkRepository curatorsRemarkRepository) {
    this.curatorsRemarkRepository = curatorsRemarkRepository;
  }


  @Override
  protected List<Specification<CuratorsRemarkEntity>> getSpecs(RemarkSearchParameters searchParameters) {
    List<Specification<CuratorsRemarkEntity>> specs = new ArrayList<>();

    List<String> remark = searchParameters.getRemark();
    List<String> remarkCode = searchParameters.getRemarkCode();

    if (!remark.isEmpty()) {
      specs.add(SearchUtils.contains(remark, CuratorsRemarkEntity_.REMARK));
    }
    if (!remarkCode.isEmpty()) {
      specs.add(SearchUtils.equal(remarkCode, CuratorsRemarkEntity_.REMARK_CODE));
    }

    return specs;
  }

  @Override
  protected RemarkView toView(CuratorsRemarkEntity entity) {
    RemarkView view = new RemarkView();
    view.setRemark(entity.getRemark());
    view.setRemarkCode(entity.getRemarkCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsRemarkEntity newEntityWithDefaultValues(String id) {
    CuratorsRemarkEntity entity = new CuratorsRemarkEntity();
    entity.setRemark(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsRemarkEntity entity, RemarkView view) {
    entity.setRemarkCode(view.getRemarkCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsRemarkRepository getRepository() {
    return curatorsRemarkRepository;
  }

}
