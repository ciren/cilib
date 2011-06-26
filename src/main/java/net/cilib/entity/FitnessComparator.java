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

import java.util.Comparator;

/**
 * General comparator for fitness comparisons.
 */
public enum FitnessComparator implements Comparator<Option<Double>> {

    /**
     * Comparator designed to obtain the "least fit" of two instances that
     * maintain a fitness.
     */
    MIN {
        /**
         * {@inheritDoc}
         */
        @Override
        public <A extends HasFitness> boolean isLessFit(A a, A b) {
            return isLessFit(a.fitness(), b.fitness());
        }

        @Override
        public boolean isLessFit(Option<Double> a, Option<Double> b) {
            return compare(a, b) > 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <A extends HasFitness> boolean isMoreFit(A a, A b) {
            return isMoreFit(a.fitness(), b.fitness());
        }

        @Override
        public boolean isMoreFit(Option<Double> a, Option<Double> b) {
            return compare(a, b) < 0;
        }

        @Override
        public int compare(Option<Double> o1, Option<Double> o2) {
            if (o1.isNone() && o2.isNone()) {
                throw new UnsupportedOperationException("Cannot compare fitnesses: " + o1 + " " + o2);
            }

            return o1.isNone() ? 1 // o2 is better
                    : o2.isNone() ? -1 // o1 is better
                    : o1.some().compareTo(o2.some());
        }
    },
    /**
     * Comparator designed to obtain the "most fit" of two instances that
     * maintain a fitness.
     */
    MAX {
        /**
         * {@inheritDoc}
         */
        @Override
        public <A extends HasFitness> boolean isLessFit(A a, A b) {
            return isLessFit(a.fitness(), b.fitness());
        }

        @Override
        public boolean isLessFit(Option<Double> a, Option<Double> b) {
            return compare(a, b) > 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <A extends HasFitness> boolean isMoreFit(A a, A b) {
            return isMoreFit(a.fitness(), b.fitness());
        }

        @Override
        public boolean isMoreFit(Option<Double> a, Option<Double> b) {
            return compare(a, b) < 0;
        }

        @Override
        public int compare(Option<Double> o1, Option<Double> o2) {
            if (o1.isNone() && o2.isNone()) {
                throw new UnsupportedOperationException("Cannot compare fitnesses: " + o1 + " " + o2);
            }

            return o1.isNone() ? 1 // o2 is better
                    : o2.isNone() ? -1 // o1 is better
                    : o2.some().compareTo(o1.some());
        }
    };

    @Override
    public abstract int compare(Option<Double> o1, Option<Double> o2);

    /**
     * Question whether the first parameter is less fit than the second.
     *
     * @param <A> the type extending {@code HasFitness}.
     * @param a   first fitness.
     * @param b   second fitness.
     * @return {@code true} if {@code a} is less fit than {@code b},
     *         {@code false} otherwise.
     */
    public abstract <A extends HasFitness> boolean isLessFit(A a, A b);

    public abstract boolean isLessFit(Option<Double> a, Option<Double> b);

    /**
     * Obtain the less fit instance between the provided instances.
     *
     * @param <A> the type extending {@code HasFitness}.
     * @param a   first fitness.
     * @param b   second fitness.
     * @return {@code a} if, and only if, {@code a} is less fit,
     *         {@code b} otherwise.
     */
    public <A extends HasFitness> A lessFit(A a, A b) {
        return isLessFit(a, b) ? a : b;
    }

    /**
     * Question whether the first parameter is more fit than the second.
     *
     * @param <A> the type extending {@code HasFitness}.
     * @param a   first fitness.
     * @param b   second fitness.
     * @return {@code true} if {@code a} is more fit than {@code b},
     *         {@code false} otherwise.
     */
    public abstract <A extends HasFitness> boolean isMoreFit(A a, A b);

    public abstract boolean isMoreFit(Option<Double> a, Option<Double> b);

    /**
     * Obtain the more fit instance beObjecttween the provided instances.
     *
     * @param <A> the type extending {@code HasFitness}.
     * @param a   first fitness.
     * @param b   second fitness.
     * @return {@code a} if, and only if, {@code a} is more fit,
     *         {@code b} otherwise.
     */
    public <A extends HasFitness> A moreFit(A a, A b) {
        return isMoreFit(a, b) ? a : b;
    }
}
