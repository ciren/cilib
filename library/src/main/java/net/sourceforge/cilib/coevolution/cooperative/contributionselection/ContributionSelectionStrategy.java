/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface dictates how a participating {@linkplain PopulationBasedAlgorithm} should select its participant solution for a {@linkplain CooperativeCoevolutionAlgorithm}.
 */
public interface ContributionSelectionStrategy extends Cloneable {

    /**
     * Return the relevant participant solution.
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to select the participant from.
     * @return The participating solution {@linkplain Vector}
     */
    Vector getContribution(PopulationBasedAlgorithm algorithm);

    /**
     * {@inheritDoc}
     */
    @Override
    ContributionSelectionStrategy getClone();

}
