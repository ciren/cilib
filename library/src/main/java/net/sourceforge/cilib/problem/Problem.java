/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Optimisation problems are characterized by a domain that specifies the search
 * space and a fitness given a potential solution. This interface ensures that
 * an {@linkplain net.sourceforge.cilib.algorithm.Algorithm} has all
 * the information it needs to find a solution to a given optimisation problem.
 * In addition, it is the responsibility of an optimisation problem to keep
 * track of the number of times the fitness has been evaluated.
 * <p>
 * All optimisation problems must implement this interface.
 */
public interface Problem extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Problem getClone();

    /**
     * Returns the fitness of a potential solution to this problem. The solution
     * object is described by the domain of this problem, see
     * {@link #getDomain()}. An instance of
     * {@link net.sourceforge.cilib.problem.solution.InferiorFitness} should be
     * returned if the solution falls outside the search space of this problem.
     *
     * @param solution  the potential solution found by the optimisation algorithm.
     * @return          the fitness of the solution.
     */
    Fitness getFitness(Type solution);

    /**
     * Returns the number of times the underlying fitness function has been
     * evaluated.
     *
     * @return The number fitness evaluations.
     */
    int getFitnessEvaluations();

    /**
     * Returns the domain component that describes the search space for this
     * problem.
     *
     * @return  a {@link net.sourceforge.cilib.type.DomainRegistry} object
     *          representing the search space.
     */
    DomainRegistry getDomain();

    void setDomain(String domain);

}
