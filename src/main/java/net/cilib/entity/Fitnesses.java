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

/**
 *
 * @author gpampara
 */
public class Fitnesses {

    private static final Fitness INFERIOR_FITNESS = new Fitness() {
        @Override
        public int compareTo(Fitness o) {
            return 1; // An inferior fitness is ALWAYS worse, therefore return the other one
        }

        @Override
        public double value() {
            return Double.NaN; // Value doesn't exist
        }
    };

    private Fitnesses() {
    }

    /**
     * Get an inferior fitness. This fitness value will always be
     * "worse" than anything other fitness value.
     * @return an inferior fitness value.
     */
    public static Fitness inferior() {
        return INFERIOR_FITNESS;
    }

    /**
     * Create a new fitness value, that should be maximized.
     * @param value the value of the fitness
     * @return the maximization fitness of the given value.
     */
    public static Fitness newMaximizationFitness(double value) {
        return new MaximizationFitness(value);
    }

    /**
     * Create a new fitness value, that should be minimized.
     * @param value the value of the fitness
     * @return the minimization fitness of the given value.
     */
    public static Fitness newMinimizationFitness(double value) {
        return new MinimizationFitness(value);
    }

    private static class MaximizationFitness implements Fitness {
        private final double value;

        MaximizationFitness(double value) {
            this.value = value;
        }

        @Override
        public int compareTo(Fitness o) {
            return (o == INFERIOR_FITNESS) ? -1 : Double.compare(value, o.value());
        }

        @Override
        public double value() {
            return value;
        }
    }

    private static class MinimizationFitness implements Fitness {
        private final double value;

        MinimizationFitness(double value) {
            this.value = value;
        }

        @Override
        public int compareTo(Fitness o) {
            return (o == INFERIOR_FITNESS) ? -1 : -Double.compare(value, o.value());
        }

        @Override
        public double value() {
            return value;
        }
    }
}
