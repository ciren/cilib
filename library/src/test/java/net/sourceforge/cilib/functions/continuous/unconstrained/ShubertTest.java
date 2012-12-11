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

public class ShubertTest {
 
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Shubert();
    }

    /**
     * Test of evaluate method, of class {@link Shubert}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0,0);

        assertEquals(19.875836249, function.apply(x), 0.000000009);

        x.setReal(0, -8.0);
        x.setReal(1, -8.0);
        assertEquals(7.507985827, function.apply(x), 0.000000009);

        x.setReal(0, -7.2);
        x.setReal(1, -7.7);
        assertEquals(-157.071676802, function.apply(x), 0.000000009);
    }   
}
