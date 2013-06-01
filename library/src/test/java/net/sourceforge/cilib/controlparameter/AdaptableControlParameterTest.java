/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import org.junit.Test;
import net.sourceforge.cilib.controlparameter.AdaptableControlParameter;
import org.junit.Assert;

public class AdaptableControlParameterTest {

    @Test
    public void getParameterTest() {
        AdaptableControlParameter parameter = new AdaptableControlParameter();
        parameter.setParameter(2.1);
        Assert.assertTrue(2.1 == parameter.getParameter());
    }

    @Test
    public void setParameterTest() {
       AdaptableControlParameter parameter = new AdaptableControlParameter();
        parameter.setParameter(2.1);
        Assert.assertTrue(2.1 == parameter.getParameter());
    }

    @Test
    public void update() {
        AdaptableControlParameter parameter = new AdaptableControlParameter();
        parameter.setParameter(2.1);
        parameter.update(6.4);
        Assert.assertTrue(6.4 == parameter.getParameter());
    }
}
