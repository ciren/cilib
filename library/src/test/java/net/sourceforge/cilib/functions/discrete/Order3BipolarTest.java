/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Order3BipolarTest {

    private Order3Bipolar func = new Order3Bipolar();

    @Test
    public void testFunction() {
        // 0 or 6
        assertEquals(1.0, func.apply(Vector.of(0,0,0,0,0,0)), Maths.EPSILON);
        assertEquals(1.0, func.apply(Vector.of(1,1,1,1,1,1)), Maths.EPSILON);

        // 1 or 5
        assertEquals(0.0, func.apply(Vector.of(1,0,0,0,0,0)), Maths.EPSILON);
        assertEquals(0.0, func.apply(Vector.of(1,1,1,1,1,0)), Maths.EPSILON);

        // 2 or 4
        assertEquals(0.4, func.apply(Vector.of(1,1,0,0,0,0)), Maths.EPSILON);
        assertEquals(0.4, func.apply(Vector.of(1,1,1,1,0,0)), Maths.EPSILON);

        // 3
        assertEquals(0.8, func.apply(Vector.of(1,1,1,0,0,0)), Maths.EPSILON);
    }

}
