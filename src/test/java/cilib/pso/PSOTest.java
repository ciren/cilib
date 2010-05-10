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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.LBestTopology;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class PSOTest {

    @Test
    public void someTest() {
//        Module m = new AbstractModule() {
//
//            @Override
//            protected void configure() {
//                bind(Topology.class).to(LBestTopology.class);
//            }
//        };
//
//        Injector injector = Guice.createInjector(Modules.override(new PSOModule()).with(m));
//
//        PSO p = injector.getInstance(PSO.class);
//        p.performIteration();
    }
}
