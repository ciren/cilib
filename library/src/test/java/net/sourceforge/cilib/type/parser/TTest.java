/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class TTest {

    private static T creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new T();
    }

    @Test
    public void testCreateNoBounds() {
        Type t = creator.create();

        assertTrue(t instanceof StringType);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testCreateBounds() {
        Type t = creator.create(new Bounds(0, 3));
    }

}
