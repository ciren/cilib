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
package net.sourceforge.cilib.functions.activation;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;

import net.sourceforge.cilib.type.types.Real;
import org.junit.Test;

public class SigmoidTest {

    @Test
    public void evaluate() {
        Sigmoid sigmoid = new Sigmoid();
        assertEquals(0.5, sigmoid.apply(Real.valueOf(Double.valueOf(0.0))).doubleValue(), 0);
        assertEquals(1.0, sigmoid.apply(Real.valueOf(Double.POSITIVE_INFINITY)).doubleValue(), 0);
        assertEquals(1.0, sigmoid.apply(Real.valueOf(Double.MAX_VALUE)).doubleValue(), 0);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), 0);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(Double.NEGATIVE_INFINITY)).doubleValue(), 0);
        assertEquals(0.5, sigmoid.apply(Real.valueOf(Double.MIN_VALUE)).doubleValue(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidEvaluate() {
        Sigmoid sigmoid = new Sigmoid();
        sigmoid.setSteepness(new ConstantControlParameter(-8.0));

        sigmoid.apply(Real.valueOf(0.0));
    }
}
