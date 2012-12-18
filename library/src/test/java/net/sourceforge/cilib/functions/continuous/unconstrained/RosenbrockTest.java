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

public class RosenbrockTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Rosenbrock();
    }

    /**
     * Test of evaluate method, of class {@link Rosenbrock}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1,2,3);
        Vector y = Vector.of(3,2,1);

        assertEquals(201.0, function.apply(x), 0.0);
        assertEquals(5805.0, function.apply(y), 0.0);

        Vector z = Vector.of(1,2,3,4);
        assertEquals(2705.0, function.apply(z), 0.0);
    }


}
