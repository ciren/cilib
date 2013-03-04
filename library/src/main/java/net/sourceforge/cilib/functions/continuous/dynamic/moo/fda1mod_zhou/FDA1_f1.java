/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1mod_zhou;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA1_mod problem defined in the following paper:
 * A. Zhou et al. Prediction-based population re-initialization for evolutionary dynamic
 * multi-objective optimization, In Proceedings of the International Conference on
 * Evolutionary Multi-Criterion Optimization (EMO), Lecture  * Notes in Computer Science,
 * 4403:832-846, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 */

public class FDA1_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 1914230427150406406L;

    //Domain("R(0, 1)")

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        double val = Math.abs(x.doubleValueOf(0));
        if (val > 1.0)
            val = 2.0 - (double)val;

        return val;
    }
}
