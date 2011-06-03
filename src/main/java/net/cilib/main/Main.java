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
package net.cilib.main;

import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.inject.PopulationBasedModule;
import net.cilib.inject.CIlibCoreModule;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.List;
import net.cilib.algorithm.Algorithm;
import net.cilib.simulation.Simulation;
import net.cilib.simulation.SimulationBuilder;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entity;
import net.cilib.entity.Particle;
import net.cilib.measurement.Measurement;
import net.cilib.problem.Problem;
import net.cilib.pso.PSO;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Main {

    private Main() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        List<Predicate<Algorithm>> stoppingConditions = Lists.newArrayList();
        stoppingConditions.add(new Predicate<Algorithm>() {
            private int count = 1000;
            @Override
            public boolean apply(Algorithm input) {
                if (count > 0) {
                    count--;
                    return true;
                }
                return false;
            }
        });

        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());

        // The topology for the algorithm is external. This implies that the topology
        // needs to be constructed ahead of time, so that the algorithm may use it.
        Problem problem = new MockProblem();
        Topology<Entity> topology = createParticleTopology();

        PSO pso = injector.getInstance(PSO.class);

        List<Measurement> measurements = Lists.newArrayList();
        Simulation simulation = injector.getInstance(SimulationBuilder.class)
                .newPopulationBasedSimulation()
                .using(pso)
                .on(problem)
                .initialTopology(topology)
                .measuredBy(measurements)
                .build();

        simulation.execute(stoppingConditions);
    }

    private static Topology<Entity> createParticleTopology() {
        ImmutableGBestTopology.ImmutableGBestTopologyBuilder<Entity> topology = new ImmutableGBestTopology.ImmutableGBestTopologyBuilder<Entity>();
        for (int i = 0; i < 20; i++) {
            topology.add(Particle.create(CandidateSolution.of(0.0, 0.0)));
        }
        return topology.build();
    }
}
