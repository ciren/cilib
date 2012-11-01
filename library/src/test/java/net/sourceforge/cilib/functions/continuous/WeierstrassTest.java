/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class WeierstrassTest {
    
    private Weierstrass func = new Weierstrass();
    
    @Test
    public void testFunction() {
        assertEquals(0.0, func.apply(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0)), Maths.EPSILON);
        
        func.setkMax(2);
        assertEquals(1.786474508, func.apply(Vector.of(0.1, 0.1)), 0.01);
    }
    
}
