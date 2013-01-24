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

public class LevyTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Levy();
    }

    /**
     * Test of evaluate method, of class {@link Levy}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 1.0, 1.0, -9.752356);

        assertEquals(-21.502356, function.apply(x), 0.0000001);

        Vector y = Vector.of(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -4.754402);
        assertEquals(-11.504403, function.apply(y), 0.0000001);
    }
}
