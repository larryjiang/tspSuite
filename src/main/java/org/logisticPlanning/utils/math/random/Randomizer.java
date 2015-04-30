package org.logisticPlanning.utils.math.random;

import java.util.Random;

/**
 * This unsynchronized random number generator overrides the class
 * {@link java.util.Random}. This class has three major purposes:</p>
 * <ol>
 * <li>First, implements the exactly same behavior (as off Java 1.7),
 * which, in turn, is based on well-known algorithms&nbsp;[<a
 * href="#cite_K1969TAOCPSNA" style="font-weight:bold">1</a>, <a
 * href="#cite_BM1958RAN" style="font-weight:bold">2</a>]. The reason of
 * implementing the same behavior already provided by Java is that we
 * cannot be sure about future changes, i.e., whether some of the backing
 * algorithms of the random number generation are changed. This may result
 * in different behavior of software relying on them - in particular when
 * we want to repeat experiments with exactly the same random number
 * sequence. Therefore, by fixing the algorithms here, we can avoid such
 * issues (at the cost that updates/improvements by the JVM are not used by
 * us).</li>
 * <li>Second, it removes the synchronization. We do not need
 * synchronization, as we use instances of the randomizer only within a
 * single thread. Removing the synchronization may thus result in a much
 * faster computation - at least in cases where the JIT cannot or does not
 * remove the synchronization automatically. By furthermore making the
 * class {@code final} we aim to encourage the JIT to inline as much of its
 * code as possible.</li>
 * <li>Finally, we add additional routines for generating random number.</li>
 * </ol>
 * <h2>References</h2>
 * <ol>
 * <li><div><span id="cite_K1969TAOCPSNA" />Donald Ervin Knuth: <span
 * style="font-style:italic;font-family:cursive;">&ldquo;Seminumerical
 * Algorithms,&rdquo;</span> 1969, volume 2 of The Art of Computer
 * Programming (TAOCP), Boston, MA, USA: Addison-Wesley Longman Publishing
 * Co., Inc.. ISBN:&nbsp;<a
 * href="https://www.worldcat.org/isbn/0201896842">0-201-89684-2</a>, <a
 * href="https://www.worldcat.org/isbn/8177583352">8177583352</a>, <a
 * href="https://www.worldcat.org/isbn/9780201896848"
 * >978-0-201-89684-8</a>, and&nbsp;<a
 * href="https://www.worldcat.org/isbn/9788177583359">978-8177583359</a>;
 * OCLC:&nbsp;<a href="https://www.worldcat.org/oclc/85975465">85975465</a>
 * and&nbsp;<a href="https://www.worldcat.org/oclc/38207978">38207978</a>;
 * Google Book ID:&nbsp;<a
 * href="http://books.google.com/books?id=OtLNKNVh1XoC">OtLNKNVh1XoC</a>;
 * ASIN:&nbsp;<a
 * href="http://www.amazon.com/dp/0201896842">0201896842</a></div></li>
 * <li><div><span id="cite_BM1958RAN" />George Edward Pelham Box
 * and&nbsp;Mervin Edgar Muller: <span style="font-weight:bold">&ldquo;A
 * Note on the Generation of Random Normal Deviates,&rdquo;</span> in <span
 * style="font-style:italic;font-family:cursive;">The Annals of
 * Mathematical Statistics</span> 29(2):610&ndash;611, June&nbsp;1958;
 * published by Beachwood, OH, USA: Institute of Mathematical Statistics.
 * LCCN:&nbsp;<a href="http://lccn.loc.gov/sn98-23312">sn98-23312</a>;
 * doi:&nbsp;<a
 * href="http://dx.doi.org/10.1214/aoms/1177706645">10.1214/aoms
 * /1177706645</a>; JSTOR stable:&nbsp;<a
 * href="http://www.jstor.org/stable/2237361">2237361</a>; OCLC:&nbsp;<a
 * href="https://www.worldcat.org/oclc/40280837">40280837</a>;
 * ISSN:&nbsp;<a
 * href="https://www.worldcat.org/issn/00034851">0003-4851</a>. <div>link:
 * [<a
 * href="http://projecteuclid.org/euclid.aoms/1177706645">1</a>]</div></
 * div></li>
 * </ol>
 */
public final class Randomizer extends Random {
  /** the serial version uid */
  private static final long serialVersionUID = 1l;

  /** the MULTIPLIER */
  private static final long MULTIPLIER = 0x5DEECE66DL;
  /** the ADDEND */
  private static final long ADDEND = 0xBL;
  /** the MASK */
  private static final long MASK = ((1L << 48l) - 1l);

  /** the random number generator for getting seeds */
  private static final Random SEEDS = new Random();

  /** the seed @serial serializable field */
  private long m_seed;

  /**
   * the next Gaussian random number, if available @serial serializable
   * field
   */
  private double m_nextNextGaussian;

  /**
   * do we have such a Gaussian random number ready? @serial serializable
   * field
   */
  private boolean m_haveNextNextGaussian;

  /**
   * create!
   * 
   * @param seed
   *          the seed to use
   */
  public Randomizer(final long seed) {
    super();
    this.m_seed = Randomizer._initialScramble(seed);
  }

  /** create! */
  public Randomizer() {
    super();
    // Use as seed a random number generated by the original random
    // implementation.
    // This is ok, since the initialization should be random and thus
    // unpredictable and it does not matter if this behavior changes from
    // version to version due to changes in the implementation of the class
    // Random.
    this.m_seed = Randomizer.SEEDS.nextLong();
  }

  /**
   * scramble the initial seed
   * 
   * @param seed
   *          the seed
   * @return the scrambled seed
   */
  private static long _initialScramble(final long seed) {
    return ((seed ^ Randomizer.MULTIPLIER) & Randomizer.MASK);
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("sync-override")
  public final void setSeed(final long seed) {
    this.m_seed = (Randomizer._initialScramble(seed));
    this.m_haveNextNextGaussian = false;
  }

  /** {@inheritDoc} */
  @Override
  protected final int next(final int bits) {
    return (int) ((this.m_seed = (((this.m_seed * Randomizer.MULTIPLIER) + Randomizer.ADDEND) & Randomizer.MASK)) >>> (48 - bits));
  }

  /** {@inheritDoc} */
  @Override
  public final int nextInt(final int n) {
    int bits, val;

    if (n <= 0) {
      super.nextInt(-1);// throw the original exception
    }

    if ((n & (-n)) == n) {
      return (int) ((n * ((long) (this.next(31)))) >> 31);
    }

    do {
      bits = this.next(31);
      val = (bits % n);
    } while (((bits - val) + (n - 1)) < 0);
    return val;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("sync-override")
  @Override
  public final double nextGaussian() {
    double v1, v2, s;
    final double ymultiplier;

    if (this.m_haveNextNextGaussian) {
      this.m_haveNextNextGaussian = false;
      return this.m_nextNextGaussian;
    }

    do {
      v1 = ((2d * this.nextDouble()) - 1d);
      v2 = ((2d * this.nextDouble()) - 1d);
      s = ((v1 * v1) + (v2 * v2));
    } while ((s >= 1d) || (s <= 0d));

    ymultiplier = StrictMath.sqrt((-2d * StrictMath.log(s)) / s);
    this.m_nextNextGaussian = (v2 * ymultiplier);
    this.m_haveNextNextGaussian = true;
    return (v1 * ymultiplier);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code bytes}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code byte}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final byte[] array, final int start,
      final int count) {
    final int n;
    byte t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code bytes}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code byte}s that should be randomized
   */
  public final void shuffle(final byte[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code shorts}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code short}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final short[] array, final int start,
      final int count) {
    final int n;
    short t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code shorts}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code short}s that should be randomized
   */
  public final void shuffle(final short[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code ints}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code int}s whose sub-sequence to be randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final int[] array, final int start,
      final int count) {
    final int n;
    int t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code ints}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code int}s that should be randomized
   */
  public final void shuffle(final int[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code longs}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code long}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final long[] array, final int start,
      final int count) {
    final int n;
    long t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code longs}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code long}s that should be randomized
   */
  public final void shuffle(final long[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code floats}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code float}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final float[] array, final int start,
      final int count) {
    final int n;
    float t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code floats}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code float}s that should be randomized
   */
  public final void shuffle(final float[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code doubles}
   * . After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code double}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final double[] array, final int start,
      final int count) {
    final int n;
    double t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code doubles}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code double}s that should be randomized
   */
  public final void shuffle(final double[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of {@code chars}.
   * After this procedure, the {@code count} elements of the array
   * beginning at index {@code start} are uniformly randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code char}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final char[] array, final int start,
      final int count) {
    final int n;
    char t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code chars}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code char}s that should be randomized
   */
  public final void shuffle(final char[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of
   * {@code booleans}. After this procedure, the {@code count} elements of
   * the array beginning at index {@code start} are uniformly randomly
   * distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code boolean}s whose sub-sequence to be
   *          randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final boolean[] array, final int start,
      final int count) {
    final int n;
    boolean t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code booleans}. After this
   * procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code boolean}s that should be randomized
   */
  public final void shuffle(final boolean[] array) {
    this.shuffle(array, 0, array.length);
  }

  /**
   * <p>
   * <strong>This method had a bug until version 0.9.8 of TSPSuite. It did
   * not generate uniformly distributed permutations. The bug actually is
   * one of the common problems listed <a href=
   * "http://en.wikipedia.org/wiki/Knuth_shuffle#Implementation_errors"
   * >here</a>: The swap index was always picked from the whole
   * range.</strong>
   * </p>
   * <p>
   * Randomize a sub-sequence of an array or permutation of
   * {@code java.lang.Objects}. After this procedure, the {@code count}
   * elements of the array beginning at index {@code start} are uniformly
   * randomly distributed.
   * </p>
   * 
   * @param array
   *          the array of {@code java.lang.Object}s whose sub-sequence to
   *          be randomized
   * @param start
   *          the start index
   * @param count
   *          the number of elements to be randomized
   */
  public final void shuffle(final java.lang.Object[] array,
      final int start, final int count) {
    final int n;
    java.lang.Object t;
    int i, j, k;

    if (count > 0) {
      n = array.length;
      for (i = count; i > 1;) {
        j = ((start + this.nextInt(i--)) % n);
        k = ((start + i) % n);
        t = array[k];
        array[k] = array[j];
        array[j] = t;
      }
    }
  }

  /**
   * Randomize an array or permutation of {@code java.lang.Objects}. After
   * this procedure, the elements of the array are uniformly randomly
   * distributed.
   * 
   * @param array
   *          the array of {@code java.lang.Object}s that should be
   *          randomized
   */
  public final void shuffle(final java.lang.Object[] array) {
    this.shuffle(array, 0, array.length);
  }
}
