/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class Shekel4Test {

    private Shekel4 function;

    @Before
    public void instantiate() {
        this.function = new Shekel4();
    }

    /**
     * Test of evaluate method, of class {@link Shekel4}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(4.0, 4.0, 4.0, 4.0);

        function.setN(5);
        assertEquals(-10.1532, function.apply(x), 0.001);

        function.setN(7);
        assertEquals(-10.40294, function.apply(x), 0.001);

        function.setN(10);
        assertEquals(-10.53641, function.apply(x), 0.001);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }

    /**
     * Test invalid n argument.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidsetN() {
        function.setN(2);
    }
}