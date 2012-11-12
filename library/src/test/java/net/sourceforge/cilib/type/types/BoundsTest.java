/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.Maths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class BoundsTest {

    @Test
    public void equals() {
        Bounds b1 = new Bounds(0.0, 2.0);
        Bounds b2 = new Bounds(0.0, 2.0);

        Assert.assertEquals(b1, b2);
    }

    @Test
    public void boundsEdgeCases() {
        Bounds b1 = new Bounds(0.0, 2.0);
        Assert.assertTrue(b1.isInsideBounds(2.0));
        Assert.assertTrue(b1.isInsideBounds(0.0));

        Assert.assertFalse(b1.isInsideBounds(2.0 + Maths.EPSILON));
        Assert.assertFalse(b1.isInsideBounds(0.0 - Maths.EPSILON));
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidLowerBound() {
        new Bounds(7.0, 4.0);
    }

}
