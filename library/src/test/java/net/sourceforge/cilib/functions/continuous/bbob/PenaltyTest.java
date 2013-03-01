/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PenaltyTest {

    private Penalty function;

    @Before
    public void instantiate() {
        this.function = new Penalty();
    }

    /**
     * Test of evaluate method, of class {@link Penalty}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 2.0, 3.0);
        assertEquals(14.0, function.apply(x), 0.0);

        function.setBoundary(ConstantControlParameter.of(5.0));
        Vector y = Vector.of(5.0, 6.0, 7.0);
        assertEquals(5.0, function.apply(y), 0.0);
    }
}
