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

public class Order5DeceptiveTest {

    private Order5Deceptive func = new Order5Deceptive();

    @Test
    public void testFunction() {
        assertEquals(4.0, func.apply(Vector.of(0,0,0,0,0)), Maths.EPSILON);
        assertEquals(3.0, func.apply(Vector.of(0,0,0,0,1)), Maths.EPSILON);
        assertEquals(2.0, func.apply(Vector.of(0,0,0,1,1)), Maths.EPSILON);
        assertEquals(1.0, func.apply(Vector.of(0,0,1,1,1)), Maths.EPSILON);
        assertEquals(3.5, func.apply(Vector.of(1,1,1,1,1)), Maths.EPSILON);

        assertEquals(0.0, func.apply(Vector.of(1,0,0,0,1)), Maths.EPSILON);
        assertEquals(0.0, func.apply(Vector.of(1,0,1,0,1)), Maths.EPSILON);
        assertEquals(0.0, func.apply(Vector.of(1,1,0,0,1)), Maths.EPSILON);
        assertEquals(0.0, func.apply(Vector.of(1,0,0,1,1)), Maths.EPSILON);
    }

}
