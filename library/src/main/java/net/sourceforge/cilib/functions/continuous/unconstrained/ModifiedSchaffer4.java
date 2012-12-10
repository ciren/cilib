/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Modified Schaffer Function 4.
 *
 * <p>
 * Characteristics:
 * <ul>
 *  <li>Multi-modal</li>
 *  <li>Non-Seperable</li>
 *  <li>Not Regular</li>
 * </ul>
 * </p>
 *
 * <p>
 * Reference:
 * @article{
 * mishra-some,
 * title={{Some new test functions for global optimization and performance of repulsive particle swarm method}},
 * author={Mishra, S.K. and Campus, N.}
 * }
 * </p>
 */
public class ModifiedSchaffer4 implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        double square1, square2, numerator, denominator;

        for (int j = 0; j < input.size() - 1; j++) {
            square1 = input.doubleValueOf(j) * input.doubleValueOf(j);
            square2 = input.doubleValueOf(j + 1) * input.doubleValueOf(j + 1);
            numerator = Math.pow(Math.cos(Math.sin(Math.abs(square1 - square2))), 2) - 0.5;
            denominator = Math.pow((1 + 0.001 * (square1 + square2)), 2);

            sum += 0.5 + (numerator / denominator);
        }

        return new Double(sum);
    }
}
