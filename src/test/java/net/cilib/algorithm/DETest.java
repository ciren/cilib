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
package net.cilib.algorithm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.CandidateSolution;
import net.cilib.entity.Entity;
import net.cilib.entity.Individual;
import net.cilib.entity.IndividualProvider;
import net.cilib.main.CIlibCoreModule;
import net.cilib.main.PopulationBasedModule;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class DETest {

    /**
     * Test of iterate method, of class DE.
     */
    @Test
    public void integration() {
        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());
        DE de = injector.getInstance(DE.class);

        IndividualProvider provider = injector.getInstance(IndividualProvider.class);

        Individual i1 = provider.solution(CandidateSolution.copyOf(1.0)).get();
        Individual i2 = provider.solution(CandidateSolution.copyOf(3.0)).get();
        Individual i3 = provider.solution(CandidateSolution.copyOf(4.0)).get();

        Topology<Entity> topology = ImmutableGBestTopology.<Entity>newBuilder()
                .add(i1).add(i2).add(i3).build();
        Topology<Entity> topology1 = de.iterate(topology);
        System.out.println("topology1: " + topology1);
    }
}
