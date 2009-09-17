/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;

/**
 * This class provides helper functions in addtion to the standard <code>java.lang.Math</code>
 * class.
 *
 * These utility functions further are necessary for the various distributions and selections
 * required within CIlib as a whole.
 *
 * @author Gary Pampara
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
    public static double factorial(double x) {
        if (x < 0)
            throw new IllegalArgumentException("Factorial is defined to work on numbers >= 0 only");

        if (x == 0.0)
            return 1.0;
        else if (x == 1.0)
            return 1.0;
        else
            return x * factorial(x-1);
    }

    /**
     * Return the combination of <code>n</code> and <code>r</code>.
     * @param n The total elements from which the combination is perfromed.
     * @param r The {@code r}-combinations (of size {@code r}) to select.
     * @return The combination of <code>n</code> and <code>r</code>.
     */
    public static double combination(double n, double r) {
        if (n < r)
            throw new IllegalArgumentException("In a combination the following must hold: n >= x");

        return permutation(n, r) / factorial(r);
    }

    /**
     * This is a convienience method providing an alias to <code>combination</code>.
     * @param n The number of elements available for selection.
     * @param r The {@code r}-combinations (of size {@code r}) to select.
     * @return The value of the operation "<code>n</code> choose <code>x</code>".
     */
    public static double choose(double n, double r) {
        return combination(n, r);
    }

    /**
     * In combinatorics, a permutation is usually understood to be a sequence
     * containing each element from a finite set once, and only once.
     * @param n The number of elements available for selection.
     * @param r The number of elements to be selected {@code (0 <= r <= n)}.
     * @return The number of permutations.
     */
    public static double permutation(double n, double r) {
        return factorial(n) / factorial(n-r);
    }

    public static <T> Iterator<List<T>> permutation(final List<T> input, final int number) {
        return new Iterator<List<T>>() {
            private List<T> internalList = new ArrayList<T>(input); // Keep our own copy
            private int n = input.size();
            private int m = number;
            private int[] index = initialize();
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

            private int[] initialize() {
                if (!(this.n >= m && m >= 0))
                    throw new IllegalStateException("Permutation error! n >= m");

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
     * @return 1 - if a "flip" occured, 0 otherwise.
     */
    public static int flip(double probability) {
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Illegal input: valid range is [0,1]");

        Random randomNumber = new MersenneTwister();
        if (randomNumber.nextDouble() <= probability)
            return 1;

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

}
