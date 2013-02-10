/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import static com.google.common.base.Preconditions.checkArgument;
import fj.F;
import static fj.data.List.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Numerics;


/**
 * This class provides helper functions in addition to the standard <code>java.lang.Math</code>
 * class.
 *
 * These utility functions further are necessary for the various distributions and selections
 * required within CIlib as a whole.
 *
 */
public final class Maths {

    public static final double EPSILON = 1.0E-15d;

    private Maths() {
    }

    /**
     * Generate the required factorial of the number <code>x</code>.
     * @param x The number to generate the factorial from.
     * @return The factorial of <code>x</code>.
     */
    public static int factorial(int x) {
        checkArgument(x >= 0, "x must be a positive integer");
        checkArgument(x < 20, "The size of the factorial will result in a overflow. Please consider using Maths.largeFactorial() instead");

        if (x == 0 || x == 1) {
            return 1;
        }

        return x * factorial(x-1);
    }

    /**
     * Generate the required factorial of {@code x}. Additionally, this
     * method is not affected by data type overflow.
     * @param x The value to calculate the factorial of.
     * @return A {@link BigInteger} representing the factorial.
     */
    public static BigInteger largeFactorial(int x) {
        checkArgument(x >= 0, "x must be a positive integer");
        BigInteger n = BigInteger.ONE;

        for (int i = 1; i < x; i++) {
            n = n.multiply(BigInteger.valueOf(i));
        }

        return n;
    }

    /**
     * Return the combination of <code>n</code> and <code>r</code>.
     * @param n The total elements from which the combination is performed.
     * @param r The {@code r}-combinations (of size {@code r}) to select.
     * @return The combination of <code>n</code> and <code>r</code>.
     */
    public static int combination(int n, int r) {
        checkArgument(n >= r, "Combination violation: n >= r");
        checkArgument(r >= 0, "Combination violation: r >= 0");
        return permutation(n, r) / factorial(r);
    }

    /**
     * This is a convenience method providing an alias to <code>combination</code>.
     * @param n The number of elements available for selection.
     * @param r The {@code r}-combinations (of size {@code r}) to select.
     * @return The value of the operation "<code>n</code> choose <code>x</code>".
     */
    public static double choose(int n, int r) {
        return combination(n, r);
    }

    /**
     * In combinatorics, a permutation is usually understood to be a sequence
     * containing each element from a finite set once, and only once.
     * @param n The number of elements available for selection.
     * @param r The number of elements to be selected {@code (0 <= r <= n)}.
     * @return The number of permutations.
     */
    public static int permutation(int n, int r) {
        checkArgument(n >= r, "Permutations violation: n >= r");
        checkArgument(r >= 0, "Permutations violation: r >= 0");
        return factorial(n) / factorial(n-r);
    }

    public static <T> Iterator<List<T>> permutation(final List<T> input, final int number) {
        return new Iterator<List<T>>() {
            private List<T> internalList = new ArrayList<T>(input); // Keep our own copy
            private int n = input.size();
            private int m = number;
            private int[] index = initialise();
            private boolean hasMore = true;

            @Override
            public boolean hasNext() {
                return this.hasMore;
            }

            @Override
            public List<T> next() {
                if (!this.hasMore){
                    return null;
                }
                List<T> list =  new ArrayList<T>(this.m);
                for (int i = 0; i < this.m; i++) {
                    int thisIndexI = this.index[i];
                    T element = internalList.get(thisIndexI);
                    list.add(element);
                }
                moveIndex();
                return list;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            private int[] initialise() {
                if (!(this.n >= m && m >= 0)) {
                    throw new IllegalStateException("Permutation error! n >= m");
                }

                int[] tmp = new int[this.n];
                for (int i = 0; i < this.n; i++) {
                    tmp[i] = i;
                }

                reverseAfter(tmp, m - 1);
                return tmp;
            }

            /**
             * Reverse the index elements to the right of the specified index.
             */
            private void reverseAfter(int[] indicies, int i) {
                int start = i + 1;
                int end = this.n - 1;
                while (start < end) {
                    int t = indicies[start];
                    indicies[start] = indicies[end];
                    indicies[end] = t;
                    start++;
                    end--;
                }
            }

            private void moveIndex(){
                // find the index of the first element that dips
                int i = rightmostDip();
                if (i < 0) {
                    this.hasMore = false;
                    return;
                }

                // find the least greater element to the right of the dip
                int leastToRightIndex = i + 1;
                for (int j = i + 2; j < this.n; j++){
                    if (this.index[j] < this.index[leastToRightIndex] &&  this.index[j] > this.index[i]) {
                        leastToRightIndex = j;
                    }
                }

                // switch dip element with least greater element to its right
                int t = this.index[i];
                this.index[i] = this.index[leastToRightIndex];
                this.index[leastToRightIndex] = t;

                if (this.m - 1 > i) {
                    // reverse the elements to the right of the dip
                    reverseAfter(this.index, i);
                    // reverse the elements to the right of m - 1
                    reverseAfter(this.index, this.m - 1);
                }
            }

            private int rightmostDip() {
                for (int i = this.n - 2; i >= 0; i--){
                    if (this.index[i] < this.index[i+1]){
                        return i;
                    }
                }
                return -1;
            }
        };
    }

    /**
     * Determine if a "flip" would occur given the provided probability value.
     * @param probability The provided probability value. This value must be in [0,1]
     * @return 1 - if a "flip" occurred, 0 otherwise.
     */
    public static int flip(double probability) {
        checkArgument(probability >= 0 && probability <= 1, "Illegal input: valid range is [0,1]");

        if (Rand.nextDouble() <= probability) {
            return 1;
        }

        return 0;
    }

    /**
     * Determine the log of the specified <code>value</code> with the provided <code>base</code>.
     * @param base The base of the log operation.
     * @param value The value to determine the log of.
     * @return The log value of <code>value</code> using the base value of <code>base</code>.
     * @see java.lang.Math#log(double).
     */
    public static double log(double base, double value) {
        return Math.log(value) / Math.log(base);
    }

    private static fj.data.List<Vector> combinations(final fj.data.List<Vector> input, final int i) {
        if (i == input.length()) {
            return list(Vector.of());
        }

        final fj.data.List<Vector> recursive = combinations(input, i + 1);
        final Vector current = input.index(i);

        return fj.data.List.join(iterableList(current).map(Numerics.doubleValue())
            .map(new F<Double, fj.data.List<Vector>>() {
                @Override
                public fj.data.List<Vector> f(final Double a) {
                    return recursive.map(new F<Vector, Vector>() {
                        @Override
                        public Vector f(Vector b) {
                            return Vector.newBuilder().copyOf(b).add(a).build();
                        }
                    });
                }
            }));
    }

    public static fj.data.List<Vector> combinations(final fj.data.List<Vector> input) {
        return combinations(input, 0);
    }

}
