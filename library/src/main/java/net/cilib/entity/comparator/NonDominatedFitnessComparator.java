/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.pso.particle.Particle;
import net.cilib.entity.SocialEntity;
import net.cilib.problem.MOOptimisationProblem;
import net.cilib.problem.solution.MOFitness;

/**
 * Compare two {@link SocialEntity} instances, based on the available social best
 * fitness. Two solutions are compared using Pareto-dominance.
 * @param <E> The {@code SocialEntity} type.
 *
 */
public class NonDominatedFitnessComparator <E extends SocialEntity> implements Comparator<E>, Serializable  {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        MOOptimisationProblem problem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        Particle p1 = (Particle)o1;
        Particle p2 = (Particle)o2;
        MOFitness fitness1 = ((MOFitness)problem.getFitness(p1.getBestPosition()));
        MOFitness fitness2 = ((MOFitness)problem.getFitness(p2.getBestPosition()));

        int value = fitness1.compareTo(fitness2);
        if (value == 0) {
            value = 1;
        }
        return value;
    }
}
