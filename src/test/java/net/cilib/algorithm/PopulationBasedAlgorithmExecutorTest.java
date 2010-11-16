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

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entity;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class PopulationBasedAlgorithmExecutorTest {

    private final DE emptyDE = new DE(null, null, null, null);

    @Test(expected = NullPointerException.class)
    public void nullAlgorithm() {
        PopulationBasedAlgorithmExecutor executor = new PopulationBasedAlgorithmExecutor(null);
        executor.execute(null, null, Lists.<Predicate<Algorithm>>newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void nullTopology() {
        PopulationBasedAlgorithmExecutor executor = new PopulationBasedAlgorithmExecutor(null);
        executor.execute(emptyDE, null, Lists.<Predicate<Algorithm>>newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void nullStoppingConditions() {
        PopulationBasedAlgorithmExecutor executor = new PopulationBasedAlgorithmExecutor(null);
        executor.execute(emptyDE, ImmutableGBestTopology.<Entity>of(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stoppingConditionsRequired() {
        PopulationBasedAlgorithmExecutor executor = new PopulationBasedAlgorithmExecutor(null);
        executor.execute(emptyDE, ImmutableGBestTopology.<Entity>of(), Lists.<Predicate<Algorithm>>newArrayList());
    }
}
