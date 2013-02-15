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
 * @author Marde Greeff
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
