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

public class SchwefelTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Schwefel();
    }

    /** Test of evaluate method, of class {@link Schwefel}. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 2.0, 3.0);
        Vector y = Vector.of(-1.0, -2.0, -3.0);

        assertEquals(1251.170579, function.apply(x), 0.000001);
        assertEquals(1262.726744, function.apply(y), 0.000001);

        x.setReal(0, 420.9687);
        x.setReal(1, 420.9687);
        x.setReal(2, 420.9687);
        assertEquals(0.0, function.apply(x), 0.000000001);
    }
}
