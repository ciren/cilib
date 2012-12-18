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

/**
 * Test the validity of the implmentation of the {@link Himmelblau} function.
 */
public class HimmelblauTest {

    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Himmelblau();
    }

    /**
     * Test of evaluate method, of class {@link Himmelblau}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(3,2);
        assertEquals(0.0, function.apply(x), EPSILON);

        x.setReal(0, -2.805118);
        x.setReal(1, 3.131312);
        assertEquals(0.0, function.apply(x), EPSILON);

        x.setReal(0, -3.779301);
        x.setReal(1, -3.283185);
        assertEquals(0.0, function.apply(x), EPSILON);

        x.setReal(0, 3.584428);
        x.setReal(1, -1.848126);
        assertEquals(0.0, function.apply(x), EPSILON);

        //test one other point
        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        assertEquals(26.0, function.apply(x), EPSILON);
    }

    /** Test argument with invalid dimension */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
