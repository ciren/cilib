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

public class ModifiedSchaffer4Test {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new ModifiedSchaffer4();
    }

    /**
     * Test of evaluate method, of class {@link ModifiedSchaffer4}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(0.0, 1.253132);

        //test global minimum
        assertEquals(0.292579, function.apply(x), EPSILON);

        //test another point
        x.setReal(0, 2.0);
        x.setReal(1, 3.0);
        assertEquals(0.334273, function.apply(x), EPSILON);
    }
}
