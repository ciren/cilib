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
public class IndividualTest {

    @Test
    public void createIndividual() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 3.0);
        Individual i = new Individual(solution, null);

        Assert.assertThat(i.size(), is(2));
    }

    @Test
    public void lessFit() {
        Individual i1 = new Individual(null, Fitnesses.newMinimizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMinimizationFitness(-3.0));

        Assert.assertThat((Individual) i1.lessFit(i2), is(i1));
        Assert.assertThat((Individual) i2.lessFit(i1), is(i1));
    }

    @Test
    public void isLessFit() {
        Individual i1 = new Individual(null, Fitnesses.newMinimizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMinimizationFitness(-3.0));

        Assert.assertTrue(i1.isLessFit(i2));
    }

    @Test
    public void moreFit() {
        Individual i1 = new Individual(null, Fitnesses.newMaximizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMaximizationFitness(-3.0));

        Assert.assertThat((Individual) i1.moreFit(i2), is(i1));
        Assert.assertThat((Individual) i2.moreFit(i1), is(i1));
    }

    @Test
    public void isMoreFit() {
        Individual i1 = new Individual(null, Fitnesses.newMaximizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMaximizationFitness(-3.0));

        Assert.assertTrue(i1.isMoreFit(i2));
    }
}
