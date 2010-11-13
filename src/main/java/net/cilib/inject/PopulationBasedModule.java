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
package net.cilib.inject;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import java.util.List;
import net.cilib.algorithm.Selector;
import net.cilib.algorithm.MockMutationProvider;
import net.cilib.algorithm.MutationProvider;
import net.cilib.algorithm.ReplacementSelector;
import net.cilib.inject.annotation.Global;
import net.cilib.inject.annotation.Local;
import net.cilib.inject.annotation.Unique;
import net.cilib.collection.Topology;
import net.cilib.entity.Entity;
import net.cilib.inject.annotation.SimulationScoped;
import net.cilib.pso.Guide;
import net.cilib.pso.NeighborhoodBest;
import net.cilib.pso.PersonalBest;
import net.cilib.pso.StandardVelocityProvider;
import net.cilib.pso.VelocityProvider;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author gpampara
 */
public class PopulationBasedModule extends AbstractModule {

    @Override
    protected void configure() {
        SimulationScope scope = new SimulationScope();
        bindScope(SimulationScoped.class, scope);
        bind(SimulationScope.class).toInstance(scope);

        bindConstant().annotatedWith(Names.named("population.size")).to(40);
        bind(Selector.class).to(ReplacementSelector.class);
        bind(MutationProvider.class).to(MockMutationProvider.class);

        // PSO related bindings
        bind(VelocityProvider.class).to(StandardVelocityProvider.class);
        bind(Guide.class).annotatedWith(Global.class).to(NeighborhoodBest.class);
        bind(Guide.class).annotatedWith(Local.class).to(PersonalBest.class);
        bind(new TypeLiteral<Supplier<Double>>() {
        }).annotatedWith(Unique.class).toProvider(UniqueSupplier.class);
        bind(new TypeLiteral<Supplier<Double>>() {
        }).annotatedWith(Names.named("acceleration")).toInstance(Suppliers.ofInstance(1.496180));

        bind(Topology.class).toProvider(CurrentTopologyProvider.class).in(scope);
    }

    /**
     * This needs serious work... The API is in the air... No real concrete
     * need for it is exists....
     * @param selector
     * @param randomProvider
     * @return
     */
    @Provides
    @Unique
    Selector getSelector(Selector selector, final RandomProvider randomProvider) {
        return new Selector() {

            @Override
            public Entity select(Entity... elements) {
                return select(Lists.newArrayList(elements));
            }

            @Override
            public Entity select(Iterable<Entity> elements) {
                List<Entity> list = Lists.newArrayList(elements);
                return list.get(randomProvider.nextInt(list.size()));
            }
        };
    }

    /**
     * This needs to be something that manages the current topology for the
     * entire process.
     */
    static class CurrentTopologyProvider implements Provider<Topology> {

        private final SimulationScope scope;

        @Inject
        public CurrentTopologyProvider(SimulationScope scope) {
            this.scope = scope;
        }

        @Override
        public Topology get() {
            return scope.get(Key.get(Topology.class));
        }
    }

    static class UniqueSupplier implements Provider<Supplier<Double>> {

        private final Provider<RandomProvider> randomProvider;

        @Inject
        public UniqueSupplier(@Unique Provider<RandomProvider> randomProvider) {
            this.randomProvider = randomProvider;
        }

        @Override
        public Supplier<Double> get() {
            return new Supplier<Double>() {
                final RandomProvider random = randomProvider.get();

                @Override
                public Double get() {
                    return random.nextDouble();
                }
            };
        }
    }
}
