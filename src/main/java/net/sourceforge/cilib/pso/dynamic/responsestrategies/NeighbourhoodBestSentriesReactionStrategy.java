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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

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
        RandomProvider random = new MersenneTwister();

        for (Entity entity : Topologies.getNeighbourhoodBestEntities(algorithm.getTopology()))
            entity.getCandidateSolution().randomize(random);
            // TODO: What is the influence of reevaluation?
//            entity.calculateFitness(false);
    }
}
