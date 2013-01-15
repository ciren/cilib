/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.positionupdatestrategies;

import net.sourceforge.cilib.ff.firefly.*;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.Maths;
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
