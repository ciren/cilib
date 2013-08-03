/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PeakTest {

    @Test
    public void evaluate() {
        Peak peak = new Peak();
        assertEquals(0.0, peak.f(Real.valueOf(Double.POSITIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, peak.f(Real.valueOf(Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, peak.f(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(0.0, peak.f(Real.valueOf(Double.NEGATIVE_INFINITY)).doubleValue(), Maths.EPSILON);
        assertEquals(1.0, peak.f(Real.valueOf(Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        assertEquals(1.0, peak.f(Real.valueOf(0.0)).doubleValue(), Maths.EPSILON);
        assertEquals(0.5, peak.f(Real.valueOf(1.0)).doubleValue(), Maths.EPSILON);
        assertEquals(0.5, peak.f(Real.valueOf(-1.0)).doubleValue(), Maths.EPSILON);
        assertEquals(1/1.25, peak.f(Real.valueOf(0.5)).doubleValue(), Maths.EPSILON);
        assertEquals(1/1.25, peak.f(Real.valueOf(-0.5)).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void derivative() {
        Peak peak = new Peak();
        assertEquals(0.0, peak.getGradient(Double.MIN_VALUE), Maths.EPSILON);
        assertEquals(0.0, peak.getGradient(0.0), Maths.EPSILON);
        assertEquals(-0.5, peak.getGradient(1.0), Maths.EPSILON);
        assertEquals(0.5, peak.getGradient(-1.0), Maths.EPSILON);
    }
}
