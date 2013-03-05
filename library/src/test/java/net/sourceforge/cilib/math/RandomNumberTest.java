/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.math.random.DiscreteUniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 *
 */
public class RandomNumberTest {

    private ProbabilityDistributionFunction rand;

    @Test
    public void testGuassian() {
        rand = new GaussianDistribution();

        for (int i = 0; i < 1000; i++) {
            double number = rand.getRandomNumber();
            assertTrue(-5.0 < number);
            assertTrue(number < 5.0);
        }
    }

    @Test
    public void testUniform() {
        rand = new UniformDistribution();

        for (int i = 0; i < 200; i++) {
            double number = rand.getRandomNumber();
            assertTrue(number <= 1.0);
            assertTrue(0.0 <= number);
        }
    }

    @Test
    public void testDiscreteUniform() {
        rand = new DiscreteUniformDistribution();

        for (int i = 0; i < 200; i++) {
            double number = rand.getRandomNumber();
            assertTrue(number == 0.0 || number == 1.0);
        }

        Vector v = Vector.of(1.0, 2.0, 3.0, 4.0);
        rand = new DiscreteUniformDistribution(v);

        for (int i = 0; i < 200; i++) {
            double number = rand.getRandomNumber();
            assertTrue(v.contains(Real.valueOf(number)));
        }

        for (int i = 0; i < 200; i++) {
            double value = rand.getRandomNumber(1.0, 2.0, 3.0, 4.0);
            assertTrue(v.contains(Real.valueOf(value)));
        }
    }

}
