/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Select the contribution based on the topology best entity of the {@linkplain PopulationBasedAlgorithm}.
 */
public class TopologyBestContributionSelectionStrategy implements
        ContributionSelectionStrategy {
    private static final long serialVersionUID = -3129164721354568798L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getContribution(PopulationBasedAlgorithm algorithm) {
        return (Vector) Topologies.getBestEntity(algorithm.getTopology()).getCandidateSolution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopologyBestContributionSelectionStrategy getClone() {
        return new TopologyBestContributionSelectionStrategy();
    }
}
