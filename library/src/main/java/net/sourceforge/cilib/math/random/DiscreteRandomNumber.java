/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random;

import net.sourceforge.cilib.math.Maths;

/**
 *
 *
 */
public final class DiscreteRandomNumber {

    /**
     * Default constructor hidden - this is a utility class.
     */
    private DiscreteRandomNumber() {
    }

    /**
     * Get the value of the Poisson distribution given a specific point and
     * specific lambda value.
     * @param x The point to calculate the distribution value from.
     * @param lambda The value of lambda.
     * @return The value at point {@code x}.
     */
    public static double getPoisson(int x, int lambda) {
        double numerator = Math.pow(Math.E, -lambda) * Math.pow(lambda, x);
        double denominator = Maths.factorial(x);
        return numerator / denominator;
    }

    /**
     * Get the value of the Binomial distribution at point {@code x}, given the values
     * for {@code p} and {@code n}.
     * @param x The point to calculate the distribution value from.
     * @param p The value of {@code p}.
     * @param n The value of {@code n}.
     * @return The value of the binomial distribution.
     */
    public static double getBinomial(int x, int p, int n) {
        return Maths.combination(n, x) * Math.pow(p, x) * Math.pow((1-p), (n-x));
    }

}
