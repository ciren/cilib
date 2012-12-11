/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.Maths;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class EllipticTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Elliptic();
    }

    /**
     * Test of evaluate method, of class {@link Elliptic}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(Real.valueOf(0.0), Real.valueOf(0.0));
        assertEquals(0.0, function.apply(x), Maths.EPSILON);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);
        assertEquals(4000001, function.apply(x), Maths.EPSILON);
    }
}
