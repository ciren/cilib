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

public class Hartman3Test {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Hartman3();
    }

    /**
     * Test of evaluate method, of class {@link Hartman3}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0.1, 0.55592003, 0.85218259);
        assertEquals(-3.8756, function.apply(x), 0.0001);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0, 4.0));
    }
}
