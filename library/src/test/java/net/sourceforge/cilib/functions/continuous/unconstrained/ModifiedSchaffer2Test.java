/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

/**
 */
public class ModifiedSchaffer2Test {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new ModifiedSchaffer2();
    }

    /** Test of the apply method of  the {@link ModifiedSchaffer2} class */
    @Test
    public void testApply() {
        Vector x = Vector.of(0.0, 0.0);

        //test global minimum
        Assert.assertEquals(0.0, function.apply(x), EPSILON);

        //test another point
        x.setReal(0, 2.0);
        x.setReal(1, 3.0);
        Assert.assertEquals(0.908837, function.apply(x), EPSILON);

    }
}
