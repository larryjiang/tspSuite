package test.junit.org.logisticPlanning.tsp.solving.algorithms.localSearch.permutation.localOpt;

import org.logisticPlanning.tsp.solving.algorithms.localSearch.permutation.localOpt.LocalNOpt;
import org.logisticPlanning.tsp.solving.operators.permutation.localOpt.ExhaustivelyEnumeratingLocal7Optimizer;

/**
 * the test of the local 7-opt algorithm
 */
public class Local7OptTestRandomOverlap extends LocalNOptTestRandomOverlap {

  /** create */
  public Local7OptTestRandomOverlap() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  protected LocalNOpt createAlgorithm() {
    final LocalNOpt algo;

    algo = super.createAlgorithm();
    algo.setLocalOptimizer(new ExhaustivelyEnumeratingLocal7Optimizer());
    return algo;
  }
}
