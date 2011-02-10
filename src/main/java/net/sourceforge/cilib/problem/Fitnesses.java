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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Wiehann Matthysen
 */
public final class Fitnesses {

    private Fitnesses() {
    }

    public static Vector vectorOf(MOFitness moFitness) {
        Vector.Builder builder = Vector.newBuilder();
        for (Fitness fitness : moFitness) {
            builder.add(Real.valueOf(fitness.getValue()));
        }
        return builder.build();
    }

    public static MOFitness create(MOOptimisationProblem problem, Type solution) {
        int size = problem.size();
        Fitness[] fitnesses = new Fitness[size];
        for (int i = 0; i < size; ++i) {
            fitnesses[i] = problem.getFitness(i, solution);
        }
        return new StandardMOFitness(fitnesses);
    }

    public static MOFitness create(MOOptimisationProblem problem, Type[] solutions) {
        int size = problem.size();
        Fitness[] fitnesses = new Fitness[size];
        for (int i = 0; i < size; ++i) {
            fitnesses[i] = problem.getFitness(i, solutions[i]);
        }
        return new StandardMOFitness(fitnesses);
    }

    public static MOFitness create(Fitness... fitnesses) {
        return new StandardMOFitness(fitnesses);
    }
}
