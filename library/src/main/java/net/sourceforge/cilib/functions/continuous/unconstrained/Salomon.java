/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Salomon Function.</b></p>
 *
 * <p><b>Reference:</b> R. Salomon <i>Re-evaluating genetic algorithm performance under coordinate rotation of benchmark functions. A survey of some
 * theoretical and practical aspects of genetic algorithms</i>,
 * Biosystems, 1996</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-600, 600]</li>
 * </ul>
 * </p>
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Non-separable</li>
 * </ul>
 * </p>
 * f(x) = 0; x = (0,0,...,0); x_i e (-600,600)
 *
 * R(-600, 600)^30
 *
 */
public class Salomon implements ContinuousFunction {

    private static final long serialVersionUID = -6002240316648057218L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sumSquares = 0.0;

        for (int i = 0; i < input.size(); i++) {
            sumSquares += input.doubleValueOf(i) * input.doubleValueOf(i);
        }

        return -(Math.cos(2 * Math.PI * Math.sqrt(sumSquares))) + (0.1 * Math.sqrt(sumSquares)) + 1;
    }
}
