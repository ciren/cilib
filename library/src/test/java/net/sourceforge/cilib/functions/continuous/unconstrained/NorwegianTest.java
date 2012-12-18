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
import org.junit.Test;
import org.junit.Before;

public class NorwegianTest {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Norwegian();
    }

    /**
     * Test of evaluate method, of class {@link Norwegian}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(1.0, 1.0, 1.0);

        //test global minimum
        assertEquals(-1.0, function.apply(x), EPSILON);

        //test another point
        x.setReal(0, 0.5);
        x.setReal(1, 1.0);
        x.setReal(2, 0.5);
        assertEquals(-0.845039, function.apply(x), EPSILON);
    }
}
