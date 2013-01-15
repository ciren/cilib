/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Alpine Function.</b></p>
 *
 * <p><b>Reference:</b> S. Rahnamayan, H. R. Tizhoosh, M. M. A. Salama <i>A novel population initialisation method for accelerating evolutionary algorithms</i>,
 * Computers and Mathematics with Applications, 2007</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-10, 10)^30
 * 
 */
public class Alpine implements ContinuousFunction {

    private static final long serialVersionUID = -1365268075451075465L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector x) {
        double sum = 0;
        for (Numeric n : x) {
            sum += Math.abs((n.doubleValue() * Math.sin(n.doubleValue())) + (0.1 * n.doubleValue()));
        }
        return sum;
    }
}
