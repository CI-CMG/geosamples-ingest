package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.SortDirection;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    return contains(searchValues, e -> e.get(columnAlias));
  }

  public static <E> Specification<E> contains(List<String> searchValues, Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) ->
        cb.or(
            searchValues.stream().map(v -> cb.like(cb.lower(columnAlias.apply(e)), contains(v.toLowerCase(Locale.ENGLISH)), '/'))
                .collect(Collectors.toList()).toArray(new Predicate[0]));
  }

  public static <E> Specification<E> equal(List<?> searchValues,  Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) -> equalOne(e, cb, searchValues, columnAlias);
  }

  public static <E> Predicate equalOne(Root<E> e, CriteriaBuilder cb, List<?> searchValues,  Function<Root<E>, Expression<String>> columnAlias) {
    return cb.or(searchValues.stream().map(v -> cb.equal(columnAlias.apply(e), v)).collect(Collectors.toList()).toArray(new Predicate[0]));
  }

//  public static <E> Specification<E> greaterThanOrEqual(List<String> searchValues,  Function<Root<E>, Expression<String>> columnAlias) {
//    return (e, cq, cb) ->
//        cb.or(searchValues.stream().map(v -> cb.greaterThanOrEqualTo(columnAlias.apply(e), v)).collect(Collectors.toList()).toArray(new Predicate[0]));
//  }
//
//  public static <E> Specification<E> lessThanOrEq(List<String> searchValues,  Function<Root<E>, Expression<String>> columnAlias) {
//    return (e, cq, cb) ->
//        cb.or(searchValues.stream().map(v -> cb.greaterThanOrEqualTo(columnAlias.apply(e), v)).collect(Collectors.toList()).toArray(new Predicate[0]));
//  }

  public static <E> Specification<E> equal(List<?> searchValues, String columnAlias) {
    return equal(searchValues, e -> e.get(columnAlias));
  }

  public static Specification<CuratorsSampleTsqpEntity> findExistingSample(CuratorsSampleTsqpEntity sample) {
    return (e, cq, cb) -> cb.and(
        cb.equal(e.join(CuratorsSampleTsqpEntity_.CRUISE).get(CuratorsCruiseEntity_.CRUISE_NAME), sample.getCruise().getCruiseName()),
        cb.equal(e.get(CuratorsSampleTsqpEntity_.SAMPLE), sample.getSample()),
        cb.equal(e.join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY).get(CuratorsFacilityEntity_.FACILITY_CODE), sample.getCruiseFacility().getFacility().getFacilityCode()),
        cb.equal(e.join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM).get(PlatformMasterEntity_.PLATFORM), sample.getCruisePlatform().getPlatform().getPlatform()),
        cb.equal(e.get(CuratorsSampleTsqpEntity_.DEVICE), sample.getDevice())
        );
  }

}
