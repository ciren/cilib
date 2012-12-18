/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class RastriginTest {

    private Rastrigin function;

    @Before
    public void instantiate() {
        this.function = new Rastrigin();
    }

    /**
     * Test of evaluate method, of class {@link Rastrigin}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0.0, 0.0);

        assertEquals(0.0, function.apply(x), 0.0);

        x.setReal(0, Math.PI / 2);
        x.setReal(1, Math.PI / 2);

        assertEquals(42.9885094392, function.apply(x), 0.0000000001);
    }
    
    /**
     * Test of gradient method, of class {@link Rastrigin}.
     */
    @Test
    public void testGradient() {
        Vector x = Vector.of(1.0);

        assertEquals(Vector.of(2.0).length(), function.getGradient(x).length(), 0.0000000001);
    }
}
