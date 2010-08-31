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

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryProvider;
import net.cilib.algorithm.DE;
import net.cilib.algorithm.Selector;
import net.cilib.algorithm.MockMutationProvider;
import net.cilib.algorithm.MutationProvider;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.algorithm.ReplacementSelector;
import net.cilib.annotation.Initialized;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.collection.mutable.MutableGBestTopology;
import net.cilib.entity.Entity;
import net.cilib.entity.EntityFactory;
import net.cilib.entity.Individual;

/**
 *
 * @author gpampara
 */
public class PopulationBasedModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Topology.class).to(MutableGBestTopology.class);
        bind(Selector.class).to(ReplacementSelector.class);
        bind(MutationProvider.class).to(MockMutationProvider.class);

        bind(EntityFactory.class).toProvider(FactoryProvider.newFactory(EntityFactory.class, Individual.class));

        bind(PopulationBasedAlgorithm.class).to(DE.class);
    }

    @Provides
    @Initialized
    Topology<Entity> getInitializedTopology(Provider<Topology<Entity>> t) {
        return t.get();
    }

    @Provides
    Topology<Entity> getTopology() {
        return ImmutableGBestTopology.of();
    }
}
