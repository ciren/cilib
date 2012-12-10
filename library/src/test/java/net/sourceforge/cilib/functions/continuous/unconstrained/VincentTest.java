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

public class VincentTest {
 
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Vincent();
    }

    /**
     * Test of evaluate method, of class {@link Vincent}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(5,5);

        assertEquals(-0.246258053, function.apply(x), 0.000000009);

        x.setReal(0, 10.0);
        x.setReal(1, 10.0);
        assertEquals(0.719420726, function.apply(x), 0.000000009);
        
        x.setReal(0, 8.0);
        x.setReal(1, 8.0);
        assertEquals(-2.861700698, function.apply(x), 0.000000009);
    }   
}
