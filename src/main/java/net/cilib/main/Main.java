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

import net.cilib.inject.PopulationBasedModule;
import net.cilib.inject.CIlibCoreModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import fj.Monoid;
import fj.P1;
import fj.Show;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entities;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Particle;
import net.cilib.matchers.EntityMatchers;
import net.cilib.problem.Benchmarks;
import net.cilib.problem.Evaluatable;
import net.cilib.pso.ASyncPSO;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Main {

    private Main() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new CIlibCoreModule(Evaluatable.lift(Benchmarks.square, Monoid.doubleAdditionMonoid)),
                new PopulationBasedModule());

        // The topology for the algorithm is external. This implies that the topology
        // needs to be constructed ahead of time, so that the algorithm may use it.
        final Topology<Particle> topology = createParticleTopology();

        ASyncPSO pso = injector.getInstance(ASyncPSO.class);
        Topology<Particle> next = topology;
        for (int i = 0; i < 1000; i++) {
            next = pso.next(next);
        }

        Particle p = EntityMatchers.mostFit(next, FitnessComparator.MIN);

        Show.listShow(Show.doubleShow).println(p.solution());
        Show.listShow(Show.doubleShow).println(p.memory());
        Show.listShow(Show.doubleShow).println(p.velocity());
        Show.optionShow(Show.doubleShow).println(p.fitness());
    }

    private static Topology<Particle> createParticleTopology() {
        final P1<Particle> generator = Entities.particleGen(20, new MersenneTwister());
        final ImmutableGBestTopology.ImmutableGBestTopologyBuffer<Particle> topology = new ImmutableGBestTopology.ImmutableGBestTopologyBuffer<Particle>();
        for (int i = 0; i < 20; i++) {
            topology.add(generator._1());
        }
        return topology.build();
    }
}
