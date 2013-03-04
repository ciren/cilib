/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the DIMP1 problem defined in the following paper:
 * W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for multiobjective
 * evolutionary algorithms in a fast changing environment, Memetic Computing, 2:87-110,
 * 2010.
 *
 */

public class DIMP1_f1 implements ContinuousFunction {


    //Domain("R(0, 1)");

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        double value = Math.abs(x.doubleValueOf(0));
        return value;
    }
}
