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
 * An implementation of the Elliptic function.
 * The number 10^6 is called condition number, which is used to transform
 * a Sphere function to an Elliptic function.
 *
 * Reference:
 * <pre>
 * {@literal @}article{tang2010benchmark,
 * title={{Benchmark Functions for the CEC2010 Special Session and Competition on Large-Scale Global Optimization}},
 * author={Tang, K. and Li, X. and Suganthan, PN and Yang, Z. and Weise, T.},
 * year={2010}}
 * </pre>
 *
 */
public class Elliptic implements ContinuousFunction {

    /*
     * The condition number 10^6 is used to transform a sphere
     * to an elliptic function
     */
    private double conditionNumber = 1E6;

    @Override
    public Double apply(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += Math.pow(conditionNumber, i / (input.size() - 1)) * input.doubleValueOf(i) * input.doubleValueOf(i);
        }

        return sum;
    }

    /*
     * Set the condition number of the elliptic function.
     * @param conditionNumber The new condition number.
     */
    public void setConditionNumber(double conditionNumber) {
        this.conditionNumber = conditionNumber;
    }
}
