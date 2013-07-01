/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.ff.positionupdatestrategies;

import net.cilib.ff.firefly.*;
import net.cilib.type.types.Bounds;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.math.Maths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for the standard firefly position update strategy
 */
public class StandardFireflyPositionUpdateStrategyTest {

    @Test
    public void testUpdatePosition() {
        StandardFireflyPositionUpdateStrategy p = new StandardFireflyPositionUpdateStrategy();
        // set random movement to zero
        p.setAlpha(ConstantControlParameter.of(0.0));

        Bounds bounds = new Bounds(0.0, 1.0);

        Firefly a = new StandardFirefly();
        a.setPosition(Vector.of(Real.valueOf(1.0, bounds), Real.valueOf(1.0, bounds)));

        Firefly b = new StandardFirefly();
        b.setPosition(Vector.of(Real.valueOf(0.0, bounds), Real.valueOf(0.0, bounds)));

        Vector newPosition = p.updatePosition(a, b);
        double attractiveness = (1.0 - 0.2) * Math.exp(-2) + 0.2;
        assertEquals(newPosition.doubleValueOf(0), 1 - attractiveness, Maths.EPSILON);
        assertEquals(newPosition.doubleValueOf(1), 1 - attractiveness, Maths.EPSILON);

        // set gamma to zero (no light absorbtion, all particles can see all others)
        p.setGamma(ConstantControlParameter.of(0.0));
        newPosition = p.updatePosition(a, b);
        attractiveness = 1.0;
        assertEquals(newPosition, Vector.of(Real.valueOf(0.0, bounds), Real.valueOf(0.0, bounds)));
    }
}
