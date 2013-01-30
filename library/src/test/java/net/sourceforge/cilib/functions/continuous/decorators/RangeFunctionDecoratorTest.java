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

public class RangeFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        RangeFunctionDecorator r = new RangeFunctionDecorator();
        r.setFunction(s);

        Vector v = Vector.of(1.0, 2.0, 3.0, 4.0, 5.0);
        assertEquals(s.apply(v), 55, 0.0);

        r.setStart(ConstantControlParameter.of(0));
        r.setEnd(ConstantControlParameter.of(2));
        assertEquals(r.apply(v), 5.0, 0.0);

        r.setStart(ConstantControlParameter.of(2));
        r.setEnd(ConstantControlParameter.of(4));
        assertEquals(r.apply(v), 25.0, 0.0);
    }
}
