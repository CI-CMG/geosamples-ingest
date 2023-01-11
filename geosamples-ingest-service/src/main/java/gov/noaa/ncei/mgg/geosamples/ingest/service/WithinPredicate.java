package gov.noaa.ncei.mgg.geosamples.ingest.service;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.locationtech.jts.geom.Geometry;

public class WithinPredicate extends AbstractSimplePredicate implements Serializable {

  private final Expression<Geometry> inner;
  private final Expression<Geometry> outer;

  public WithinPredicate(CriteriaBuilder criteriaBuilder, Expression<Geometry> inner, Geometry outer) {
    this(criteriaBuilder, inner, new LiteralExpression<>((CriteriaBuilderImpl) criteriaBuilder, outer));
  }

  private WithinPredicate(CriteriaBuilder criteriaBuilder, Expression<Geometry> inner, Expression<Geometry> outer) {
    super((CriteriaBuilderImpl) criteriaBuilder);
    this.inner = inner;
    this.outer = outer;
  }

  @Override
  public void registerParameters(ParameterRegistry parameterRegistry) {

  }

  private static String renderGeometry(Expression<Geometry> geometry, RenderingContext renderingContext) {
    return ((Renderable) geometry).render(renderingContext);
  }

  @Override
  public String render(boolean notWithin, RenderingContext renderingContext) {
    return String.format(
        " within(%s, %s) = %b",
        renderGeometry(inner, renderingContext),
        renderGeometry(outer, renderingContext),
        !notWithin
    );
  }
}
