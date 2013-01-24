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

public class PowellTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Powell();
    }

    /**
     * Test of evaluate method, of class {@link Powell}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(3.0, -1.0, 0.0, 1.0);
        assertEquals(215.0, function.apply(x), 0.0);

        Vector y = Vector.of(1.0, 1.0, 1.0, 1.0);
        assertEquals(122.0, function.apply(y), 0.0);

        Vector z = Vector.of(0.0, 0.0, 0.0, 0.0);
        assertEquals(0.0, function.apply(z), 0.0);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0));
    }
}
