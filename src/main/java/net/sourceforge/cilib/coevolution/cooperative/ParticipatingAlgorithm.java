/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
/**
 * Any algorithm that implements this interface can be used as a participant in any Cooperative
 * Algorithm. Any class that inherits from this inteface has to define a {@linkplain ContributionSelectionStrategy} member variable. This
 * strategy dictates how the solution contribution is selected from the {@linkplain ParticipatingAlgorithm}.
 * @author Edwin Peer
 * @author Theuns Cloete
 * @author leo
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
