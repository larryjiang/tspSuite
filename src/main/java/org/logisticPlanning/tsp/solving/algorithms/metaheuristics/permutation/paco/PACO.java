package org.logisticPlanning.tsp.solving.algorithms.metaheuristics.permutation.paco;

import java.io.PrintStream;
import java.util.Arrays;

import org.logisticPlanning.tsp.benchmarking.instances.Instance;
import org.logisticPlanning.tsp.benchmarking.objective.ObjectiveFunction;
import org.logisticPlanning.tsp.solving.Individual;
import org.logisticPlanning.tsp.solving.TSPAlgorithm;
import org.logisticPlanning.tsp.solving.TSPAlgorithmRunner;
import org.logisticPlanning.tsp.solving.algorithms.metaheuristics.permutation.paco.update.Age;
import org.logisticPlanning.tsp.solving.utils.NodeManager;
import org.logisticPlanning.utils.config.Configurable;
import org.logisticPlanning.utils.config.Configuration;
import org.logisticPlanning.utils.math.random.Randomizer;

/**
 * 
 <p>
 * In this class, we implement the Population-based ACO algorithm for
 * symmetric and asymmetric TSPs.
 * </p>
 * <h2 id="paco">Population-based ACO</h2>
 * <p>
 * The Population-based <a href=
 * "https://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms"
 * >ACO</a> algorithm (pACO)&nbsp;[<a href="#cite_G2004AAISAMCE"
 * style="font-weight:bold">1</a>, <a href="#cite_GM2002APBAFA"
 * style="font-weight:bold">2</a>, <a href="#cite_GM2002APBATDOP"
 * style="font-weight:bold">3</a>] is a version of the Ant Colony
 * Optimization Algorithm that does not require the presence and updating
 * of a complete pheromone matrix in memory. Instead, it maintains a set
 * (&quot;population&quot;) of {@code k} ants (paths, solutions,
 * permutations) which in their describe the pheromone information &#964;
 * completely. The efficiency of this algorithm has been verified
 * in&nbsp;[<a href="#cite_OHSRD2011ADAOTPBACOAFTTATQ"
 * style="font-weight:bold">4</a>, <a
 * href="#cite_OHSRD2011ADAOTPBACOAFTTATQ2"
 * style="font-weight:bold">5</a>].
 * </p>
 * <p>
 * <a href=
 * "https://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms">Ant
 * Colony Optimization</a> has been introduced by Dorigo&nbsp;[<a
 * href="#cite_D1992OLANA" style="font-weight:bold">6</a>, <a
 * href="#cite_DS2004ACO" style="font-weight:bold">7</a>, <a
 * href="#cite_DB2005ACO" style="font-weight:bold">8</a>, <a
 * href="#cite_DBS2006ACOAAAACIT" style="font-weight:bold">9</a>, <a
 * href="#cite_DGMS2002SCOACO" style="font-weight:bold">10</a>]. In ACO,
 * solutions are generated by a constructive heuristic. The constructive
 * heuristic proceeds as follows: A simulated ant starts at a node in a
 * graph. It will decide to which node to move next based on two pieces on
 * information assigned to each edge: a static heuristic value assigned to
 * the edge and a &quot;pheromone&quot; value assigned to the edge which
 * may be modified. The ant continues to move until it reaches a
 * destination node. In TSPs, this means that it will visit all nodes. In
 * each of the algorithm's rounds, several solutions are created by
 * applying the heuristic/ant simulation process. At the end of the
 * iteration, the pheromones on the edges will be updated: If a solution
 * was particularly well, then the pheromones of the edges it includes will
 * be increased and thus, make the edge more likely to be visited by ants
 * in the future round. The pheromones in plain ACO are stored in a matrix
 * of {@code n*n} elements, but in pACO, only {@code k*n} elements are
 * necessary, with {@code k<<n}.
 * </p>
 * <p>
 * The idea of pACO is that each edge in each of these {@code k} solutions
 * contributes exactly an amount &#916; to the amount of pheromone on that
 * edge. If an ant/solution {@code x} enters the population, &#916;
 * pheromone is added to each edge in {@code x}. Vice versa, if an ant
 * leaves the population, for instance because it was replaced by another
 * younger or better ant, exactly this amount of pheromone is subtracted
 * again from the pheromone value of all of its edges. No other negative
 * update (like vaporization or something) is necessary.
 * </p>
 * <p>
 * The default configuration of the algorithm is taken mainly from&nbsp;[<a
 * href="#cite_GM2002APBATDOP" style="font-weight:bold">3</a>], with the
 * exception of {@code m=10} and {@code k=5} which are taken from&nbsp;[<a
 * href="#cite_GM2002APBAFA" style="font-weight:bold">2</a>].
 * </p>
 * <p>
 * This class provides two hook methods,
 * {@link #createInitialPopulation(Individual[], ObjectiveFunction)} and
 * {@link #refineSolution(int[], long, ObjectiveFunction)}, which allow to
 * extend the ACO with local search capability.
 * </p>
 * <h2>References</h2>
 * <ol>
 * <li><div><span id="cite_G2004AAISAMCE" /><a
 * href="http://www.aifb.kit.edu/web/Michael_Guntsch/en">Michael
 * Guntsch</a>: <span
 * style="font-style:italic;font-family:cursive;">&ldquo;Ant Algorithms in
 * Stochastic and Multi-Criteria Environments,&rdquo;</span> PhD Thesis,
 * January&nbsp;13, 2004, Karlsruhe, Germany: University of Karlsruhe
 * (Friedriciana), Department of Economics and Business Engineering
 * and&nbsp;Karlsruhe, Germany: University of Karlsruhe (Friedriciana),
 * Institute for Applied Computer Science and Formal Description Methods
 * (AIFB). Google Book ID:&nbsp;<a
 * href="http://books.google.com/books?id=Lf1ztwAACAAJ">Lf1ztwAACAAJ</a>.
 * <div>links: [<a
 * href="http://www.lania.mx/~ccoello/EMOO/thesis_guntsch.pdf.gz">1</a>]
 * and&nbsp;[<a
 * href="http://digbib.ubka.uni-karlsruhe.de/volltexte/212004">2</a>];
 * urn:&nbsp;<a href=
 * "http://wm-urn.org/?urn=urn:nbn:de:swb:90-AAA2120045&amp;redirect=1"
 * >urn:nbn:de:swb:90-AAA2120045</a></div></div></li>
 * <li><div><span id="cite_GM2002APBAFA" /><a
 * href="http://www.aifb.kit.edu/web/Michael_Guntsch/en">Michael
 * Guntsch</a> and&nbsp;<a href=
 * "http://pacosy.informatik.uni-leipzig.de/15-0-Prof+Dr+Martin+Middendorf.html"
 * >Martin Middendorf</a>: <span style="font-weight:bold">&ldquo;A
 * Population Based Approach for ACO,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">Applications of
 * Evolutionary Computing, Proceedings of EvoWorkshops 2002: EvoCOP,
 * EvoIASP, EvoSTIM/EvoPLAN (EvoWorkshops'02)</span>, April&nbsp;2&ndash;4,
 * 2002, Kinsale, Ireland, pages 72&ndash;81, <a
 * href="http://www.ce.unipr.it/people/cagnoni/">Stefano Cagnoni</a>, Jens
 * Gottlieb, <a
 * href="http://www.soc.napier.ac.uk/~emmah/Prof_Emma_Hart/Welcome.html"
 * >Emma Hart</a>, <a href=
 * "http://pacosy.informatik.uni-leipzig.de/15-0-Prof+Dr+Martin+Middendorf.html"
 * >Martin Middendorf</a>, and&nbsp;<a
 * href="https://www.ads.tuwien.ac.at/raidl/">G&#252;nther R. Raidl</a>,
 * editors, volume 2279 of Lecture Notes in Computer Science (LNCS),
 * Berlin, Germany: Springer-Verlag GmbH. ISBN:&nbsp;<a
 * href="https://www.worldcat.org/isbn/3540434321">3-540-43432-1</a>
 * and&nbsp;<a
 * href="https://www.worldcat.org/isbn/9783540434320">978-3-540-
 * 43432-0</a>; doi:&nbsp;<a
 * href="http://dx.doi.org/10.1007/3-540-46004-7_8"
 * >10.1007/3-540-46004-7_8</a>; ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/03029743">0302-9743</a> and&nbsp;<a
 * href="https://www.worldcat.org/issn/16113349">1611-3349</a>.
 * <div>CiteSeer<sup>x</sup><sub
 * style="font-style:italic">&#946;</sub>:&nbsp;<a href
 * ="http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.13.2514"
 * >10.1.1.13 .2514</a></div></div></li>
 * <li><div><span id="cite_GM2002APBATDOP" /><a
 * href="http://www.aifb.kit.edu/web/Michael_Guntsch/en">Michael
 * Guntsch</a> and&nbsp;<a href=
 * "http://pacosy.informatik.uni-leipzig.de/15-0-Prof+Dr+Martin+Middendorf.html"
 * >Martin Middendorf</a>: <span style="font-weight:bold">&ldquo;Applying
 * Population Based ACO to Dynamic Optimization Problems,&rdquo;</span> in
 * <span style="font-style:italic;font-family:cursive;">From Ant Colonies
 * to Artificial Ants &#8210; Proceedings of the Third International
 * Workshop on Ant Colony Optimization (ANTS'02)</span>,
 * September&nbsp;12&ndash;14, 2002, Brussels, Belgium, pages
 * 111&ndash;122, <a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>, <a
 * href="http://www.idsia.ch/~gianni/">Gianni A. Di Caro</a>,
 * and&nbsp;Michael Samples, editors, volume 2463/2002 of Lecture Notes in
 * Computer Science (LNCS), Berlin, Germany: Springer-Verlag GmbH.
 * ISBN:&nbsp;<a
 * href="https://www.worldcat.org/isbn/3540441468">3-540-44146-8</a>
 * and&nbsp;<a
 * href="https://www.worldcat.org/isbn/9783540441465">978-3-540-
 * 44146-5</a>; doi:&nbsp;<a
 * href="http://dx.doi.org/10.1007/3-540-45724-0_10">10.1007/3-540-45724
 * -0_10</a>; ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/03029743">0302-9743</a> and&nbsp;<a
 * href="https://www.worldcat.org/issn/16113349">1611-3349</a>.
 * <div>CiteSeer<sup>x</sup><sub
 * style="font-style:italic">&#946;</sub>:&nbsp;<a href
 * ="http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.12.6580"
 * >10.1.1.12 .6580</a></div></div></li>
 * <li><div><span id="cite_OHSRD2011ADAOTPBACOAFTTATQ" />Sabrina M.
 * Oliveira, Mohamed Saifullah Hussin, <a
 * href="http://iridia.ulb.ac.be/~stuetzle/">Thomas St&#252;tzle</a>,
 * Andrea Roli, and&nbsp;<a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>:
 * <span style="font-weight:bold">&ldquo;A Detailed Analysis of the
 * Population-based Ant Colony Optimization Algorithm for the TSP and the
 * QAP,&rdquo;</span> <span
 * style="font-style:italic;font-family:cursive;">Technical Report</span>
 * Number&nbsp;TR/IRIDIA/2011-006, February&nbsp;2011; published by
 * Brussels, Belgium: Universit&#233; Libre de Bruxelles, Institut de
 * Recherches Interdisciplinaires et de D&#233;veloppements en Intelligence
 * Artificielle (IRIDIA). ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/17813794">1781-3794</a>. <div>link:
 * [<a href
 * ="http://iridia.ulb.ac.be/IridiaTrSeries/rev/IridiaTr2011-006r001.pdf"
 * >1</ a>]</div></div></li>
 * <li><div><span id="cite_OHSRD2011ADAOTPBACOAFTTATQ2" />Sabrina M.
 * Oliveira, Mohamed Saifullah Hussin, <a
 * href="http://iridia.ulb.ac.be/~stuetzle/">Thomas St&#252;tzle</a>,
 * Andrea Roli, and&nbsp;<a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>:
 * <span style="font-weight:bold">&ldquo;A Detailed Analysis of the
 * Population-based Ant Colony Optimization Algorithm for the TSP and the
 * QAP,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">Proceedings of the
 * Genetic and Evolutionary Computation Conference (GECCO'11)</span>,
 * July&nbsp;12&ndash;16, 2011, Dublin, Ireland, pages 13&ndash;14.
 * doi:&nbsp;<a href="http://dx.doi.org/10.1145/2001858.2001866">10.1145/
 * 2001858.2001866</a>. <div>link: [<a
 * href="http://code.ulb.ac.be/dbfiles/OliHusStu-etal2011gecco.pdf"
 * >1</a>]</div></div></li>
 * <li><div><span id="cite_D1992OLANA" /><a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>:
 * <span
 * style="font-style:italic;font-family:cursive;">&ldquo;Optimization,
 * Learning and Natural Algorithms,&rdquo;</span> PhD Thesis,
 * January&nbsp;1992, Milano, Italy: Dipartimento di Elettronica,
 * Politecnico di Milano</div></li>
 * <li><div><span id="cite_DS2004ACO" /><a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>
 * and&nbsp;<a href="http://iridia.ulb.ac.be/~stuetzle/">Thomas
 * St&#252;tzle</a>: <span
 * style="font-style:italic;font-family:cursive;">&ldquo;Ant Colony
 * Optimization,&rdquo;</span> July&nbsp;1, 2004, Bradford Books,
 * Cambridge, MA, USA: MIT Press. ISBN:&nbsp;<a
 * href="https://www.worldcat.org/isbn/0262042193">0-262-04219-3</a>
 * and&nbsp;<a
 * href="https://www.worldcat.org/isbn/9780262042192">978-0-262-
 * 04219-2</a>; Google Book ID:&nbsp;<a
 * href="http://books.google.com/books?id=_aefcpY8GiEC"
 * >_aefcpY8GiEC</a></div></li>
 * <li><div><span id="cite_DB2005ACO" /><a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>
 * and&nbsp;Christian Blum: <span style="font-weight:bold">&ldquo;Ant
 * Colony Optimization Theory: A Survey,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">Theoretical Computer
 * Science</span> 344(2-3), November&nbsp;17, 2005; published by Essex, UK:
 * Elsevier Science Publishers B.V.. doi:&nbsp;<a
 * href="http://dx.doi.org/10.1016/j.tcs.2005.05.020"
 * >10.1016/j.tcs.2005.05.020</a>; ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/03043975">0304-3975</a>. <div>link:
 * [<a
 * href="http://code.ulb.ac.be/dbfiles/DorBlu2005tcs.pdf">1</a>]</div></
 * div></li>
 * <li><div><span id="cite_DBS2006ACOAAAACIT" /><a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>,
 * Mauro Birattari, and&nbsp;<a
 * href="http://iridia.ulb.ac.be/~stuetzle/">Thomas St&#252;tzle</a>: <span
 * style="font-weight:bold">&ldquo;Ant Colony Optimization &#8210;
 * Artificial Ants as a Computational Intelligence Technique,&rdquo;</span>
 * in <span style="font-style:italic;font-family:cursive;">IEEE
 * Computational Intelligence Magazine (CIM)</span> 1(4):28&ndash;39,
 * November&nbsp;2006; published by Piscataway, NJ, USA: IEEE Computational
 * Intelligence Society. doi:&nbsp;<a
 * href="http://dx.doi.org/10.1109/MCI.2006.329691"
 * >10.1109/MCI.2006.329691</a>; ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/1556603X">1556-603X</a>; INSPEC
 * Accession Number:&nbsp;9184238. <div>links: [<a
 * href="http://iridia.ulb.ac.be/~mbiro/paperi/IridiaTr2006-023r001.pdf"
 * >1</a>] and&nbsp;[<a
 * href="http://iridia.ulb.ac.be/~mbiro/paperi/DorBirStu2006ieee-cim.pdf"
 * >2</a>]; CiteSeer<sup>x</sup><sub
 * style="font-style:italic">&#946;</sub>:&nbsp;<a
 * href="http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.64.9532"
 * >10.1.1.64.9532</a></div></div></li>
 * <li><div><span id="cite_DGMS2002SCOACO" /><a
 * href="https://en.wikipedia.org/wiki/Marco_Dorigo">Marco Dorigo</a>, <a
 * href="http://www.idsia.ch/~luca/">Luca Maria Gambardella</a>, <a href=
 * "http://pacosy.informatik.uni-leipzig.de/15-0-Prof+Dr+Martin+Middendorf.html"
 * >Martin Middendorf</a>, and&nbsp;<a
 * href="http://iridia.ulb.ac.be/~stuetzle/">Thomas St&#252;tzle</a>: <span
 * style="font-weight:bold">&ldquo;Special Section on &#8220;Ant Colony
 * Optimization&#8221;,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">IEEE Transactions on
 * Evolutionary Computation (IEEE-EC)</span> 6(4):317&ndash;365,
 * August&nbsp;2002; published by Washington, DC, USA: IEEE Computer
 * Society. LCCN:&nbsp;<a href="http://lccn.loc.gov/97648327">97648327</a>;
 * doi:&nbsp;<a href
 * ="http://dx.doi.org/10.1109/TEVC.2002.802446">10.1109/TEVC
 * .2002.802446</a>; ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/1089778X">1089-778X</a> and&nbsp;<a
 * href="https://www.worldcat.org/issn/19410026">1941-0026</a>;
 * CODEN:&nbsp;<a href=
 * "http://www-library.desy.de/cgi-bin/spiface/find/coden/www?code=ITEVF5"
 * >ITEVF5</a>; further information: [<a
 * href="http://www.ieee-cis.org/pubs/tec/">1</a>]</div></li>
 * </ol>
 */

public class PACO extends TSPAlgorithm {
  /** the serial version uid */
  private static final long serialVersionUID = 1L;

  /** the default {@link #m_alpha alpha}: {@value} */
  public static final double DEFAULT_ALPHA = 1d;
  /** the default {@link #m_beta beta}: {@value} */
  public static final double DEFAULT_BETA = 5d;
  /** the default {@link #m_populationSize population size} k: {@value} */
  public static final int DEFAULT_POPULATION_SIZE = 5;
  /** the default {@link #m_q0 q0}: {@value} */
  public static final double DEFAULT_Q0 = 0.9d;
  /** the default {@link #m_tauMax tau_max}: {@value} */
  public static final double DEFAULT_TAU_MAX = 1d;
  /** the default {@link #m_antCount ant count} m: {@value} */
  public static final int DEFAULT_ANT_COUNT = 10;
  /** the default {@link #m_update update strategy} */
  private static final PopulationUpdateStrategy DEFAULT_UPDATE = Age.INSTANCE;

  /** the {@link #m_alpha alpha} parameter: {@value} */
  public static final String PARAM_ALPHA = "alpha"; //$NON-NLS-1$
  /** the {@link #m_beta beta} parameter: {@value} */
  public static final String PARAM_BETA = "beta"; //$NON-NLS-1$
  /** the {@link #m_populationSize population size} parameter k: {@value} */
  public static final String PARAM_POPULATION_SIZE = "populationSize"; //$NON-NLS-1$
  /** the @link #m_q0 q0} parameter: {@value} */
  public static final String PARAM_Q0 = "q0"; //$NON-NLS-1$
  /** the {@link #m_tauMax tau_max} parameter: {@value} */
  public static final String PARAM_TAU_MAX = "tauMax"; //$NON-NLS-1$
  /** the {@link #m_antCount ant count} parameter m: {@value} */
  public static final String PARAM_ANT_COUNT = "antsPerIteration"; //$NON-NLS-1$
  /** the {@link #m_update update strategy} parameter: {@value} */
  private static final String PARAM_UPDATE = "populationUpdateStrategy"; //$NON-NLS-1$

  /**
   * The error distance which is used for zero distances: Here, we simply
   * use a value of 2. Normally, the heuristic value used for any distance
   * {@code d} is {@code 1/d}. Shorter distances lead to larger heuristic
   * values. As distances are always integers, the heuristic values are
   * always in {@code (0, 1]}. For a zero distance, we choose {@code 2} as
   * it is larger (better) than any other distance.
   */
  private static final double ERROR_NU = (2d);

  /**
   * the alpha parameter ruling the influence of the pheromone&nbsp;[<a
   * href="#cite_G2004AAISAMCE" style="font-weight:bold">1</a>, <a
   * href="#cite_GM2002APBAFA" style="font-weight:bold">2</a>, <a
   * href="#cite_GM2002APBATDOP" style="font-weight:bold">3</a>], with
   * default value {@value #DEFAULT_ALPHA}.
   * 
   * @serial serializable field
   */
  private double m_alpha;

  /**
   * the beta parameter ruling the influence of the distance&nbsp;[<a
   * href="#cite_G2004AAISAMCE" style="font-weight:bold">1</a>, <a
   * href="#cite_GM2002APBAFA" style="font-weight:bold">2</a>, <a
   * href="#cite_GM2002APBATDOP" style="font-weight:bold">3</a>], with
   * default value {@value #DEFAULT_BETA}.
   * 
   * @serial serializable field
   */
  private double m_beta;

  /**
   * the population size {@code k}, i.e., the number of ants maintained,
   * i.e., the ants/paths defining the pheromones&nbsp;[<a
   * href="#cite_G2004AAISAMCE" style="font-weight:bold">1</a>, <a
   * href="#cite_GM2002APBAFA" style="font-weight:bold">2</a>, <a
   * href="#cite_GM2002APBATDOP" style="font-weight:bold">3</a>], with
   * default value {@value #DEFAULT_POPULATION_SIZE}.
   * 
   * @serial serializable field
   */
  private int m_populationSize;

  /**
   * the probability {@code q0} of simply taking the best heuristic
   * step&nbsp;[<a href="#cite_G2004AAISAMCE"
   * style="font-weight:bold">1</a>, <a href="#cite_GM2002APBAFA"
   * style="font-weight:bold">2</a>, <a href="#cite_GM2002APBATDOP"
   * style="font-weight:bold">3</a>], with default value
   * {@value #DEFAULT_Q0}
   * 
   * @serial serializable field
   */
  private double m_q0;

  /**
   * the maximum pheromone value <code>tau<sub>max</sub></code>&nbsp;[<a
   * href="#cite_G2004AAISAMCE" style="font-weight:bold">1</a>, <a
   * href="#cite_GM2002APBAFA" style="font-weight:bold">2</a>, <a
   * href="#cite_GM2002APBATDOP" style="font-weight:bold">3</a>], with
   * default value {@value #DEFAULT_TAU_MAX}
   * 
   * @serial serializable field
   */
  private double m_tauMax;

  /**
   * the number {@code m} of ants created per iteration, from which the
   * best ant may enter the population&nbsp;[<a href="#cite_G2004AAISAMCE"
   * style="font-weight:bold">1</a>, <a href="#cite_GM2002APBAFA"
   * style="font-weight:bold">2</a>, <a href="#cite_GM2002APBATDOP"
   * style="font-weight:bold">3</a>], with default value
   * {@value #DEFAULT_ANT_COUNT}
   * 
   * @serial serializable field
   */
  private int m_antCount;

  /**
   * the population update strategy&nbsp;[<a href="#cite_G2004AAISAMCE"
   * style="font-weight:bold">1</a>, <a href="#cite_GM2002APBAFA"
   * style="font-weight:bold">2</a>, <a href="#cite_GM2002APBATDOP"
   * style="font-weight:bold">3</a>]
   * 
   * @serial serializable field
   */
  private PopulationUpdateStrategy m_update;

  /** a temporary variable for the current solution */
  private transient int[] m_cur;

  /** a temporary variable for the distances */
  private transient int[] m_dists;

  /** a temporary variable for the pheromone decision table */
  private transient double[] m_table;

  /** a temporary variable for the node manager */
  transient NodeManager m_nodes;

  /** a temporary variable for the pheromone matrix */
  private transient PheromoneMatrix m_matrix;

  /** a temporary variable for the population */
  private transient PACOIndividual[] m_pop;

  /**
   * instantiate
   * 
   * @param name
   *          the name of the algorithm, to be prepented to the name of
   *          {@link PACO}
   */
  protected PACO(final String name) {
    super(((name != null) ? (name) : "") + //$NON-NLS-1$
        "Population-based ACO"); //$NON-NLS-1$
    this.m_alpha = PACO.DEFAULT_ALPHA;
    this.m_beta = PACO.DEFAULT_BETA;
    this.m_populationSize = PACO.DEFAULT_POPULATION_SIZE;
    this.m_q0 = PACO.DEFAULT_Q0;
    this.m_tauMax = PACO.DEFAULT_TAU_MAX;
    this.m_antCount = PACO.DEFAULT_ANT_COUNT;
    this.m_update = PACO.DEFAULT_UPDATE;
    this.__clearInstance();
  }

  /** instantiate */
  public PACO() {
    this(null);
  }

  /**
   * Get the alpha parameter ruling the influence of the pheromone
   * 
   * @return the alpha parameter ruling the influence of the pheromone
   */
  public final double getAlpha() {
    return this.m_alpha;
  }

  /**
   * Set the alpha parameter ruling the influence of the pheromone
   * 
   * @param alpha
   *          the alpha parameter ruling the influence of the pheromone
   */
  public final void setAlpha(final double alpha) {
    this.m_alpha = alpha;
  }

  /**
   * Get the beta parameter ruling the influence of the distance
   * 
   * @return the beta parameter ruling the influence of the distance
   */
  public final double getBeta() {
    return this.m_beta;
  }

  /**
   * Set the beta parameter ruling the influence of the distance
   * 
   * @param beta
   *          the beta parameter ruling the influence of the distance
   */
  public final void setBeta(final double beta) {
    this.m_beta = beta;
  }

  /**
   * Get the population size
   * 
   * @return the population size
   */
  public final int getPopulationSize() {
    return this.m_populationSize;
  }

  /**
   * set the population size
   * 
   * @param ps
   *          the population size
   */
  public final void setPopulationSize(final int ps) {
    this.m_populationSize = ps;
  }

  /**
   * Get the number of ants per iteration
   * 
   * @return the number of ants per iteration
   */
  public final int getAntCount() {
    return this.m_antCount;
  }

  /**
   * set the number of ants per iteration
   * 
   * @param ac
   *          the number of ants per iteration
   */
  public final void setAntCount(final int ac) {
    this.m_antCount = ac;
  }

  /** initialize an instance: this method is called by clone */
  private final void __clearInstance() {
    this.m_cur = null;
    this.m_dists = null;
    this.m_table = null;
    this.m_pop = null;
    this.m_nodes = null;
    this.m_matrix = null;
  }

  /** {@inheritDoc} */
  @Override
  public PACO clone() {
    final PACO res;

    res = ((PACO) (super.clone()));
    res.__clearInstance();
    res.m_update = ((PopulationUpdateStrategy) (res.m_update.clone()));

    return res;
  }

  /**
   * Execute the population-based ACO&nbsp;[<a href="#cite_G2004AAISAMCE"
   * style="font-weight:bold">1</a>, <a href="#cite_GM2002APBAFA"
   * style="font-weight:bold">2</a>, <a href="#cite_GM2002APBATDOP"
   * style="font-weight:bold">3</a>]
   * 
   * @param f
   *          the objective function defining the TSP problem to solve
   */
  @Override
  public final void solve(final ObjectiveFunction f) {
    final int n, m;
    final PheromoneMatrix matrix;
    final int[] cur, dists;
    final double[] table;
    final NodeManager nodes;
    final Randomizer r;
    final double alpha, beta, q0;
    final PACOIndividual[] pop;
    final PopulationUpdateStrategy update;

    PACOIndividual bestGen;
    int curAnt, i, j, curNode, lastNode, nodesLeft, dist, bestNode, bestDist;
    double phero, bestPhero, pheroSum;
    boolean decideRandomly;
    long curTotalDist, gen;
    boolean initialized;

    // initialize all local variables and stuff
    n = f.n();
    matrix = this.m_matrix;

    // the node manager is an O(1) thingy that allows us to 1) keep track
    // of
    // all unvisited nodes, 2) delete nodes by visiting them, 3) find
    // random
    // nodes and deleting them
    nodes = this.m_nodes;

    // allocate temporary variables
    cur = this.m_cur;
    dists = this.m_dists;
    table = this.m_table;

    r = f.getRandom();

    // get local copies of algorithm parameters
    m = this.m_antCount;
    alpha = this.m_alpha;
    beta = this.m_beta;
    q0 = this.m_q0;
    update = this.m_update;

    curAnt = 0; // curAnt = the index of the ant
    gen = 1l; // the algorithm iteration/generation index

    // the initially empty population
    pop = this.m_pop;

    // Create the initial population, if initialization heuristics are
    // used.
    // If
    // individuals are created, check them into the pheromone matrix.
    this.createInitialPopulation(pop, f);// call the initialization
    // heuristics
    initialized = false;
    for (final PACOIndividual ind : pop) {// check if any individuals were
      // created
      if (ind.tourLength != Individual.TOUR_LENGTH_NOT_SET) {
        ind.m_birthday = gen; // ok, individuals were created, set
        // birthday
        initialized = true; // remember to increase generation
        matrix.add(ind.solution);// check individuals into matrix
      } else { // ok, no more individuals, break loop
        break; // this is the fast exit if no initialization
      }
    }
    if (initialized) {// individuals were created by a heuristic
      gen++;// thus, we need to increase the generation
    }

    bestGen = new PACOIndividual();

    // main algorithm part: run as long as we can
    while (!(f.shouldTerminate())) {
      nodes.init(n);

      // Build one new candidate solution by simulating the behavior of
      // one ant
      // moving through the graph.
      nodesLeft = n;
      i = 0;
      cur[i++] = curNode = nodes.deleteRandom(r); // start at a random
      // node
      curTotalDist = 0l;

      // Visit the nodes, after starting at a random node (skipping the
      // last
      // node as there is no decision to make for the last node).
      for (; (--nodesLeft) > 1;) {// for (n-2) times do...
        lastNode = curNode;

        // With probability q0, always choose best node directly.
        decideRandomly = (r.nextDouble() >= q0);

        // Ok, calculate the matrix stuff.
        // First: setup the best values.
        bestPhero = Double.NEGATIVE_INFINITY;
        bestDist = bestNode = (-1);
        pheroSum = 0d;

        // Then: for each node which is not yet assigned...
        for (j = 0; j < nodesLeft; j++) {
          // Get that node.
          curNode = nodes.getByIndex(j);

          // Get the distance from the last node.
          dist = f.distance(lastNode, curNode);

          // Compute the pheromone/heuristic value.
          phero = (Math.pow(matrix.get(lastNode, curNode), alpha) * //
          Math.pow(((dist != 0) ? (1d / dist) : PACO.ERROR_NU), beta));

          // Is this the best pheromone/heuristic value?
          if (phero >= bestPhero) { // Then remember it.
            bestPhero = phero;
            bestNode = curNode;
            bestDist = dist;
          }

          if (decideRandomly) {
            // Only if we actually are going to use the tables we
            // need to add
            // up the pheromone/heuristic values and remember them.
            // This is
            // needed to later make a value-proportional choice.
            // Otherwise, if
            // we decide deterministically anyway, we don't do this
            // to save
            // runtime.
            pheroSum += phero;
            table[j] = pheroSum;
            dists[j] = dist;
          }
        }

        // Ok, by now we have either found the best node to add (in case
        // of
        // !decideRandomly) or built the complete distance/pheromone
        // decision
        // table (in case of decideRandomly).
        // After we have decided, bestDist should hold the distance of
        // this
        // step and curNode is the selected node.
        if (decideRandomly) {
          // Decide randomly based on the table that we have
          // constructed.
          table[nodesLeft - 1] = Double.POSITIVE_INFINITY;
          j = Arrays.binarySearch(table, 0, nodesLeft,//
              r.nextDouble() * pheroSum);
          if (j < 0) {
            j = (-(j + 1));
          }
          bestDist = dists[j];
          curNode = nodes.getByIndex(j);
        } else {
          // No random decision: choose the best.
          curNode = bestNode;
        }

        // Visit the chosen node!
        cur[i++] = curNode;// Store node in solution.
        nodes.deleteByID(curNode);// Delete node from list of available
        // nodes.
        curTotalDist += bestDist;
      }

      // Add the last node: There only is one choice.
      cur[i] = bestNode = nodes.deleteLast();

      // And we can compute the total distance by adding the distance to
      // the
      // last node and the distance back to the beginning.
      curTotalDist += f.distance(curNode, bestNode) + //
          f.distance(bestNode, cur[0]);

      // Register this FE: We have constructed a complete solution!
      f.registerFE(cur, curTotalDist);

      // Call the local search procedure, if any. This procedure, if it
      // does
      // something, must also register its FEs and DEs.
      curTotalDist = this.refineSolution(cur, curTotalDist, f);

      // Is this the best ant of this generation so far?
      if (curTotalDist < bestGen.tourLength) {
        bestGen.setup(cur, curTotalDist, gen);
      }

      curAnt++; // Ok, one ant has finished
      if (curAnt == m) { // Every m steps...
        // we update the population and matrix!
        update.update(pop, bestGen, matrix);

        // Start next cycle of m ants.
        curAnt = 0;
        gen++;
        bestGen.doclear();
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void configure(final Configuration config) {
    super.configure(config);

    this.m_alpha = config.getDouble(PACO.PARAM_ALPHA, 0d, 1000d,
        this.m_alpha);

    this.m_beta = config
        .getDouble(PACO.PARAM_BETA, 0d, 1000d, this.m_beta);

    this.m_antCount = config.getInt(PACO.PARAM_ANT_COUNT, 1, 10000,//
        this.m_antCount);

    this.m_populationSize = config.getInt(PACO.PARAM_POPULATION_SIZE, 1,
        10000, this.m_populationSize);

    this.m_q0 = config.getDouble(PACO.PARAM_Q0, 0d, 1d, this.m_q0);

    this.m_tauMax = config.getDouble(PACO.PARAM_TAU_MAX, 0.5d, 100000d,
        this.m_tauMax);

    this.m_update = config.getInstance(PACO.PARAM_UPDATE,
        PopulationUpdateStrategy.class, null, this.m_update);
  }

  /** {@inheritDoc} */
  @Override
  public void printConfiguration(final PrintStream ps) {
    super.printConfiguration(ps);

    Configurable.printKey(PACO.PARAM_ALPHA, ps);
    ps.println(this.m_alpha);

    Configurable.printKey(PACO.PARAM_BETA, ps);
    ps.println(this.m_beta);

    Configurable.printKey(PACO.PARAM_ANT_COUNT, ps);
    ps.println(this.m_antCount);

    Configurable.printKey(PACO.PARAM_POPULATION_SIZE, ps);
    ps.println(this.m_populationSize);

    Configurable.printKey(PACO.PARAM_Q0, ps);
    ps.println(this.m_q0);

    Configurable.printKey(PACO.PARAM_TAU_MAX, ps);
    ps.println(this.m_tauMax);

    Configurable.printKey(PACO.PARAM_UPDATE, ps);
    Configurable.printlnObject(this.m_update, ps);
  }

  /** {@inheritDoc} */
  @Override
  public void printParameters(final PrintStream ps) {
    super.printParameters(ps);

    Configurable.printKey(PACO.PARAM_ALPHA, ps);
    ps.println("the weight of the pheromones"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_BETA, ps);
    ps.println("the weight of the visibility (inverse node distance)"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_ANT_COUNT, ps);
    ps.println("the number of ants/solutions per algorithm iteration"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_POPULATION_SIZE, ps);
    ps.println("the size of the population"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_Q0, ps);
    ps.println("the probability to follow the heuristic-maximizing path"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_TAU_MAX, ps);
    ps.println("the maximum pheromone"); //$NON-NLS-1$

    Configurable.printKey(PACO.PARAM_UPDATE, ps);
    ps.println("the population update strategy"); //$NON-NLS-1$
  }

  /**
   * This method can be overridden to create the initial population of ACO.
   * This may be useful if an heuristic initialization procedure is
   * performed. The default implementation in class {@link PACO} does
   * nothing.
   * 
   * @param pop
   *          the population to fill
   * @param f
   *          the objective function
   */
  protected void createInitialPopulation(final Individual<int[]>[] pop,
      final ObjectiveFunction f) {
    //
  }

  /**
   * This method can be overridden to perform a local search on an
   * individual which was created by ACO. The default implementation in
   * class {@link PACO} does nothing.
   * 
   * @param solution
   *          the solution to be refined. The contents of this array may be
   *          modified
   * @param tourLength
   *          the tour length of {@code solution}
   * @param f
   *          the objective function
   * @return the tour length of whatever is now stored in {@code solution}
   */
  protected long refineSolution(final int[] solution,
      final long tourLength, final ObjectiveFunction f) {
    return tourLength;
  }

  /**
   * Perform the population-based ACO
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    TSPAlgorithmRunner.benchmark(//
        Instance.ALL_INSTANCES, PACO.class,//
        args);
  }

  /** {@inheritDoc} */
  @Override
  public void beginRun(final ObjectiveFunction f) {
    final int n;
    final PACOIndividual[] res;
    int i;

    super.beginRun(f);

    this.m_nodes = new NodeManager();

    n = f.n();
    this.m_matrix = new PheromoneMatrix();
    this.m_matrix.init(n, this.m_populationSize, this.m_tauMax);

    this.m_cur = new int[n];
    this.m_dists = new int[n];
    this.m_table = new double[n];

    this.m_pop = res = new PACOIndividual[this.m_populationSize];
    for (i = res.length; (--i) >= 0;) {
      res[i] = new PACOIndividual();
    }

    this.m_update.beginRun(f);
  }

  /** {@inheritDoc} */
  @Override
  public void endRun(final ObjectiveFunction f) {
    this.__clearInstance();
    try {
      this.m_update.endRun(f);
    } finally {
      super.endRun(f);
    }
  }
}
