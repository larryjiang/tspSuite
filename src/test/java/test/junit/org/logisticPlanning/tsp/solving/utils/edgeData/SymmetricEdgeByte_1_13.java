package test.junit.org.logisticPlanning.tsp.solving.utils.edgeData;

/**
 * Testing a configuration of the EdgeNumber facility.
 */
public class SymmetricEdgeByte_1_13 extends _EdgeNumberTest {

  /** create the SymmetricEdgeByte_1_13 */
  public SymmetricEdgeByte_1_13() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  final boolean isSymmetric() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  final int n() {
    return 13;
  }

  /** {@inheritDoc} */
  @Override
  final long getMaxValue() {
    return ((java.lang.Byte.MAX_VALUE) + (1l));
  }

  /** {@inheritDoc} */
  @Override
  final int floatType() {
    return 0;
  }

}
