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
import static net.cilib.entity.MutableSeq.*;

/**
 *
 * @author gpampara
 */
public class CandidateSolutionTest {
    @Test
    public void defensiveToArray() {
        CandidateSolution solution = CandidateSolution.of(1.0, 1.0);
        double[] a = solution.toArray();
        double[] b = solution.toArray();

        Assert.assertNotSame(a, b);
        Assert.assertArrayEquals(a, b, 0.001);
    }

    @Test
    public void copyOfCreation() {
        CandidateSolution solution = CandidateSolution.of(1.0, 2.0);
        CandidateSolution copy = CandidateSolution.copyOf(solution);

        Assert.assertThat(copy, not(sameInstance(solution)));
        Assert.assertThat(solution.equals(copy), is(true));
    }

    @Test
    public void mutatability() {
        CandidateSolution solution = CandidateSolution.of(1.0, 2.0);
        MutableSeq seq = multiply(2.0, solution.toMutableSeq());

        // The original instance must not change
        Assert.assertThat(solution, equalTo(CandidateSolution.of(1.0, 2.0)));
        // The mutable version did change
        Assert.assertThat(seq.toArray(), equalTo(new double[]{2.0, 4.0}));
    }
}
