package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.MunsellSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.MunsellView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsMunsellEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsMunsellRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MunsellService extends
    SearchServiceBase<CuratorsMunsellEntity, String, MunsellSearchParameters, MunsellView, CuratorsMunsellRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(MunsellView.class);

  private final CuratorsMunsellRepository curatorsMunsellRepository;

  @Autowired
  public MunsellService(CuratorsMunsellRepository curatorsMunsellRepository) {
    this.curatorsMunsellRepository = curatorsMunsellRepository;
  }

  @Override
  protected List<Specification<CuratorsMunsellEntity>> getSpecs(MunsellSearchParameters searchParameters) {
    List<Specification<CuratorsMunsellEntity>> specs = new ArrayList<>();

    List<String> munsell = searchParameters.getMunsell();
    List<String> munsellCode = searchParameters.getMunsellCode();

    if (!munsell.isEmpty()) {
      specs.add(SearchUtils.contains(munsell, CuratorsMunsellEntity_.MUNSELL));
    }
    if (!munsellCode.isEmpty()) {
      specs.add(SearchUtils.equal(munsellCode, CuratorsMunsellEntity_.MUNSELL_CODE));
    }

    return specs;
  }

  @Override
  protected MunsellView toView(CuratorsMunsellEntity entity) {
    MunsellView view = new MunsellView();
    view.setMunsell(entity.getMunsell());
    view.setMunsellCode(entity.getMunsellCode());
    return view;
  }

  @Override
  protected CuratorsMunsellEntity newEntityWithDefaultValues(MunsellView view) {
    CuratorsMunsellEntity entity = new CuratorsMunsellEntity();
    entity.setMunsellCode(view.getMunsellCode());
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsMunsellEntity entity, MunsellView view) {
    entity.setMunsell(view.getMunsell());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsMunsellRepository getRepository() {
    return curatorsMunsellRepository;
  }

}
