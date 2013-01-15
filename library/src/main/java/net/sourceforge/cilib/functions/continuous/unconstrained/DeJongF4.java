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
 * <p><b>De Jong's Function 4 (no noise).</b></p>
 *
 * <p><b>Reference:</b> S. Rahnamayan, H. R. Tizhoosh, M. M. A. Salama <i>A novel population initialisation method for accelerating evolutionary algorithms</i>,
 * Computers and Mathematics with Applications, 2007</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-1.28, 1.28]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-1.28, 1.28)^30
 *
 */
public class DeJongF4 implements ContinuousFunction {

    private static final long serialVersionUID = 4835441178770462999L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        for (int i = 0; i < input.size(); ++i) {
            double xi = input.doubleValueOf(i);
            sum += (i + 1) * (xi * xi * xi * xi);
        }
        return sum;
    }
}
