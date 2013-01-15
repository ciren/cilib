/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.random.generator.Rand;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RealTest {

    @Test
    public void cloning() {
        Real r = Real.valueOf(-10.0);
        Real test = Real.valueOf(-10.0);

        assertEquals(r.doubleValue(), test.doubleValue(), Double.MIN_NORMAL);
        assertNotSame(r, test);
    }

    @Test
    public void equality() {
        Real i1 = Real.valueOf(10.0);
        Real i2 = Real.valueOf(10.0);
        Real i3 = Real.valueOf(-5.0);

        assertTrue(i1.equals(i1));
        assertTrue(i2.equals(i2));
        assertTrue(i3.equals(i3));

        assertTrue(i1.equals(i2));
        assertFalse(i1.equals(i3));
        assertTrue(i2.equals(i1));
        assertFalse(i2.equals(i3));
    }

    @Test
    public void hashValue() {
        Real r = Real.valueOf(-10.0, new Bounds(-30.0, 30.0));

        // This is the hasCode evaluation of the super classes bound information as well
        assertEquals(1195970368, r.hashCode());
    }

    @Test
    public void comparison() {
        Real r1 = Real.valueOf(15.0, new Bounds(0.0, 30.0));
        Real r2 = Real.valueOf(-15.0, new Bounds(-30.0, 0.0));

        assertEquals(0, r1.compareTo(r1));
        assertEquals(0, r2.compareTo(r2));
        assertEquals(1, r1.compareTo(r2));
        assertEquals(-1, r2.compareTo(r1));
    }

    @Test
    public void representation() {
        Real r = Real.valueOf(0.0, new Bounds(-30.0, 30.0));

        assertEquals("R(-30.0:30.0)", r.getRepresentation());
    }

    @Test
    public void randomiseWithinDefinedBounds() {
        Rand.setSeed(0);
        Real r1 = Real.valueOf(0.0, new Bounds(-30.0, 30.0));
        Real r2 = Real.valueOf(0.0, new Bounds(-30.0, 30.0));

        assertTrue(r1.doubleValue() == r2.doubleValue());
        r1.randomise();
        assertTrue(r1.doubleValue() != r2.doubleValue());
    }

    @Test
    public void randomiseBetweenInfiniteBounds() {
        Rand.setSeed(0);
        Real r = Real.valueOf(0.0);
        r.randomise();
        Assert.assertThat(r.doubleValue(), not(equalTo(0.0)));
        Assert.assertThat(r.doubleValue(), not(equalTo(Double.NaN)));
    }

    @Test
    public void bucketPositiveRealValue() {
        Real r = Real.valueOf(4.6);
        Assert.assertEquals(5, r.intValue());
    }

    @Test
    public void bucketNegativeRealValue() {
        Real r = Real.valueOf(-4.6);
        Assert.assertEquals(-5, r.intValue());
    }

    @Test
    public void defaultDomain() {
        Bounds expected = new Bounds(-Double.MAX_VALUE, Double.MAX_VALUE);
        Real real = Real.valueOf(0.0);
        Assert.assertThat(real.getBounds(), equalTo(expected));
    }
}
