/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;

public class ConstantControlParameterTest {

    @Test
    public void getParameterTest() {
        ConstantControlParameter parameter = new ConstantControlParameter();
        parameter.setParameter(5.3);
        Assert.assertTrue(5.3 == parameter.getParameter());
    }

    @Test
    public void setParameterTest() {
        ConstantControlParameter parameter = new ConstantControlParameter();
        parameter.setParameter(5.3);
        Assert.assertTrue(5.3 == parameter.getParameter());
    }

    @Test
    public void updateTest() {
       ConstantControlParameter parameter = new ConstantControlParameter();
        parameter.setParameter(5.3);
        parameter.update(2.3);
        Assert.assertTrue(5.3 == parameter.getParameter());
    }
}
