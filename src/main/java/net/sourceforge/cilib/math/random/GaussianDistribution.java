/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 */
public class GaussianDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;

    /**
     * Default constructor.
     */
    public GaussianDistribution() {
        provider = new MersenneTwister();
    }

    public GaussianDistribution(long seed) {
        provider = new MersenneTwister(seed);
    }

    /**
     * Get a Gaussian number with currently set <code>mean</code> and <code>deviation</code>.
     * @return A Gaussian number with mean <code>0.0</code> and deviation <code>1.0</code>.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(0.0, 1.0);
    }

    /**
     * Return a random number with the mean of <code>mean</code> and a deviation of
     * <code>deviation</code>. Based on the formula:<br><code>s*U(0, 1) + m == U(m, s)</code>
     *
     * Two parameters are required. The first specifies the location, the second
     * specifies the scale.
     *
     * <p>
     * ALGORITHM 712, COLLECTED ALGORITHMS FROM ACM.<br>
     * THIS WORK PUBLISHED IN TRANSACTIONS ON MATHEMATICAL SOFTWARE,<br>
     * VOL. 18, NO. 4, DECEMBER, 1992, PP. 434-435.<br>
     * The function returns a normally distributed pseudo-random number<br>
     * with a given mean and standard deviation.  Calls are made to a<br>
     * function subprogram which must return independent random<br>
     * numbers uniform in the interval (0,1).
     * <p>
     * The algorithm uses the ratio of uniforms method of A.J. Kinderman
     * and J.F. Monahan augmented with quadratic bounding curves.
     *
     * @param location The mean to use.
     * @param scale The deviation to use.
     * @return A Gaussian number with mean <code>location</code> and deviation <code>scale</code>
     */
    @Override
    public double getRandomNumber(double... locationScale) {
        checkArgument(locationScale.length == 2, "The Gaussian distribution requires two parameters. The first specifies the mean, the second specifies the deviation.");
        double q, u, v, x, y;

        /*
        Generate P = (u,v) uniform in rect. enclosing acceptance region
        Make sure that any random numbers <= 0 are rejected, since
        gaussian() requires uniforms > 0, but nextDouble() delivers >= 0.
         */
        do {
            u = provider.nextDouble();
            v = provider.nextDouble();

            if (u <= 0.0 || v <= 0.0) {
                u = 1.0;
                v = 1.0;
            }
            v = 1.7156 * (v - 0.5);

            /*  Evaluate the quadratic form */
            x = u - 0.449871;
            y = Math.abs(v) + 0.386595;
            q = x * x + y * (0.19600 * y - 0.25472 * x);

            /* Accept P if inside inner ellipse */
            if (q < 0.27597) {
                break;
            }

            /*  Reject P if outside outer ellipse, or outside acceptance region */
        } while ((q > 0.27846) || (v * v > -4.0 * Math.log(u) * u * u));

        /*  Return ratio of P's coordinates as the normal deviate */
        return (locationScale[0] + locationScale[1] * v / u);
    }

    public RandomProvider getProvider() {
        return provider;
    }

    public void setProvider(RandomProvider provider) {
        this.provider = provider;
    }

    @Override
    public RandomProvider getRandomProvider() {
        return this.provider;
    }
}
