/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 */
public class TypesTest {

    @Test
    public void numericDimension() {
        Real r = Real.valueOf(0.0);
        Int i = Int.valueOf(0);
        Bit b = Bit.valueOf(false);

        Assert.assertEquals(1, Types.dimensionOf(r));
        Assert.assertEquals(1, Types.dimensionOf(i));
        Assert.assertEquals(1, Types.dimensionOf(b));
    }

    @Test
    public void structureDimension() {
        Vector vector = Vector.of();
        Assert.assertEquals(0, Types.dimensionOf(vector));

        vector = Vector.newBuilder().copyOf(vector).add(Real.valueOf(0.0)).build();
        Assert.assertEquals(1, Types.dimensionOf(vector));
    }

    @Test
    public void nonStructureDimension() {
        Real r = Real.valueOf(0.0);

        Assert.assertEquals(1, Types.dimensionOf(r));
    }

    @Test
    public void structureIsNotInsideBounds() {
        Vector vector = Vector.of();
        Real r = Real.valueOf(-7.0, new Bounds(-5.0, 5.0));

        vector = Vector.newBuilder().copyOf(vector).add(r).build();
        Assert.assertFalse(Types.isInsideBounds(vector));
    }

    @Test
    public void structureInBounds() {
        Bounds bounds = new Bounds(-5.0, 5.0);
        Real r1 = Real.valueOf(-5.0, bounds);
        Real r2 = Real.valueOf(5.0, bounds);

        Vector vector = Vector.of(r1,r2);

        Assert.assertTrue(Types.isInsideBounds(vector));
    }
}
