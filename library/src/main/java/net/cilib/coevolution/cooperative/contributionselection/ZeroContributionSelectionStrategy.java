/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.coevolution.cooperative.contributionselection;

import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.type.types.container.Vector;

/**
 *
 */
public class ZeroContributionSelectionStrategy implements ContributionSelectionStrategy {

    @Override
    public Vector getContribution(SinglePopulationBasedAlgorithm algorithm) {
        throw new UnsupportedOperationException("Cannot obtain a contribution from a ZeroContributionSelectionStrategy." +
            "\nPlease specify the correct contribution strategy to use.");
    }

    @Override
    public ContributionSelectionStrategy getClone() {
        return this;
    }

}
