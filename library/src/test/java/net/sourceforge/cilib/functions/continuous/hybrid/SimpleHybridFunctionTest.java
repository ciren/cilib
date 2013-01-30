/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.hybrid;

import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleHybridFunctionTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();

        SimpleHybridFunction sh = new SimpleHybridFunction();
        sh.addFunction(s);
        sh.addFunction(s);

        Vector v = Vector.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(s.apply(v), 55, 0.0);

        assertEquals(sh.apply(v), 110.0, 0.0);
    }
}
