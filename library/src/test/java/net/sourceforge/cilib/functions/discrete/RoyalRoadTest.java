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

public class RoyalRoadTest {

    private RoyalRoad func = new RoyalRoad();

    @Test
    public void testFunction() {

        Vector maxFit = Vector.fill(1, 240);

        assertEquals(12.8, func.apply(maxFit), Maths.EPSILON);

        Vector.Builder b = Vector.newBuilder();

        for(int i = 0; i < 240; i+= 15) {
            for(int k = 0; k < 15; k++) {
                if (k < 7) {
                    b.add(true);
                } else {
                    b.add(false);
                }
            }
        }

        Vector minFit = b.build();
        assertEquals(-0.96, func.apply(minFit), Maths.EPSILON);
    }

}
