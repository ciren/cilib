/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.continuous.unconstrained.Beale;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.random.generator.Rand;
import org.junit.Test;
import static org.junit.Assert.*;

public class PermutationFunctionDecoratorTest {

    @Test
    public void testApply() {
        Beale s = new Beale();
        PermutationFunctionDecorator p = new PermutationFunctionDecorator();
        p.setFunction(s);

        Vector v = Vector.of(1.0, 2.0);
        assertEquals(s.apply(v), 126.453125, 0.0);

        // permute input vector, i.e (1.0, 2.0) -> (2.0, 1.0)
        Rand.setSeed(3);
        assertEquals(p.apply(v), 14.203125, 0.0);
    }
}
