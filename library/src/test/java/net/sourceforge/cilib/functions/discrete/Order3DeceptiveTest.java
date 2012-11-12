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

public class Order3DeceptiveTest {

    private Order3Deceptive func = new Order3Deceptive();

    @Test
    public void testFunction() {
        assertEquals(0.9, func.apply(Vector.of(0,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(1,0,0)), Maths.EPSILON);
        assertEquals(0.3, func.apply(Vector.of(1,1,0)), Maths.EPSILON);
        assertEquals(1.0, func.apply(Vector.of(1,1,1)), Maths.EPSILON);

        assertEquals(0.9, func.apply(Vector.of(0,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(0,1,0)), Maths.EPSILON);
        assertEquals(0.3, func.apply(Vector.of(0,1,1)), Maths.EPSILON);

        assertEquals(1.8, func.apply(Vector.of(0,0,0,0,0,0)), Maths.EPSILON);
        assertEquals(1.2, func.apply(Vector.of(1,0,0,1,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(1,1,0,1,1,0)), Maths.EPSILON);
        assertEquals(2.0, func.apply(Vector.of(1,1,1,1,1,1)), Maths.EPSILON);
    }

}
