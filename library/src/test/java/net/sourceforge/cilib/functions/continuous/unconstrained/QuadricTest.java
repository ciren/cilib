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

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

/**
 *
 */
public class QuadricTest {

    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Quadric();
    }

    /** Test of the evaluate method of  the {@link Quadric} class */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0, 0, 0);
        Assert.assertEquals(0.0, function.apply(x), EPSILON);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);
        x.setReal(2, 3.0);
        Assert.assertEquals(46.0, function.apply(x), EPSILON);
    }
}
