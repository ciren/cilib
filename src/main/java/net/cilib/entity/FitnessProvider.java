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

import com.google.inject.Inject;
import fj.data.Option;
import net.cilib.problem.Problem;

/**
 * Factory instance to calculate the fitness, given a {@code CandidateSolution}.
 * @author gpampara
 */
public class FitnessProvider {

    private final Problem problem;

    @Inject
    public FitnessProvider(Problem problem) {
        this.problem = problem;
    }

    /**
     * Calculate the fitness for the given {@code CandidateSolution}.
     * @param solution {@code CandidateSolution} to evaluate.
     * @return the fitness of the given {@code CandidateSolution}.
     */
    public Option<Double> finalize(CandidateSolution solution) {
        try {
            double acc = 0.0;
            SeqIterator iter = solution.iterator();
            while (iter.hasNext()) {
                acc += problem.f(iter.next());
            }
            return Option.some(acc);
        } catch (Exception e) {
            return Option.<Double>none();
        }
    }
}
