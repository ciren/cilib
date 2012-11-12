/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * R(-900, 900)^30
 * 
 */
public class Neumaier3 implements ContinuousFunction {

    private static final long serialVersionUID = 192809046725649930L;

//    /**
//     * {@inheritDoc}
//     */
//    public Double getMinimum() {
////        double dimension = getDimension();
////        return (dimension * (dimension + 4.0) * (dimension - 1.0)) / 6.0;
//        throw new UnsupportedOperationException();
//    }
    /**
     * {@inheritDoc}
     */
    public Double apply(Vector input) {
        double tmp1 = 0;
        double tmp2 = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp1 += (input.doubleValueOf(i) - 1) * (input.doubleValueOf(i) - 1);
        }
        for (int i = 1; i < input.size(); ++i) {
            tmp2 += input.doubleValueOf(i) * input.doubleValueOf(i - 1);
        }
        return tmp1 - tmp2;
    }
}
