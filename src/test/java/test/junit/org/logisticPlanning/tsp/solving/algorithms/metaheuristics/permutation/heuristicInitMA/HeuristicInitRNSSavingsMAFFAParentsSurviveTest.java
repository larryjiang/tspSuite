package test.junit.org.logisticPlanning.tsp.solving.algorithms.metaheuristics.permutation.heuristicInitMA;

import java.util.Random;

import org.logisticPlanning.tsp.solving.algorithms.metaheuristics.permutation.heuristicInitMA.HeuristicInitRNSSavingsMAFFA;

import test.junit.org.logisticPlanning.tsp.solving.algorithms.TSPAlgorithmSymmetricTest;

/**
 * the test of the permutation-based heuristic init savings MA with RNS
 * mutation and ffa
 */
public class HeuristicInitRNSSavingsMAFFAParentsSurviveTest extends
    TSPAlgorithmSymmetricTest {

  /** create */
  public HeuristicInitRNSSavingsMAFFAParentsSurviveTest() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  protected HeuristicInitRNSSavingsMAFFA createAlgorithm() {
    HeuristicInitRNSSavingsMAFFA res;
    Random r;
    int mu, lambda;

    res = new HeuristicInitRNSSavingsMAFFA();
    res.setParentsSurvive(true);

    r = new Random();
    mu = (1 + r.nextInt(512));
    lambda = (mu + 1 + r.nextInt(512));
    res.setMu(mu);
    res.setLambda(lambda);

    return res;
  }
}
