package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.SortDirection;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class SearchUtils {


  public static Map<String, String> mapViewToEntitySort(Class<?> cls) {
    Map<String, String> viewToEntitySortMapping = new HashMap<>();
    Sortable sortable = cls.getAnnotation(Sortable.class);
    for (String field : sortable.value()) {
      String mapped = field;
      viewToEntitySortMapping.put(field, mapped);
    }
    return Collections.unmodifiableMap(viewToEntitySortMapping);
  }


  public static String escape(String s) {
    return s.replaceAll("/", "//").replaceAll("_", "/_").replaceAll("%", "/%");
  }

  public static String contains(String s) {
    return "%" + escape(s) + "%";
  }

  public static Order paramsToOrder(Map<String, String> viewToEntitySortMapping, String definedSort) {
    String[] parts = definedSort.split(":");
    String viewSort = parts[0];
    SortDirection direction = SortDirection.valueOf(parts[1].toLowerCase(Locale.ENGLISH));
    String dbSort = viewToEntitySortMapping.get(viewSort);
    if (dbSort == null) {
      throw new IllegalStateException("Sorting is not defined for " + definedSort);
    }
    return new Order(direction == SortDirection.asc ? Direction.ASC : Direction.DESC, dbSort)
        .nullsLast()
        .ignoreCase();
  }

  public static <E> Specification<E> and(@Nullable Specification<E> a, Specification<E> b) {
    if (a == null) {
      return b;
    }
    return a.and(b);
  }

  public static <E> Specification<E> contains(List<String> searchValues, String columnAlias) {
    return (e, cq, cb) ->
        cb.or(
            searchValues.stream().map(v -> cb.like(cb.lower(e.get(columnAlias)), contains(v.toLowerCase(Locale.ENGLISH)), '/'))
                .collect(Collectors.toList()).toArray(new Predicate[0]));
  }

  public static <E> Specification<E> equal(List<?> searchValues, String columnAlias) {
    return (e, cq, cb) ->
        cb.or(searchValues.stream().map(v -> cb.equal(e.get(columnAlias), v)).collect(Collectors.toList()).toArray(new Predicate[0]));
  }

}
