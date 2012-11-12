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

public class SchwefelProblem2_22Test {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new SchwefelProblem2_22();
    }

    /** Test of evaluate method, of class cilib.functions.unconstrained.SchwefelProblem2_22. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0, 0);
        assertEquals(0.0, function.apply(x), 0.0);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);
        assertEquals(5.0, function.apply(x), 0.0000000001);
    }
}
