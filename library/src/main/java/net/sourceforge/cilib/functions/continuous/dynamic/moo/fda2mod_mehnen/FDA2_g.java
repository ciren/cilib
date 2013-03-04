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
 * This function is the g function of the FDA2_mod problem defined in the
 * following paper: J. Mehnen, T. Wagner and G. Rudolph. Evolutionary
 * optimization of dynamic multi-objective test functions, In Proceedings of the
 * seconed Italian Workshop on Evolutionary Computation, 2006.
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

        if (x.size() > 1) {
            for (int k = 0; k < 15; k++) {
                sum += Math.pow(x.doubleValueOf(k), 2);
            }

            for (int kk = 15; kk < x.size(); kk++) {
                sum += Math.pow(x.doubleValueOf(kk) + 1.0, 2);
            }
        } else {
            for (int k = 0; k < x.size(); k++) {
                sum += Math.pow(x.doubleValueOf(k), 2);

                for (int kk = 0; kk < x.size(); kk++) {
                    sum += Math.pow(x.doubleValueOf(kk) + 1.0, 2);
                }
            }
        }

        return sum;
    }
}
