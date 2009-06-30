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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;

public class NeighbourhoodBestSentriesReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -2142727048293776335L;

    public NeighbourhoodBestSentriesReactionStrategy() {
        // super() is called automatically
    }

    public NeighbourhoodBestSentriesReactionStrategy(NeighbourhoodBestSentriesReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesReactionStrategy<E> getClone() {
        return new NeighbourhoodBestSentriesReactionStrategy<E>(this);
    }

    @Override
    public void performReaction(PopulationBasedAlgorithm algorithm) {
        Random random = new MersenneTwister();

        for (Entity entity : Topologies.getNeighbourhoodBestEntities(algorithm.getTopology()))
            entity.getCandidateSolution().randomize(random);
            // TODO: What is the influence of reevaluation?
//            entity.calculateFitness(false);
    }
}
