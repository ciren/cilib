/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.Maths;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

public class NastyBenchmarkTest {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new NastyBenchmark();
    }

    /**
     * Test of evaluate method, of class {@link NastyBenchmark}.
     */
    @Test
    public void testApply() {
        Vector x = Vector.of(3.0, 1.0, 6.0);

        assertEquals(14.0, function.apply(x), Maths.EPSILON);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);
        x.setReal(2, 3.0);
        assertEquals(0.0, function.apply(x), Maths.EPSILON);
    }
}
