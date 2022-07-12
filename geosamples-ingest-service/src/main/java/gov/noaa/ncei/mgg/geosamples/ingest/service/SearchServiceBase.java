package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiError;
import gov.noaa.ncei.mgg.geosamples.ingest.api.error.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagingAndSortingParameters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class SearchServiceBase<E, I, S extends PagingAndSortingParameters, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, I>> {

  protected abstract Map<String, String> getViewToEntitySortMapping();

  protected abstract List<Specification<E>> getSpecs(S searchParameters);

  protected abstract R getRepository();

  protected abstract V toView(E entity);

  protected abstract E newEntityWithDefaultValues(V view);

  protected abstract void updateEntity(E entity, V view);

  protected I normalizeId(I id) {
    return id;
  }

  private E getRequiredEntity(I id) {
    return getRepository()
        .findById(normalizeId(id))
        .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build()));
  }


  private E toEntity(V view, I id) {
    E entity;
    if (id == null) {
      entity = newEntityWithDefaultValues(view);
    } else {
      entity = getRequiredEntity(id);
    }
    updateEntity(entity, view);
    return entity;
  }

  public V create(V view) {
    return toView(getRepository().save(toEntity(view, null)));
  }

  public V update(V view, I id) {
    return toView(getRepository().save(toEntity(view, id)));
  }

  public V delete(I id) {
    E entity = getRequiredEntity(id);
    getRepository().delete(entity);
    return toView(entity);
  }

  public V get(I id) {
    return toView(getRequiredEntity(id));
  }

  public PagedItemsView<V> search(S searchParameters) {
    Sort sort = Sort.by(searchParameters.getOrder().stream()
        .map(def -> SearchUtils.paramsToOrder(getViewToEntitySortMapping(), def))
        .collect(Collectors.toList()));

    List<Specification<E>> specs = getSpecs(searchParameters);

    //nullable
    Specification<E> spec = specs.stream().reduce(null, SearchUtils::and);

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<E> page = getRepository().findAll(spec, PageRequest.of(pageIndex, maxPerPage, sort));

    return new PagedItemsView.Builder<V>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(this::toView).collect(Collectors.toList()))
        .build();
  }

}
