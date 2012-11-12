/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
