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

public class RosenbrockTest {

    public RosenbrockTest() {

    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Rosenbrock. */
    @Test
    public void testEvaluate() {
        ContinuousFunction function = new Rosenbrock();
        Vector x = Vector.of(1,2,3);
        Vector y = Vector.of(3,2,1);

        assertEquals(201.0, function.apply(x), 0.0);
        assertEquals(5805.0, function.apply(y), 0.0);

        function = new Rosenbrock();
        Vector z = Vector.of(1,2,3,4);
        assertEquals(2705.0, function.apply(z), 0.0);
    }


}
