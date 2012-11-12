/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SchwefelTest {

    public SchwefelTest() {
    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Schwefel. */
    @Test
    public void testEvaluate() {
        ContinuousFunction function = new Schwefel();

        Vector x = Vector.of(1.0, 2.0, 3.0);
        Vector y = Vector.of(-1.0, -2.0, -3.0);

        assertEquals(1262.726744, function.apply(x), 0.0000009);
        assertEquals(1251.170579, function.apply(y), 0.0000009);
    }
}
