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

import net.cilib.collection.immutable.CandidateSolution;
import org.junit.Assert;
import net.cilib.problem.Problem;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 *
 * @author gpampara
 */
public class FitnessProviderTest {

    @Test
    public void testFinalize() {
        FitnessProvider provider = new FitnessProvider(new Problem() {
            @Override
            public Double f(Double a) {
                return a;
            }
        });
        
        Assert.assertThat(provider.evaluate(CandidateSolution.of(1.0, 2.0)).some(), equalTo(3.0));
    }
}