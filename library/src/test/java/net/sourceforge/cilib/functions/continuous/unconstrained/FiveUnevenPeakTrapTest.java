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

public class FiveUnevenPeakTrapTest {
    
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new FiveUnevenPeakTrap();
    }

    /**
     * Test of evaluate method, of class {@link FiveUnevenPeakTrap}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0.0);
        
        assertEquals(-200.0, function.apply(x), 0.000000009);

        x.setReal(0, 5.0);
        assertEquals(-160.0, function.apply(x), 0.000000009);
        
        x.setReal(0, 12.5);
        assertEquals(-140.0, function.apply(x), 0.000000009);
        
        x.setReal(0, 22.5);
        assertEquals(-160.0, function.apply(x), 0.000000009);
        
        x.setReal(0, 30.0);
        assertEquals(-200.0, function.apply(x), 0.000000009);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0, 2.0));
    }
}
