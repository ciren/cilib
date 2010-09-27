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
package net.cilib.entity;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class FitnessesTest {

    /**
     * Test of inferior method, of class {@code Fitnesses}.
     */
    @Test
    public void inferiorSameInstance() {
        Fitness f1 = Fitnesses.inferior();
        Fitness f2 = Fitnesses.inferior();

        Assert.assertThat(f1, sameInstance(f2));
    }

    @Test
    public void inferiorWorseThanMaximization() {
        Fitness inf = Fitnesses.inferior();
        Fitness min = Fitnesses.newMaximizationFitness(Double.MIN_VALUE);

        Assert.assertTrue(min.isMoreFitThan(inf));
        Assert.assertFalse(inf.isMoreFitThan(min));
    }

    @Test
    public void inferiorWorseThanMinimization() {
        Fitness inf = Fitnesses.inferior();
        Fitness min = Fitnesses.newMinimizationFitness(Double.MAX_VALUE);

        Assert.assertTrue(min.isMoreFitThan(inf));
        Assert.assertFalse(inf.isMoreFitThan(min));
    }

    @Test
    public void newMaximizationFitness() {
        Fitness f = Fitnesses.newMaximizationFitness(-5.0);
        Assert.assertThat(f, instanceOf(Fitnesses.MaximizationFitness.class));
    }

    @Test
    public void newMinimizationFitness() {
        Fitness f = Fitnesses.newMinimizationFitness(-5.0);
        Assert.assertThat(f, instanceOf(Fitnesses.MinimizationFitness.class));
    }
}
