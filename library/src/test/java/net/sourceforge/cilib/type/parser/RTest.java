/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.type.parser;

import net.cilib.type.types.Bounds;
import static org.junit.Assert.assertTrue;
import net.cilib.type.types.Real;
import net.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class RTest {

    private static R creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new R();
    }

    @Test
    public void testCreateNoBounds() {
        Type r = creator.create(new Bounds(0, 2));

        assertTrue(r instanceof Real);
    }

    @Test
    public void testCreateBounds() {
        Type r = creator.create(new Bounds(0, 2));

        assertTrue(r instanceof Real);
    }

}
