/*
 * Copyright (C) 2003 - 2008
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

    public static double EPSILON = 1.0E-15d;

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

    /**
     * Determine if a "flip" would occur given the provided probability value.
     * @param probability The provided probability value. This value must be in [0,1]
     * @return 1 - if a "flip" occured, 0 otherwise.
     */
    public static int flip(double probability) {
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Illegal input: valid range is [0,1]");

        Random random = new MersenneTwister();
        if (random.nextDouble() <= probability)
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
