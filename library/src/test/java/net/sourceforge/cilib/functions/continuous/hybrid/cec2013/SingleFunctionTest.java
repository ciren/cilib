/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.hybrid.cec2013;

import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SingleFunctionTest {

    private SingleFunction function;

    @Before
    public void instantiate() {
        this.function = new SingleFunction();
    }

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        function.setFunction(s);
        Vector v = Vector.of(1.0, 2.0, 3.0);

        assertEquals(s.apply(v), function.apply(v), 0.0);

        function.setBias(450.0);
        assertEquals(s.apply(v) + 450, function.apply(v), 0.0);
    }

    @Test
    public void testWeight() {
        Vector v = Vector.of(1.0, 2.0, 3.0);
        function.setHorizontalShift(ConstantControlParameter.of(0.0));
        assertEquals(6.926569E-3, function.getWeight(v), 0.0000001);

        function.setSigma(2.0);
        assertEquals(3.9859653E-2, function.getWeight(v), 0.0000001);

        function.setHorizontalShift(ConstantControlParameter.of(2.0));
        assertEquals(4.60022207E-1, function.getWeight(v), 0.0000001);
    }
}
