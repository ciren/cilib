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

public class ShiftedFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        ShiftedFunctionDecorator d = new ShiftedFunctionDecorator();
        d.setHorizontalShift(ConstantControlParameter.of(0));
        d.setVerticalShift(ConstantControlParameter.of(1));
        d.setFunction(s);
        
        assertEquals(d.apply(Vector.of(0.0, 0.0)), 1.0, 0.0);
        
        d.setHorizontalShift(ConstantControlParameter.of(5));
        
        assertEquals(d.apply(Vector.of(5.0, 5.0)), 1.0, 0.0);
        
        d.setVerticalShift(ConstantControlParameter.of(-1));
        
        assertEquals(d.apply(Vector.of(5.0, 5.0)), -1.0, 0.0);
    }
}
