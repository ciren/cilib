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

public class BirdTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Bird();
    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Bird. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1,2);
        assertEquals(6.8250541015507, function.apply(x), 0.00000000001);
    }
}
