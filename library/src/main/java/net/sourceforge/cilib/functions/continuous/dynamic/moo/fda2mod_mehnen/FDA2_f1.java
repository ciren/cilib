/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_mehnen;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA2_mod problem defined in the following paper:
 * J. Mehnen, T. Wagner and G. Rudolph. Evolutionary optimization of dynamic multi-objective
 * test functions, In Proceedings of the seconed Italian Workshop on Evolutionary Computation,
 * 2006.
 *
 */

public class FDA2_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 3509865802519318920L;

    //Domain("R(0, 1)")

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        return (double)(Math.abs(x.doubleValueOf(0)));
    }
}
