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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SigmoidTest {

    @Test
    public void evaluate() {
        Sigmoid sigmoid = new Sigmoid();
        assertEquals(1.0, sigmoid.apply(Real.valueOf(Double.POSITIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(1.0, sigmoid.apply(Real.valueOf(Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(Double.NEGATIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(0.5, sigmoid.apply(Real.valueOf(Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.5, sigmoid.apply(Real.valueOf(0.0)).doubleValue(), Maths.EPSILON);
        assertEquals(0.52497918747894, sigmoid.apply(Real.valueOf((0.1)).doubleValue()), Maths.EPSILON);
        assertEquals(0.622459331201855, sigmoid.apply(Real.valueOf((0.5)).doubleValue()), Maths.EPSILON);
        assertEquals(0.710949502625004, sigmoid.apply(Real.valueOf((0.9)).doubleValue()), Maths.EPSILON);
        assertEquals(0.2689414213699951, sigmoid.apply(Real.valueOf((-1.0))).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void evaluateWithLambdaAndGamma() {
        Sigmoid sigmoid = new Sigmoid();
        sigmoid.setLambda(ConstantControlParameter.of(2.0));
        sigmoid.setGamma(ConstantControlParameter.of(2.0));
        assertEquals(2.0, sigmoid.apply(Real.valueOf(Double.POSITIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(2.0, sigmoid.apply(Real.valueOf(Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, sigmoid.apply(Real.valueOf(Double.NEGATIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(1.0, sigmoid.apply(Real.valueOf(Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(1.0, sigmoid.apply(Real.valueOf(0.0)).doubleValue(), Maths.EPSILON);
        assertEquals(1.099667994624956, sigmoid.apply(Real.valueOf((0.1)).doubleValue()), Maths.EPSILON);
        assertEquals(1.4621171572600098, sigmoid.apply(Real.valueOf((0.5)).doubleValue()), Maths.EPSILON);
        assertEquals(1.7162978701990246, sigmoid.apply(Real.valueOf((0.9)).doubleValue()), Maths.EPSILON);
        assertEquals(0.2384058440442351, sigmoid.apply(Real.valueOf((-1.0))).doubleValue(), Maths.EPSILON);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidEvaluate() {
        Sigmoid sigmoid = new Sigmoid();
        sigmoid.setLambda(ConstantControlParameter.of(-8.0));

        sigmoid.apply(Real.valueOf(0.0));
    }
}
