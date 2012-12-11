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

public class ZettleTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Zettle();
    }

    /**
     * Test of evaluate method, of class {@link Zettle}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(-0.0299, 0);
        assertEquals(-0.003791, function.apply(x), 0.000009);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
