/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef5;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the adapted F5 problem defined in the following paper:
 * H. Li and Q. Zhang. Multiobjective optimization problems with complicated Pareto sets,
 * MOEA/D and NSGA-II, IEEE Transactions on Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 */
public class HEF5_g implements ContinuousFunction {

    /**
     * Evaluates the function
     */
    @Override
    public Double apply(Vector input) {
        double value = Math.abs(input.doubleValueOf(0));
        double sum = 0.0;

        int index = 0;
        for (int k=1; k < input.size(); k+=2) {
            double value2 = 0.3*Math.pow(value, 2)*Math.cos(24.0*Math.PI*value + 4.0*(double)k*Math.PI/input.size()) + 0.6*value;
            sum += Math.pow(input.doubleValueOf(k)-value2*Math.sin(6.0*Math.PI*value + (double)k*Math.PI/input.size()), 2);
            index++;
        }
        sum *= 2.0/index;
        sum += 2.0 - Math.sqrt(value);
        return sum;
    }


}
