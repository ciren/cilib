/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.hs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HarmonyTest {

    @Test
    public void equals() {
        Harmony h1 = new Harmony();
        Harmony h2 = new Harmony();

        assertFalse(h1.equals(h2));
        assertFalse(h2.equals(h1));
        assertTrue(h1.equals(h1));

        assertFalse(h1.equals(null));
    }

    @Test
    public void hashCodes() {
        Harmony h1 = new Harmony();
        Harmony h2 = new Harmony();

        assertTrue(h1.hashCode() != h2.hashCode());
    }
}
