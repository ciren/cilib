/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Shekel4 function.
 *
 * Minimum: f(x*) = 0, x* = (4, 4, 4, 4)
 * N = 5: -10.1532
 * N = 7: -10.40294
 * N = 10: -10.53641
 *
 * R(0, 10)^4
 *
 */
public class Shekel4 implements ContinuousFunction {

    private int n;

    private static final double[][] A = {
        {4.0, 4.0, 4.0, 4.0},
        {1.0, 1.0, 1.0, 1.0},
        {8.0, 8.0, 8.0, 8.0},
        {6.0, 6.0, 6.0, 6.0},
        {3.0, 7.0, 3.0, 7.0},
        {2.0, 9.0, 2.0, 9.0},
        {5.0, 5.0, 3.0, 3.0},
        {8.0, 1.0, 8.0, 1.0},
        {6.0, 2.0, 6.0, 2.0},
        {7.0, 3.6, 7.0, 3.6},};

    private static final double[] C = {0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};

    /**
     * Creates a new instance of {@link Shekel4} and initialises n to 10.
     */
    public Shekel4() {
        setN(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 4, "Shekel4 function is only defined for 4 dimensions");

        double sum = 0;
        for (int i = 0; i < n; ++i) {
            double innerSum = 0;
            for (int j = 0; j < 4; ++j) {
                innerSum += (input.doubleValueOf(j) - A[i][j]) * (input.doubleValueOf(j) - A[i][j]);
            }
            sum += 1 / (innerSum + C[i]);
        }
        return -sum;
    }

    /**
     * Set the N value for the function.
     * @param n The value to set
     */
    public void setN(int n) {
        Preconditions.checkArgument(n == 5 || n == 7 || n == 10, "Shekel4 function N must be 5, 7 or 10.");
        this.n = n;
    }
}
