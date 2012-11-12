/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class EggHolderTest {

    public EggHolderTest() {

    }

    @Test
    public void testEvaluate() {
        ContinuousFunction function = new EggHolder();
        Vector vector = Vector.of(200,100);
        Assert.assertEquals(-166.745338888944,function.apply(vector),0.00000000001);
    }

    @Test
    public void testMinimum() {
        ContinuousFunction function = new EggHolder();
        Vector vector = Vector.of(512,404.2319);
        Assert.assertEquals(-959.640662710616,function.apply(vector),0.00000000001);
    }
}
