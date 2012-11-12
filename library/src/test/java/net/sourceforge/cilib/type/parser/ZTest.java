/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class ZTest {

    private static Z creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new Z();
    }

    @Test
    public void testCreateNoBounds() {
        Type z = creator.create(new Bounds(0, 2));

        assertTrue(z instanceof Int);
    }

    @Test
    public void testCreateBounds() {
        Type z = creator.create(new Bounds(0, 2));

        assertTrue(z instanceof Int);
    }

}
