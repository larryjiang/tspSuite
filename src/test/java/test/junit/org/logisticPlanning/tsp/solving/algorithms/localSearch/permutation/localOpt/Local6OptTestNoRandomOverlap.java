package test.junit.org.logisticPlanning.tsp.solving.algorithms.localSearch.permutation.localOpt;

import org.logisticPlanning.tsp.solving.algorithms.localSearch.permutation.localOpt.LocalNOpt;
import org.logisticPlanning.tsp.solving.operators.permutation.localOpt.ExhaustivelyEnumeratingLocal6Optimizer;

/**
 * the test of the local 6-opt algorithm
 */
public class Local6OptTestNoRandomOverlap extends
    LocalNOptTestNoRandomOverlap {

  /** create */
  public Local6OptTestNoRandomOverlap() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  protected LocalNOpt createAlgorithm() {
    final LocalNOpt algo;

    algo = super.createAlgorithm();
    algo.setLocalOptimizer(new ExhaustivelyEnumeratingLocal6Optimizer());
    return algo;
  }
}
