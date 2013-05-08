/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.dynamic.detectionstrategies.EnvironmentChangeDetectionStrategy;
import net.sourceforge.cilib.pso.dynamic.detectionstrategies.MOORandomSentriesDetectionStrategy;
import net.sourceforge.cilib.pso.dynamic.responsestrategies.ArchiveReevaluationResponseStrategy;
import net.sourceforge.cilib.pso.dynamic.responsestrategies.EnvironmentChangeResponseStrategy;
import net.sourceforge.cilib.pso.dynamic.responsestrategies.PartialReinitialisationResponseStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;

/**
 * Dynamic iteration strategy for PSO in dynamic environments.
 * <p>
 * In each iteration, it checks for an environmental change, but the top-level
 * algorithm, such as VEPSO, handles the response for each sub-swarm.
 */
public class HigherLevelDynamicIterationStrategy<E extends SinglePopulationBasedAlgorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = -4441422301948289718L;
    private IterationStrategy<E> iterationStrategy;
    private EnvironmentChangeDetectionStrategy<SinglePopulationBasedAlgorithm> detectionStrategy;
    private EnvironmentChangeResponseStrategy responseStrategy;
    private EnvironmentChangeResponseStrategy archiveResponseStrategy;

    /**
     * Create a new instance of {@linkplain DynamicIterationStrategy}.
     * <p>
     * The following defaults are set in the constructor:
     * randomiser is instantiated as a MersenneTwister,
     * theta is set to 0.001,
     * reinitialisationRatio is set to 0.5 (reinitialise one half of the swarm)
     */
    public HigherLevelDynamicIterationStrategy() {
        this.iterationStrategy = (IterationStrategy<E>)new SynchronousIterationStrategy();
        this.detectionStrategy = (EnvironmentChangeDetectionStrategy<SinglePopulationBasedAlgorithm>)
        	new MOORandomSentriesDetectionStrategy<SinglePopulationBasedAlgorithm>();
        this.responseStrategy = new PartialReinitialisationResponseStrategy();
        this.archiveResponseStrategy = new ArchiveReevaluationResponseStrategy();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public HigherLevelDynamicIterationStrategy(HigherLevelDynamicIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.responseStrategy = copy.responseStrategy.getClone();
        this.archiveResponseStrategy = copy.archiveResponseStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
//    @Override
    @Override
    public HigherLevelDynamicIterationStrategy getClone() {
        return new HigherLevelDynamicIterationStrategy(this);
    }

    /**
     * Structure of Dynamic iteration strategy with re-initialisation:
     *
     * <ol>
     *   <li>Check for environment change</li>
     *   <li>If the environment has changed:</li>
     *   <ol>
     *     <li>Respond to change</li>
     *   <ol>
     *   <li>Perform normal iteration</li>
     * </ol>
     */
    @Override
    public void performIteration(E algorithm) {
        boolean hasChanged = detectionStrategy.detect(algorithm);

        if (hasChanged)
            responseStrategy.respond(algorithm);

        iterationStrategy.performIteration(algorithm);
    }

    /**
     * Get the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<E> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<E> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeDetectionStrategy}.
     * @return The current {@linkplain EnvironmentChangeDetectionStrategy}.
     */
    public EnvironmentChangeDetectionStrategy<SinglePopulationBasedAlgorithm> getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain EnvironmentChangeDetectionStrategy} to be used.
     * @param detectionStrategy The {@linkplain EnvironmentChangeDetectionStrategy} to set.
     */
    public void setDetectionStrategy(EnvironmentChangeDetectionStrategy<SinglePopulationBasedAlgorithm> detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeResponseStrategy},
     * @return The current {@linkplain EnvironmentChangeResponseStrategy}.
     */
    public EnvironmentChangeResponseStrategy getResponseStrategy() {
        return responseStrategy;
    }

    /**
     * Set the current {@linkplain EnvironmentChangeResponseStrategy} to use.
     * @param responseStrategy The {@linkplain EnvironmentChangeResponseStrategy} to set.
     */
    public void setResponseStrategy(EnvironmentChangeResponseStrategy responseStrategy) {
        this.responseStrategy = responseStrategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeResponseStrategy}
     * for the archive.
     * @return The current {@linkplain EnvironmentChangeResponseStrategy}.
     */
    public EnvironmentChangeResponseStrategy getArchiveResponseStrategy() {
        return this.archiveResponseStrategy;
    }

    /**
     * Set the current {@linkplain EnvironmentChangeResponseStrategy} to use for the archive.
     * @param responseStrategy The {@linkplain EnvironmentChangeResponseStrategy} to set.
     */
    public void setArchiveResponseStrategy(EnvironmentChangeResponseStrategy responseStrategy) {
        this.archiveResponseStrategy = responseStrategy;
    }

}
