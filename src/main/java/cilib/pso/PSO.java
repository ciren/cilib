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
package cilib.pso;

import cilib.algorithm.PopulationBasedAlgorithm;
import cilib.annotation.Initialized;
import com.google.inject.Inject;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;

/**
 *
 * @author gpampara
 */
public class PSO implements PopulationBasedAlgorithm<Particle> {

    private final Topology<Particle> topology;
    private final IterationStrategy iterationStrategy;

    @Inject
//    public PSO(@Initialized Topology<Particle> topology,
//            IterationStrategy iterationStrategy) {
    public PSO(@Initialized Topology topology,
            IterationStrategy iterationStrategy) {
        this.topology = topology;
        this.iterationStrategy = iterationStrategy;
        System.out.println("topology: " + topology);
    }

    @Override
    public Topology<Particle> getTopology() {
        return topology;
    }

    @Override
    public void performIteration() {
        System.out.println("performing iteration");
//        iterationStrategy.performIteration(null);
    }
}
