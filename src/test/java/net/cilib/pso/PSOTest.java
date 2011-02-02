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
package net.cilib.pso;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.simulation.Simulation;
import net.cilib.simulation.SimulationBuilder;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.CandidateSolution;
import net.cilib.entity.Fitnesses;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;
import net.cilib.inject.CIlibCoreModule;
import net.cilib.inject.PopulationBasedModule;
import net.cilib.main.MockProblem;
import net.cilib.measurement.Measurement;
import org.junit.Test;

/**
 * @author gpampara
 */
public class PSOTest {

    @Test
    public void iteration() {
        Topology<Particle> topology = ImmutableGBestTopology.of();
        VelocityProvider velocityProvider = new StandardVelocityProvider(null, null, null, null, null, null);
        PopulationBasedAlgorithm<Particle> instance = new PSO(velocityProvider, null);

        instance.iterate(topology);
    }

    /**
     * Integration test
     */
    @Test
    public void integration() {
        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());

        Particle p1 = new Particle(CandidateSolution.of(1.0, 1.0), CandidateSolution.of(1.0, 1.0), Velocity.copyOf(0.0, 0.0), Fitnesses.inferior());
        Particle p2 = new Particle(CandidateSolution.of(1.0, 1.0), CandidateSolution.of(1.0, 1.0), Velocity.copyOf(0.0, 0.0), Fitnesses.inferior());
        Topology initial = ImmutableGBestTopology.newBuilder().add(p1).add(p2).build();

        PSO pso = injector.getInstance(PSO.class);

        Simulation simulation = injector.getInstance(SimulationBuilder.class)
                .newPopulationBasedSimulation()
                .using(pso)
                .initialTopology(initial)
                .on(new MockProblem())
                .measuredBy(Lists.<Measurement>newArrayList())
                .build();

        Predicate<Algorithm> predicate = new Predicate<Algorithm>() {
            int count = 5;

            @Override
            public boolean apply(Algorithm input) {
                if (count > 0) {
                    count--;
                    return true;
                }
                return false;
            }
        };

        simulation.execute(Lists.newArrayList(predicate));
    }
}
