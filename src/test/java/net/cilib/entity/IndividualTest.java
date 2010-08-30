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
    public void plus() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 3.0);
        Individual current = new Individual(solution, null);
        Individual other = new Individual(solution, null);

        // z = x + y
        Entity result = current.plus(other).build();
        Assert.assertArrayEquals(new double[]{2, 6}, result.solution().toArray(), 0.001);
    }

    @Test
    public void subtract() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 3.0);
        Individual current = new Individual(solution, null);
        Individual other = new Individual(solution, null);

        // z = x - y
        Entity result = current.subtract(other).build();

        Assert.assertArrayEquals(new double[]{0.0, 0.0}, result.solution().toArray(), 0.001);
    }

    @Test
    public void multiply() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 3.0);
        Individual current = new Individual(solution, null);

        // z = x * y
        Entity result = current.multiply(2.0).build();
        Assert.assertArrayEquals(new double[]{2.0, 6.0}, result.solution().toArray(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalDivide() {
        CandidateSolution.Builder builder = CandidateSolution.newBuilder();
        Individual current = new Individual(builder.build(), null);

        current.divide(0.0);
    }

    @Test
    public void divide() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 3.0);
        Individual current = new Individual(solution, null);

        // z = x / y
        Entity result = current.divide(1.0).build();
        Assert.assertArrayEquals(new double[]{1.0, 3.0}, result.solution().toArray(), 0.001);
    }

    @Test
    public void complexFunctionalOperation() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 2.0);
        Individual current = new Individual(solution, null);
        Individual other = new Individual(solution, null);

        Entity result = current.multiply(4.0).plus(other).build();
        Assert.assertArrayEquals(new double[]{5.0, 10.0}, result.solution().toArray(), 0.001);
    }

    @Test
    public void lessFit() {
        Individual i1 = new Individual(null, Fitnesses.newMinimizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMinimizationFitness(-3.0));

        Assert.assertThat((Individual) i1.lessFit(i2), is(i1));
        Assert.assertThat((Individual) i2.lessFit(i1), is(i1));
    }

    @Test
    public void moreFit() {
        Individual i1 = new Individual(null, Fitnesses.newMaximizationFitness(4.0));
        Individual i2 = new Individual(null, Fitnesses.newMaximizationFitness(-3.0));

        Assert.assertThat((Individual) i1.moreFit(i2), is(i1));
        Assert.assertThat((Individual) i2.moreFit(i1), is(i1));
    }
}
