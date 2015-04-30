package org.logisticPlanning.tsp.evaluation.data.properties.limit;

import org.logisticPlanning.tsp.benchmarking.objective.DataPoint;
import org.logisticPlanning.tsp.evaluation.data.Run;
import org.logisticPlanning.tsp.evaluation.data.RunSet;

/**
 * A collection of data reflecting a state in the end of a run.
 */
final class _ConvergenceLimitDataCollection extends _LimitDataCollection {

  /**
   * the run set
   * 
   * @param rs
   *          the run set
   */
  _ConvergenceLimitDataCollection(final RunSet rs) {
    super(rs);
  }

  /** {@inheritDoc} */
  @Override
  public final double get(final int point, final int dimension) {
    final Run r;
    final long l;
    int s;
    DataPoint p1, p2;

    r = this.m_rs.get(point);
    s = r.size();
    if (s > 0) {
      p2 = p1 = r.get(--s);
      l = p1.getBestF();
      for (; (--s) >= 0;) {
        p2 = p1;
        p1 = r.get(s);
        if (l < p1.getBestF()) {
          break;
        }
      }

      return p2.get(dimension);
    }

    return Double.POSITIVE_INFINITY;
  }

}
