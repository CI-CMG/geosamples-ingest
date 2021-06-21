package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TextureSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.TextureView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsTextureRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TextureService extends
    SearchServiceBase<CuratorsTextureEntity, String, TextureSearchParameters, TextureView, CuratorsTextureRepository> {

  private static final Map<String, String> viewToEntitySortMapping = SearchUtils.mapViewToEntitySort(TextureView.class);

  private final CuratorsTextureRepository curatorsTextureRepository;

  @Autowired
  public TextureService(CuratorsTextureRepository curatorsTextureRepository) {
    this.curatorsTextureRepository = curatorsTextureRepository;
  }

  @Override
  protected List<Specification<CuratorsTextureEntity>> getSpecs(TextureSearchParameters searchParameters) {
    List<Specification<CuratorsTextureEntity>> specs = new ArrayList<>();

    List<String> texture = searchParameters.getTexture();
    List<String> textureCode = searchParameters.getTextureCode();

    if (!texture.isEmpty()) {
      specs.add(SearchUtils.contains(texture, CuratorsTextureEntity_.TEXTURE));
    }
    if (!textureCode.isEmpty()) {
      specs.add(SearchUtils.equal(textureCode, CuratorsTextureEntity_.TEXTURE_CODE));
    }

    return specs;
  }

  @Override
  protected TextureView toView(CuratorsTextureEntity entity) {
    TextureView view = new TextureView();
    view.setTexture(entity.getTexture());
    view.setTextureCode(entity.getTextureCode());
    view.setSourceUri(entity.getSourceUri());
    return view;
  }

  @Override
  protected CuratorsTextureEntity newEntityWithDefaultValues(String id) {
    CuratorsTextureEntity entity = new CuratorsTextureEntity();
    entity.setTexture(id);
    entity.setPublish("Y");
    return entity;
  }

  @Override
  protected void updateEntity(CuratorsTextureEntity entity, TextureView view) {
    entity.setTextureCode(view.getTextureCode());
    entity.setSourceUri(view.getSourceUri());
  }


  @Override
  protected Map<String, String> getViewToEntitySortMapping() {
    return viewToEntitySortMapping;
  }

  @Override
  protected CuratorsTextureRepository getRepository() {
    return curatorsTextureRepository;
  }

}
