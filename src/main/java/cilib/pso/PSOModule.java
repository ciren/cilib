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

import cilib.annotation.Initialized;
import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 *
 * @author gpampara
 */
public class PSOModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Topology.class).to(GBestTopology.class);
        bind(Topology.class).annotatedWith(Initialized.class).toProvider(InitializedTopologyProvider.class);
//        bind(new TypeLiteral<Topology<Particle>>(){}).to(new TypeLiteral<GBestTopology<Particle>>(){});
//        bind(new TypeLiteral<Topology<Particle>>(){}).annotatedWith(Initialized.class).toProvider(InitializedTopologyProvider.class);
        bind(IterationStrategy.class).to(SynchronousIterationStrategy.class);
        bind(new TypeLiteral<PopulationInitialisationStrategy<Particle>>(){}).to(new TypeLiteral<ClonedPopulationInitialisationStrategy<Particle>>(){});
    }

    private static class InitializedTopologyProvider implements Provider<Topology<Particle>> {

        @Inject private Provider<Topology> topology;
        @Inject private PopulationInitialisationStrategy<Particle> strategy;

        @Override
        public Topology<Particle> get() {
            strategy.setEntityType(new StandardParticle());
            FunctionMinimisationProblem f = new FunctionMinimisationProblem();
            f.setFunction(new Spherical());

            Topology<Particle> t = topology.get();
            Iterables.addAll(t, strategy.initialise(f));
            return t;
        }
    }
}
