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
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.List;
import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.DE;
import net.cilib.algorithm.Simulation;
import net.cilib.algorithm.SimulationBuilder;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.measurement.Measurement;
import net.cilib.problem.Problem;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<Predicate<Algorithm>> stoppingConditions = Lists.newArrayList();
        stoppingConditions.add(new Predicate<Algorithm>() {
            int count = 5;
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

        DE de = injector.getInstance(DE.class);
        Problem problem = new MockProblem();
        List<Measurement> measurements = Lists.newArrayList();
        Simulation simulation = injector.getInstance(SimulationBuilder.class)
                .newPopulationBasedSimulation()
                .using(de)
                .on(problem)
                .initialTopology(ImmutableGBestTopology.of())
                .measuredBy(measurements)
                .build();

        simulation.execute(stoppingConditions);
    }
}
