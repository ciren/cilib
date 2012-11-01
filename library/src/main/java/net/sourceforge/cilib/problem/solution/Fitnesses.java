/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
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
