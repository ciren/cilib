/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_deb;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA2_mod problem defined in the following paper:
 * K. Deb, U. Rao N and S. Karthik. Dynamic Multi-objective optimization and decision making
 * using  modified NSGA-II: A case study on hydro-thermal power scheduling, In Proceedings of
 * the International Conference on Evolutionary Multi-Criterion Optimization (EMO), Lecture
 * Notes in Computer Science, 4403:803-817, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 */

public class FDA2_g implements ContinuousFunction {

    private static final long serialVersionUID = 8726700022515610264L;

    //setDomain("R(-1, 1)^15")

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {

        double sum = 1.0;

        for (int k=0; k < x.size(); k++) {
            sum += Math.pow(x.doubleValueOf(k), 2);
        }

        return sum;
    }

}
