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

import com.google.common.collect.Lists;
import net.cilib.algorithm.SimulationBuilder.PopulationBasedSimulationBuilder;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.inject.SimulationScope;
import net.cilib.main.MockProblem;
import net.cilib.measurement.Measurement;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class SimulationBuilderTest {

    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutAlgorithm() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .using(null);
    }


    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutProblem() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .on(null);
    }

    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutTopology() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .initialTopology(null);
    }

    @Test(expected=NullPointerException.class)
    public void populationBuilderWithoutMeasurements() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .measuredBy(null);
    }

    @Test(expected=NullPointerException.class)
    public void populationBuilderClearsMembersAfterBuild() {
        PopulationBasedSimulationBuilder builder = new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .using(new DE(null, null, null, null))
                .on(new MockProblem())
                .initialTopology(ImmutableGBestTopology.of())
                .measuredBy(Lists.<Measurement>newArrayList());

        builder.build(); // Creates the instance
        builder.build(); // Throws the exception -> Builders are invalid after use.
    }
}