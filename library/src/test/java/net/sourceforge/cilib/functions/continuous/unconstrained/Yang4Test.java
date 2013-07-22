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

public class Yang4Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Yang4();
    }

    /**
     * Test of evaluate method, of class {@link Yang4}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0, 0, 0);
        assertEquals(0, function.f(x), 0.0);
    }
}
