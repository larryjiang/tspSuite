package org.logisticPlanning.tsp.evaluation.modules.impl.single.ecdf;

import org.logisticPlanning.tsp.evaluation.data.properties.StatisticRunProperty;
import org.logisticPlanning.utils.graphics.chart.spec.range.AxisEnd;
import org.logisticPlanning.utils.graphics.chart.spec.range.AxisRange2DDef;
import org.logisticPlanning.utils.graphics.chart.spec.range.EValueSelector;
import org.logisticPlanning.utils.graphics.chart.spec.range.FixedAxisEnd;
import org.logisticPlanning.utils.graphics.chart.spec.range.MultipleOfAxisBigEnd;
import org.logisticPlanning.utils.graphics.chart.spec.range.MultipleOfAxisSmallEnd;

/**
 * <p>
 * Some utility methods related to diagrams for the Empirical (Cumulative)
 * Distribution Functions (ECDFs)&nbsp;[<a href="#cite_HAFR2012RPBBOBES"
 * style="font-weight:bold">1</a>, <a href="#cite_HS1998ELVAPAR"
 * style="font-weight:bold">2</a>].
 * </p>
 * <h2>References</h2>
 * <ol>
 * <li><div><span id="cite_HAFR2012RPBBOBES" /><a
 * href="http://www.lri.fr/~hansen/">Nikolaus Hansen</a>, <a
 * href="http://www.lri.fr/~auger/">Anne Auger</a>, <a
 * href="http://www.researchgate.net/profile/Steffen_Finck/">Steffen
 * Finck</a>, and&nbsp;<a
 * href="https://tao.lri.fr/tiki-index.php?page=people">Raymond Ros</a>:
 * <span style="font-weight:bold">&ldquo;Real-Parameter Black-Box
 * Optimization Benchmarking: Experimental Setup,&rdquo;</span> <span
 * style="font-style:italic;font-family:cursive;">Technical Report</span>
 * March&nbsp;24, 2012; published by Orsay, France: Universit&#233; Paris
 * Sud, Institut National de Recherche en Informatique et en Automatique
 * (INRIA) Futurs, &#201;quipe TAO. <div>link: [<a href=
 * "http://coco.lri.fr/BBOB-downloads/download11.05/bbobdocexperiment.pdf"
 * >1</a>]</div></div></li>
 * <li><div><span id="cite_HS1998ELVAPAR" /><a
 * href="http://www.cs.ubc.ca/~hoos/">Holger H. Hoos</a> and&nbsp;<a
 * href="http://iridia.ulb.ac.be/~stuetzle/">Thomas St&#252;tzle</a>: <span
 * style="font-weight:bold">&ldquo;Evaluating Las Vegas Algorithms &#8210;
 * Pitfalls and Remedies,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">Proceedings of the 14th
 * Conference on Uncertainty in Artificial Intelligence (UAI'98)</span>,
 * July&nbsp;24&ndash;26, 1998, Madison, WI, USA, pages 238&ndash;245,
 * Gregory F. Cooper and&nbsp;Serafin Moral, editors, San Francisco, CA,
 * USA: Morgan Kaufmann Publishers Inc.. ISBN:&nbsp;<a
 * href="https://www.worldcat.org/isbn/155860555X">1-55860-555-X</a>
 * and&nbsp;<a
 * href="https://www.worldcat.org/isbn/9781558605558">978-1-55860
 * -555-8</a>. <div>link: [<a href=
 * "http://www.intellektik.informatik.tu-darmstadt.de/TR/1998/98-02.ps.Z"
 * >1</a>]</div></div></li>
 * </ol>
 */
public final class ECDFUtils {

  /** the axis range definition for scale axis x for FEs and DEs */
  public static final AxisRange2DDef DEFAULT_AXIS_DEF_COUNT;
  /**
   * the axis range definition for scale axis x for Time and normalized
   * time
   */
  public static final AxisRange2DDef DEFAULT_AXIS_DEF_TIME;

  static {
    final AxisEnd a, b, c, d;

    a = new MultipleOfAxisSmallEnd(0, EValueSelector.FINITE_POINT, 0.5d);
    b = new MultipleOfAxisBigEnd(0, EValueSelector.FINITE_POINT, 0.5d);
    c = new FixedAxisEnd(0d);
    d = new FixedAxisEnd(1d);

    DEFAULT_AXIS_DEF_COUNT = new AxisRange2DDef(a, b, c, d);
    DEFAULT_AXIS_DEF_TIME = new AxisRange2DDef(a, b, true, c, d, false);
  }

  /** the default maximum number of points collected in ecdf diagrams */
  public static final int DEFAULT_MAX_POINTS = StatisticRunProperty.DEFAULT_MAX_POINTS;

}
