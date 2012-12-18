/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SixHumpCamelBackTest {

    private static final double EPSILON = 1.0E-4;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new SixHumpCamelBack();
    }

    /**
     * Test of evaluate method, of class {@link SixHumpCamelBack}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(-0.0898,0.7126);
        assertEquals(-1.0316, function.apply(x), EPSILON);

        x.setReal(0, 0.0898);
        x.setReal(1, -0.7126);
        assertEquals(-1.0316, function.apply(x), EPSILON);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
