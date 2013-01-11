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

public class MultimodalFunction3Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new MultimodalFunction3();
    }

    /**
     * Test of evaluate method, of class {@link MultimodalFunction3}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(0.16, 0.16);

        //test minimum
        assertEquals(0.0, function.apply(x), 0.0000001);

        //test another point
        x.setReal(0, 0.08);
        x.setReal(1, 0.08);
        assertEquals(2.0, function.apply(x), 0.001);
    }
}
