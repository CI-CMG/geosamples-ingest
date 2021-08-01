package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.SortDirection;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.Sortable;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.SampleBase;
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

  public static <T extends SampleBase> Specification<T> findExistingSample(SampleBase sample) {
    return (e, cq, cb) -> cb.and(
        cb.equal(e.get("cruise"), sample.getCruise()),
        cb.equal(e.get("sample"), sample.getSample()),
        cb.equal(e.get("facility"), sample.getFacility()),
        cb.equal(e.get("platform"), sample.getPlatform()),
        cb.equal(e.get("device"), sample.getDevice()),
        cb.equal(e.get("beginDate"), sample.getBeginDate()),
        cb.equal(e.get("endDate"), sample.getEndDate()),
        cb.equal(e.get("lat"), sample.getLat()),
        cb.equal(e.get("endLat"), sample.getEndLat()),
        cb.equal(e.get("lon"), sample.getLon()),
        cb.equal(e.get("endLon"), sample.getEndLon()),
        cb.equal(e.get("latLonOrig"), sample.getLatLonOrig()),
        cb.equal(e.get("waterDepth"), sample.getWaterDepth()),
        cb.equal(e.get("endWaterDepth"), sample.getEndWaterDepth()),
        cb.equal(e.get("storageMeth"), sample.getStorageMeth()),
        cb.equal(e.get("coredLength"), sample.getCoredLength()),
        cb.equal(e.get("coredLengthMm"), sample.getCoredLengthMm()),
        cb.equal(e.get("coredDiam"), sample.getCoredDiam()),
        cb.equal(e.get("coredDiamMm"), sample.getCoredDiamMm()),
        cb.equal(e.get("pi"), sample.getPi()),
        cb.equal(e.get("province"), sample.getProvince()),
        cb.equal(e.get("lake"), sample.getLake()),
        cb.equal(e.get("otherLink"), sample.getOtherLink()),
        cb.equal(e.get("igsn"), sample.getIgsn()),
        cb.equal(e.get("leg"), sample.getLeg()),
        cb.equal(e.get("sampleComments"), sample.getSampleComments())
        );
  }

}
