/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsymmetricFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        AsymmetricFunctionDecorator a = new AsymmetricFunctionDecorator();
        a.setBeta(ConstantControlParameter.of(0.0));
        a.setFunction(s);

        Vector v = Vector.of(1.0, 2.0);
        assertEquals(5.0, a.apply(v), 0.0);

        a.setBeta(ConstantControlParameter.of(1.0));
        assertEquals(29.411, a.apply(v), 0.001);

        a.setBeta(ConstantControlParameter.of(2.0));
        assertEquals(202.810, a.apply(v), 0.001);

        v.setReal(1, -2.0);
        assertEquals(5.0, a.apply(v), 0.0);
    }
}
