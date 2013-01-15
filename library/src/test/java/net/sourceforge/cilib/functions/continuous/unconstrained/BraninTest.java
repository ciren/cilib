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

public class BraninTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Branin();
    }

    /**
     * Test of evaluate method, of class {@link Branin}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1,2);

        assertEquals(21.62763539206238, function.apply(x), 0.00000000000001);

        x.setReal(0, -Math.PI);
        x.setReal(1, 12.275);
        assertEquals(0.397887, function.apply(x), 0.0000009);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
