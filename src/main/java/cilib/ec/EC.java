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
package cilib.ec;

import cilib.algorithm.PopulationBasedAlgorithm;
import cilib.annotation.Initialized;
import com.google.inject.Inject;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;

/**
 *
 * @author gpampara
 */
public class EC implements PopulationBasedAlgorithm<Individual> {

    private final Topology<Individual> topology;
    private final IterationStrategy iterationStrategy;

    @Inject
    public EC(@Initialized Topology<Individual> topology, IterationStrategy iterationStrategy) {
        this.topology = topology;
        this.iterationStrategy = iterationStrategy;
    }

    @Override
    public Topology<Individual> getTopology() {
        return this.topology;
    }

    @Override
    public void performIteration() {
        iterationStrategy.performIteration(null);
    }
}
