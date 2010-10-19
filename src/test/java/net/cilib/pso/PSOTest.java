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

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entity;
import net.cilib.entity.Particle;
import net.cilib.inject.CIlibCoreModule;
import net.cilib.inject.PopulationBasedModule;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class PSOTest {

    @Test
    public void iteration() {
        Topology<Entity> topology = ImmutableGBestTopology.of();
        VelocityProvider velocityProvider = new StandardVelocityProvider(null, null);
        PopulationBasedAlgorithm instance = new PSO(velocityProvider, null);

        instance.iterate(topology);
    }

    @Test
    public void integration() {
        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());
        PSO pso = injector.getInstance(PSO.class);

        Particle p1 = new Particle(null, null, null, null);
    }
}
