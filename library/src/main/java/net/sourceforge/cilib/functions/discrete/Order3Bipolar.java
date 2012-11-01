/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Implementation of the Bipolar order-3 problem.
 * Intended for bit strings that are multiples of length 6.
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Al-kazemi, Buthainah Sabeeh No'man and Mohan, Chilukuri K., "Multi-phase
 * discrete particle swarm optimization" (2000). Electrical Engineering and
 * Computer Science. Paper 54.
 * </li></ul>
 */
public class Order3Bipolar implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.size()-5; i+=6) {
            result += getValue(input.copyOfRange(i,i+6));
        }

        return result;
    }

    private Double getValue(Vector input) {

        int sum = 0;
        for(int i = 0; i < input.size(); i++) {
            if (input.booleanValueOf(i)) {
                sum++;
            }
        }

        if ((sum == 0) || (sum == 6)) {
           return 1.0;
        } else if ((sum == 1) || (sum == 5)) {
            return 0.0;
        } else if ((sum == 2) || (sum == 4)) {
            return 0.4;
        } else {
            return 0.8;
        }
    }
}
