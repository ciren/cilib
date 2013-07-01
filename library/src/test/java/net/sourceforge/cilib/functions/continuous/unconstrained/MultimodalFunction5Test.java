/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.unconstrained;

import net.cilib.functions.ContinuousFunction;
import net.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

public class MultimodalFunction5Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new MultimodalFunction5();
    }

    /**
     * Test of evaluate method, of class {@link MultimodalFunction5}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(0.0, 0.0);
        assertEquals(30, function.f(x), 0.0);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}
