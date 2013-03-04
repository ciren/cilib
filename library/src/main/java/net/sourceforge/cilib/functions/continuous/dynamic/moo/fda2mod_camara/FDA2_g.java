/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_camara;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA2_mod problem defined in the following paper:
 * M. Camara, J. Ortega and F. de Toro. Approaching dynamic multi-objective optimization
 * problems by using parallel evolutionary algorithms, Advances in Multi-Objective Nature
 * Inspired Computing, Studies in Computational Intelligence, vol. 272, pp. 63-86,
 * Springer Berlin/Heidelberg, 2010.
 *
 */

public class FDA2_g implements ContinuousFunction {

    private static final long serialVersionUID = 8726700022515610264L;

    //Domain = "R(-1, 1)^15"

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
