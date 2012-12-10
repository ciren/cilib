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

public class EggHolderTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new EggHolder();
    }

    /**
     * Test of evaluate method, of class {@link EggHolder}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(200,100);
        assertEquals(-166.745338888944,function.apply(x),0.00000000001);

        x.setReal(0, 512.0);
        x.setReal(1, 404.2319);
        assertEquals(-959.640662710616,function.apply(x),0.00000000001);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.apply(Vector.of(1.0));
    }
}
