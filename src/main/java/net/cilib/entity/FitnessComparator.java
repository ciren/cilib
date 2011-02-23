/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.entity;

import fj.data.Option;

/**
 * General comparator for fitness comparisons.
 */
public abstract class FitnessComparator {

    /**
     * Define the core logic for comparison of given fitness values.
     * @param a the first fitness as an {@code Option}.
     * @param b the second fitness as an {@code Option}.
     * @return {@code true} if {@code a {@literal <} b},
     *         {@code false} otherwise.
     * @throws UnsupportedOperationException when both paramters are
     *         {@linkplain Option#none() none}.
     */
    protected abstract boolean compare(Option<Double> a, Option<Double> b);

    /**
     * Question whether the first parameter is less fit than the second.
     * @param <A> the type extending {@code HasFitness}.
     * @param a first fitness.
     * @param b second fitness.
     * @return {@code true} if {@code a} is less fit than {@code b},
     *         {@code false} otherwise.
     */
    public <A extends HasFitness> boolean isLessFit(A a, A b) {
        return compare(a.fitness(), b.fitness());
    }

    /**
     * Obtain the less fit instance between the provided instances.
     * @param <A> the type extending {@code HasFitness}.
     * @param a first fitness.
     * @param b second fitness.
     * @return {@code a} iff {@code a} is less fit, {@code b} otherwise.
     */
    public <A extends HasFitness> A lessFit(A a, A b) {
        return isLessFit(a, b) ? a : b;
    }

    /**
     * Question whether the first parameter is more fit than the second.
     * @param <A> the type extending {@code HasFitness}.
     * @param a first fitness.
     * @param b second fitness.
     * @return {@code true} if {@code a} is more fit than {@code b},
     *         {@code false} otherwise.
     */
    public <A extends HasFitness> boolean isMoreFit(A a, A b) {
        return compare(a.fitness(), b.fitness());
    }

    /**
     * Obtain the more fit instance between the provided instances.
     * @param <A> the type extending {@code HasFitness}.
     * @param a first fitness.
     * @param b second fitness.
     * @return {@code a} iff {@code a} is more fit, {@code b} otherwise.
     */
    public <A extends HasFitness> A moreFit(A a, A b) {
        return isMoreFit(a, b) ? a : b;
    }

    /**
     * Comparator determining the more fit instance, for <b>maximization</b>.
     */
    public static final class MaxFitnessComparator extends FitnessComparator {

        /**
         * Determine which {@code Option} provides a larger value.
         * {@inheritDoc}
         */
        @Override
        protected boolean compare(Option<Double> a, Option<Double> b) {
            if (a.isNone() && b.isNone()) {
                throw new UnsupportedOperationException();
            }

            return a.isNone() ? false
                    : b.isNone() ? true
                    : a.some() > b.some();
        }
    }

    /**
     * Comparator determining the more fit instance, for <b>minimization</b>.
     */
    public static final class MinFitnessComparator extends FitnessComparator {

        /**
         * Determine which {@code Option} provides a smaller value.
         * {@inheritDoc}
         */
        @Override
        protected boolean compare(Option<Double> a, Option<Double> b) {
            if (a.isNone() && b.isNone()) {
                throw new UnsupportedOperationException();
            }

            return a.isNone() ? false
                    : b.isNone() ? true
                    : a.some() < b.some();
        }
    }
}
