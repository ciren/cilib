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

import org.junit.Ignore;

/**
 *
 */
@Ignore("Pending")
public class DETest {

//    /**
//     * Test of iterate method, of class DE.
//     */
//    @Test
//    public void integration() {
//        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());
//        DE de = injector.getInstance(DE.class);
//
//        IndividualProvider provider = injector.getInstance(IndividualProvider.class);
//        FitnessProvider fitnessProvider = new FitnessProvider(null);
//
//        Individual i1 = provider.solution(CandidateSolution.of(1.0)).fitness(fitnessProvider).get();
//        Individual i2 = provider.solution(CandidateSolution.of(3.0)).fitness(fitnessProvider).get();
//        Individual i3 = provider.solution(CandidateSolution.of(4.0)).fitness(fitnessProvider).get();
//
//        Topology<Individual> topology = ImmutableGBestTopology.topologyOf(i1, i2, i3);
//        Topology<Entity> topology1 = de.next(topology);
//    }
//
//    @Test
//    public void iteration() {
//        Injector injector = Guice.createInjector(new EventingModuleBuilder().build(), new CIlibCoreModule(), new PopulationBasedModule());
//        DE de = injector.getInstance(DE.class);
//
//        IndividualProvider provider = injector.getInstance(IndividualProvider.class);
//        FitnessProvider fitnessProvider = injector.getInstance(FitnessProvider.class);
//
//        Individual i1 = provider.solution(CandidateSolution.of(1.0)).fitness(fitnessProvider).get();
//        Individual i2 = provider.solution(CandidateSolution.of(3.0)).fitness(fitnessProvider).get();
//        Individual i3 = provider.solution(CandidateSolution.of(4.0)).fitness(fitnessProvider).get();
//
//        Topology<Individual> topology = ImmutableGBestTopology.topologyOf(i1, i2, i3);
//
//        Predicate<StateT> statePred = new Predicate<StateT>() {
//            private int count = 0;
//            @Override
//            public Boolean f(StateT a) {
////                return /*(a.iterations() == 5) ? */true/* : false*/;
//                return count++ > 1;
//            }
//        };
//
//        Topology<Entity> result = de.fold(topology, statePred, new StateT());
////        Topology<Entity> result = de.fold(topology, stop, new StateT());
//    }
}