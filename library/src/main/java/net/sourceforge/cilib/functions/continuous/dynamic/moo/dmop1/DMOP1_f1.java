/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the DMOP1 problem defined on page 119 in the following article:
 * C-K. Goh and K.C. Tan. A competitive-cooperative coevolutionary paradigm for dynamic multiobjective
 * optimization, IEEE Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */

public class DMOP1_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 5097532459618440548L;

    //Domain = "R(0, 1)"

    @Override
    public Double apply(Vector x) {
    	double value = Math.abs(x.doubleValueOf(0));
	return value;
    }
}
