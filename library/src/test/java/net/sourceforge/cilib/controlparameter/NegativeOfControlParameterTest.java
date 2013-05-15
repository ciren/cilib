/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.sourceforge.cilib.math.Maths;

public class NegativeOfControlParameterTest {

    @Test
    public void getParameterTest() {
        ConstantControlParameter parameter = new ConstantControlParameter();
        parameter.setParameter(5.3);
        NegativeOfControlParameter negative = new NegativeOfControlParameter();
        negative.setControlParameter(parameter);
        assertEquals(-5.3, negative.getParameter(), Maths.EPSILON);
    }
}
