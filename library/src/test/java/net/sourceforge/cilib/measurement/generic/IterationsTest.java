/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IterationsTest {

    @Test
    public void iterationNumber() {
        final int expected = 1000;
        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        when(algorithm.getIterations()).thenReturn(expected);

        Measurement m = new Iterations();
        Assert.assertEquals(expected, ((Int) m.getValue(algorithm)).intValue());
    }

    @Test
    public void resultType() {
        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

        Measurement m = new Iterations();
        Assert.assertThat(m.getValue(algorithm), is(Int.class));
    }
}
