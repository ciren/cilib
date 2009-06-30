/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.math.random;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class provides the needed functionality to sample random numbers from different
 * continuous valued distributions.
 * <p>
 * These distributions include:<br>
 * <ul>
 *   <li>Gaussian / Normal random numbers</li>
 *   <li>Cauchy random numbers</li>
 *   <li>Uniform random numbers</li>
 * </ul>
 *
 * @author Gary Pampara
 */
public class RandomNumber implements Cloneable {
    private static final long serialVersionUID = -7960211483219171592L;

    private Random randomGenerator;


    /**
     * Create a <code>RandomNumber</code> instance. The internal random number generator is
     * defined to be the <code>MersenneTwister</code> by default with a <code>mean</code>
     * of 0.0 and a <code>deviation</code> of 1.0.
     */
    public RandomNumber() {
        randomGenerator = new MersenneTwister();
    }

    public RandomNumber(RandomNumber copy) {
        this.randomGenerator = copy.randomGenerator.getClone();
    }

    public RandomNumber getClone() {
        return new RandomNumber(this);
    }


    /**
     * Return the random number generator being used.
     * @return The random number generator.
     */
    public Random getRandomGenerator() {
        return this.randomGenerator;
    }


    /**
     * Set the random number generator to be used.
     * @param random The random number generator to be used.
     */
    public void setRandomGenerator(Random random) {
        this.randomGenerator = random;
    }


    /**
     * Return a random number from the Guassian distribution with a <code>mean</code> of
     * 0.0 and a <code>deviation</code> of 1.0.
     *
     * @return Random number ~ N(0,1).
     */
    public double getGaussian() {
        return getGaussian(0.0, 1.0);
    }


    /**
     * Return a random number with the mean of <code>mean</code> and a deviation of
     * <code>deviation</code>. Based on the formula:<br><code>s*U(0, 1) + m == U(m, s)</code>
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
     * @return A Gaussian number with mean <code>mean</code> and deviation <code>deviation</code>
     */
    public double getGaussian(double location, double scale) {
         double  q, u, v, x, y;

         /*
           Generate P = (u,v) uniform in rect. enclosing acceptance region
           Make sure that any random numbers <= 0 are rejected, since
           gaussian() requires uniforms > 0, but nextDouble() delivers >= 0.
         */
         do {
             u = randomGenerator.nextDouble();
             v = randomGenerator.nextDouble();

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
             if (q < 0.27597)
                 break;

             /*  Reject P if outside outer ellipse, or outside acceptance region */
         } while ((q > 0.27846) || (v * v > -4.0 * Math.log(u) * u * u));

         /*  Return ratio of P's coordinates as the normal deviate */
        return (location + scale * v / u);
    }


    /**
     *
     * @return
     */
    public double getNormal() {
        return getGaussian();
    }


    /**
     * Convenience method for <code>getGaussian()</code>.
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Guassian number with mean <code>location</code> and deviation <code>scale</code>.
     */
    public double getNormal(double location, double scale) {
        return getGaussian(location, scale);
    }


    /**
     * Convenience method to obtain a Cauchy number. The distribution has a <code>mean</code>
     * of <code>0.0</code> and a <code>deviation</code> of <code>1.0</code>.
     * @return A cauchy number in the given distribution.
     */
    public double getCauchy() {
        return getCauchy(0.0, 1.0);
    }


    /**
     * Return a random number sampled from the Cauchy distribution.
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Cauchy random number with location <tt>location</tt> and
     *         scale parameter <tt>scale</tt>
     */
    public double getCauchy(double location, double scale) {
        double x = randomGenerator.nextDouble(); // Uniform number between 0.0 and 1.0

        double term = (x-location)/scale;
        return (1.0 / scale*Math.PI*(1 + (term*term)));
    }


    /**
     * Get a uniform random number located within <code>0 &lt;= x &lt; 1</code>.
     *
     * @return Uniform random number (<code>0 &lt;= x &lt; 1</code>).
     */
    public double getUniform() {
        return getUniform(0.0, 1.0);
    }


    /**
     * Get the uniform random number. The number is located within <code>A &lt;= x &lt; B</code>
     * where <code>A == mean</code> and <code>B == deviation</code>.
     *
     * @param lower The lower bound for the number generation.
     * @param upper The upper bound for the number generation.
     * @return Uniform random number (<code>lower &lt;= x &lt; upper</code>).
     */
    public double getUniform(double lower, double upper) {
        double r = randomGenerator.nextDouble();
        return ((upper - lower) * r + lower);
    }

}
