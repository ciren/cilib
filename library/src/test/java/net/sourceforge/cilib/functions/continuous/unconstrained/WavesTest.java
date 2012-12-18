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

public class WavesTest {
 
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Waves();
    }

    /**
     * Test of evaluate method, of class {@link Waves}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(-0.5,-1.2);
        
        assertEquals(-5.907363227, function.apply(x), 0.000000009);

        x.setReal(0, 0.0);
        x.setReal(1, 0.0);
        assertEquals(0.0, function.apply(x), 0.000000009);
        
        x.setReal(0, 0.0);
        x.setReal(1, 1.2);
        assertEquals(0.0, function.apply(x), 0.000000009);
        
        x.setReal(0, 1.2);
        x.setReal(1, -1.2);
        assertEquals(7.210944, function.apply(x), 0.000000009);
        
        x.setReal(0, 1.2);
        x.setReal(1, 0);
        assertEquals(-0.046656, function.apply(x), 0.000000009);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0, 3.0));
    }
}
