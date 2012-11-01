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

public class SchwefelProblem1_2Test {
    
    private SchwefelProblem1_2 func = new SchwefelProblem1_2();
    
    @Test
    public void testFunction() {
        assertEquals(0.0, func.apply(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0)), Maths.EPSILON);
        
        assertEquals(5.0, func.apply(Vector.of(1.0, 1.0)), Maths.EPSILON);
        
        assertEquals(30.0, func.apply(Vector.of(1.0, 1.0, 3.0)), Maths.EPSILON);
    }
    
}
