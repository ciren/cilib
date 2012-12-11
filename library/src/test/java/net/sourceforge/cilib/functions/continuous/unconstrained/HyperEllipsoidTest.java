/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;;
import org.junit.Before;
import org.junit.Test;

public class HyperEllipsoidTest {

    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new HyperEllipsoid();
    }

    /**
     * Test of evaluate method, of class {@link HyperEllipsoid}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0, 0, 0);
        assertEquals(0.0, function.apply(x), EPSILON);

        //test another point
        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        x.setReal(2, 2.0);
        assertEquals(24.0, function.apply(x), EPSILON);
    }
}
