/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.solution.MOFitness;

/**
 * Compare two {@link SocialEntity} instances, based on the available social best
 * fitness. The solutions are compared using Pareto-dominance.
 * @param <E> The {@code SocialEntity} type.
 *
 */
public class DominantFitnessComparator <E extends SocialEntity> implements Comparator<E>, Serializable  {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        MOOptimisationProblem problem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        Particle p1 = (Particle)o1;
        Particle p2 = (Particle)o2;
        MOFitness fitness1 = ((MOFitness)problem.getFitness(p1.getBestPosition()));
        MOFitness fitness2 = ((MOFitness)problem.getFitness(p2.getBestPosition()));

        return fitness1.compareTo(fitness2);
    }
}


