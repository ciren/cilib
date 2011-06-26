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

import fj.data.Option;
import net.cilib.collection.immutable.CandidateSolution;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author gpampara
 */
public class MaxFitnessComparatorTest {

    @Test
    public void lessFit() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.some(2.0));
        FitnessComparator c = FitnessComparator.MAX;

        Assert.assertThat(c.lessFit(i1, i2), is(i1));
    }

    @Test
    public void isMoreFit() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.some(2.0));
        FitnessComparator c = FitnessComparator.MAX;

        Assert.assertThat(c.isMoreFit(i1, i2), is(false));
        Assert.assertThat(c.isMoreFit(i2, i1), is(true));
    }

    @Test
    public void isAMoreFitIndividualReturned() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.some(2.0));
        FitnessComparator c = FitnessComparator.MAX;

        Assert.assertThat(c.moreFit(i1, i2), is(i2));
    }

    @Test
    public void validIndividualReturned() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.<Double>none());
        FitnessComparator c = FitnessComparator.MAX;

        Individual result = c.moreFit(i1, i2);
        Assert.assertThat(result, is(i1));
    }

    @Test
    public void bidirectionalMoreFit() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.<Double>none());
        FitnessComparator c = FitnessComparator.MAX;

        Assert.assertThat(c.moreFit(i1, i2), is(i1));
        Assert.assertThat(c.moreFit(i2, i1), is(i1));
    }

    @Test
    public void bidirectionalLessFit() {
        Individual i1 = new Individual(CandidateSolution.solution(1.0), Option.some(1.0));
        Individual i2 = new Individual(CandidateSolution.solution(1.0), Option.<Double>none());
        FitnessComparator c = FitnessComparator.MAX;

        Assert.assertThat(c.lessFit(i1, i2), is(i2));
        Assert.assertThat(c.lessFit(i2, i1), is(i2));
    }
}
