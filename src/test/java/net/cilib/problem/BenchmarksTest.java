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
package net.cilib.problem;

import org.junit.Assert;
import fj.data.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 *
 */
public class BenchmarksTest {

    @Test
    public void griewankSolution() {
        Evaluatable e = Evaluators.createL(Benchmarks.rastrigin);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }

    @Test
    public void griewankAtPoint() {
        Evaluatable e = Evaluators.createL(Benchmarks.rastrigin);

        Assert.assertThat(e.evaluate(List.list(Math.PI / 2, Math.PI / 2)).some(), closeTo(1.0012337, 0.000001));
    }
}
