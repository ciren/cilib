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

public class SummationRangeFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        SummationRangeFunctionDecorator sr = new SummationRangeFunctionDecorator();
        sr.setFunction(s);

        Vector v = Vector.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);

        sr.setGroupSize(2);
        sr.setLower(ConstantControlParameter.of(0));
        sr.setUpper(ConstantControlParameter.of(2));
        assertEquals(30.0, sr.apply(v), 0.0);
    }
}
