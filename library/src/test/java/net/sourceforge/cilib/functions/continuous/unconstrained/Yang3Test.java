/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import org.junit.Before;
import org.junit.Test;

public class Yang3Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Yang3();
    }

    /**
     * Test of evaluate method, of class {@link Yang3}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(Math.PI, Math.PI);
        double z = function.f(x);
        double K = 10.0;
        assertThat(z, is(greaterThanOrEqualTo(-(K * K + 5))));
        assertThat(z, is(lessThanOrEqualTo(-5.0)));
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}
