/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
/**
 * Any algorithm that implements this interface can be used as a participant in any Cooperative
 * Algorithm. Any class that inherits from this interface has to define a {@linkplain ContributionSelectionStrategy} member variable. This
 * strategy dictates how the solution contribution is selected from the {@linkplain ParticipatingAlgorithm}.
 */
public interface ParticipatingAlgorithm {

    /**
     * Return the {@linkplain ParticipatingAlgorithm}'s {@linkplain ContributionSelectionStrategy}.
     * @return The current instance of the {@linkplain ContributionSelectionStrategy}.
     */
    ContributionSelectionStrategy getContributionSelectionStrategy();

    /**
     * Change the current instance of the {@linkplain ParticipatingAlgorithm}'s {@linkplain ContributionSelectionStrategy}.
     * @param strategy The new instance of the {@linkplain ContributionSelectionStrategy}.
     */
    void setContributionSelectionStrategy(ContributionSelectionStrategy strategy);
}
