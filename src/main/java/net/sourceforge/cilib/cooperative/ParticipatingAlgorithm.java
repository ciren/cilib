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
package net.sourceforge.cilib.cooperative;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;

/**
 * Any algorithm that implements this interface can be used as a participating algorithm
 * within a Cooperative Algorithm.
 * <p>
 * <b>Note:</b>
 * Currently this interface only supports continuous domained problems.
 * @author Edwin Peer
 * @author Theuns Cloete
 */
public interface ParticipatingAlgorithm {
    /**
     * Returns contribution to the solution for the co-operative optimisation algorithm.
     * The contribution is defined as the most fit entity within the current participating
     * algorithm.
     * @return The algorithm's solution contribution, also known as the current best solution
     *         from the algorithm.
     */
    public Entity getContribution();

    /**
     * Obtain the fitness value of the best ssolution within the current sub-swarm of
     * this participating algorithm within a co-operative algorithm.
     * @return The fitness of the solution contribution for the current participating
     *         algorithm.
     */
    public Fitness getContributionFitness();

    /**
     * Updates the new fitness for the solution contribution of the current participating
     * algorithm for the cooperative algorithm maintaining the current algorithm.
     * @param fitness The new fitness of the contribution.
     */
    public void updateContributionFitness(Fitness fitness);
}
