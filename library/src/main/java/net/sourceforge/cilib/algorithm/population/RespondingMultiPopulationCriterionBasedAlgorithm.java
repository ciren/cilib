/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.moo.iterationstrategies.HigherLevelArchivingIterationStrategy;

/**
 * <p>
 * Extends {@link MultiPopulationCriterionBasedAlgorithm} so that the top-level algorithm has its
 * own iteration strategy. This enables a higher-level algorithm with sub-populations, such as
 * VEPSO, to control the response to a change in the environment at a higher level and it is
 * not limited to only calling the sub-population's response strategy.
 * </p>
 *
 */
public class RespondingMultiPopulationCriterionBasedAlgorithm extends MultiPopulationCriterionBasedAlgorithm {

    private IterationStrategy<PopulationBasedAlgorithm> iterationStrategy;

    public RespondingMultiPopulationCriterionBasedAlgorithm() {
        this.iterationStrategy = new HigherLevelArchivingIterationStrategy<PopulationBasedAlgorithm>();
    }

    public RespondingMultiPopulationCriterionBasedAlgorithm(RespondingMultiPopulationCriterionBasedAlgorithm copy) {
        super(copy);
        this.iterationStrategy = copy.getIterationStrategy().getClone();
    }

    @Override
    public RespondingMultiPopulationCriterionBasedAlgorithm getClone() {
        return new RespondingMultiPopulationCriterionBasedAlgorithm(this);
    }

    /**
     * Performs an iteration of the algorithm.
     */
    @Override
	protected void algorithmIteration() {
            this.getIterationStrategy().performIteration((PopulationBasedAlgorithm)this);
    }

    /**
     * Returns the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<PopulationBasedAlgorithm> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Sets the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<PopulationBasedAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

}
