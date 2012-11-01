/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.*;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class BoundsFactoryTest {

    @Test
    public void creation() {
        BoundsFactory factory = new BoundsFactory();
        Bounds b1 = factory.create(-1.0, 1.0);

        Assert.assertEquals(-1.0, b1.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, b1.getUpperBound(), 0.0);
    }

    @Test
    public void returnSameInstance() {
        BoundsFactory factory = new BoundsFactory();
        Bounds b1 = factory.create(-1.0, 1.0);
        Bounds b2 = factory.create(-1.0, 1.0);

        Assert.assertSame(b1, b2);
        Assert.assertEquals(b1, b2);
    }

}
