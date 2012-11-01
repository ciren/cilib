/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColvilleTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Colville();
    }

    /** Test of evaluate method, of class cilib.functions.unconstrained.Colville. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0,0,0,0);
        Assert.assertEquals(42.0, function.apply(x), 0.0);

        x.setReal(0, 1.0);
        x.setReal(1, 1.0);
        x.setReal(2, 1.0);
        x.setReal(3, 1.0);
        Assert.assertEquals(0.0, function.apply(x), 0.0);
    }
}
