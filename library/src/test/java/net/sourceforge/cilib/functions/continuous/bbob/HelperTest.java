/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HelperTest {

	@Test
	public void testXOpt() {
		Vector v = Helper.randomXOpt(30);
		for (int i = 0; i < 30; i++) {
			double value = v.doubleValueOf(i);
			assertTrue(value >= -4.0);
			assertTrue(value <= 4.0);
		}
	}

    @Test
    public void testFOpt() {
    	for (int i = 0; i < 200; i++) {
	    	double fOpt = Helper.randomFOpt();
			assertTrue(fOpt >= -1000.0);
			assertTrue(fOpt <= 1000.0);
    	}
    }
}
