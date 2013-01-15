/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import org.junit.Test;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import org.junit.Assert;

public class StandardUpdatableControlParameterTest {

    @Test
    public void getParameterTest() {
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(2.1);
        Assert.assertTrue(2.1 == parameter.getParameter());
    }

    @Test
    public void setParameterTest() {
       StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(2.1);
        Assert.assertTrue(2.1 == parameter.getParameter());
    }

    @Test
    public void update() {
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(2.1);
        parameter.update(6.4);
        Assert.assertTrue(6.4 == parameter.getParameter());
    }
}
