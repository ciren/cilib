/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DamavandiTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Damavandi();
    }

    /**
     * Test of evaluate method, of class {@link Damavandi}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(Real.valueOf(2.0000001),
                Real.valueOf(2.0000001));
        assertEquals(0.0, function.apply(x), 0.000000001);

        x.setReal(0, 7.0);
        x.setReal(1, 7.0);
        assertEquals(2.0, function.apply(x), 0.0);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
