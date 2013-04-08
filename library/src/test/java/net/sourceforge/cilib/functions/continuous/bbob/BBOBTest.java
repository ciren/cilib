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
import org.junit.Before;
import org.junit.Test;

public class BBOBTest {

	@Test
	public void testBBOB() throws Exception {
		// f19 - f24: xOpt doesn't result in fOpt
		for (int i = 1; i <= 18; i++) {
			if (i != 9) {
				String className = "net.sourceforge.cilib.functions.continuous.bbob.BBOB" + i;
				testApply((AbstractBBOB)Class.forName(className).newInstance());
			}
		}
	}

	private void testApply(AbstractBBOB f) {
		f.apply(Vector.fill(1, 10));
		assertEquals(f.getOptimum(), f.apply(f.getXOpt()), 0.0);
	}
}
