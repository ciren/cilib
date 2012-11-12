/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

public class Bohachevsky1Test {

    private ContinuousFunction function;

    public Bohachevsky1Test() {
    }

    @Before
    public void instantiate() {
        this.function = new Bohachevsky1();
    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Bohachevsky1. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 2.0);
        assertEquals(9.6, function.apply(x), 0.0);

        x.setReal(0, 0.0);
        x.setReal(1, 0.0);
        assertEquals(0.0, function.apply(x), 0.0);
    }
}
