/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.Maths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the validity of the implmentation of the {@link GoldsteinPrice} function.
 */
public class GoldsteinPriceTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new GoldsteinPrice();
    }

    /**
     * Test of evaluate method.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0,-1);
        Assert.assertEquals(3.0, function.apply(x), Maths.EPSILON);

        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        Assert.assertEquals(76728.0, function.apply(x), Maths.EPSILON);
    }
}
