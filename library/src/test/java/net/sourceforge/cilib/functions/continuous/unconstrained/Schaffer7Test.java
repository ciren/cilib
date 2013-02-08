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

public class Schaffer7Test {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Schaffer7();
    }

    /**
     * Test of evaluate method, of class {@link Schaffer7}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0.0, 0.0, 0.0);
        assertEquals(0.0, function.apply(x), 0.0);

        Vector y = Vector.of(1.0, 2.0, 3.0);
        assertEquals(9.633, function.apply(y), 0.001);
    }
}
