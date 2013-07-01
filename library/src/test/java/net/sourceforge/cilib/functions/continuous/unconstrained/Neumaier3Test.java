/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.unconstrained;

import net.cilib.functions.ContinuousFunction;
import net.cilib.type.types.container.Vector;
import net.cilib.math.Maths;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

public class Neumaier3Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new  Neumaier3();
    }

    /**
     * Test of evaluate method, of class {@link Neumaier3}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(0.0, 2.0);

        assertEquals(2.0, function.f(x), Maths.EPSILON);

        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        assertEquals(-2.0, function.f(x), Maths.EPSILON);
    }
}
