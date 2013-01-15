/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Bukin 6 Function.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle Swarm Methods</i>
 * North-Eastern Hill University, India, 2002</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0.0 </li>
 * <li> <b>x</b>* = (-10,1)</li>
 * <li> for x<sub>1</sub> in [-15,-5], x<sub>2</sub> in [-3,3]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Nonregular</li>
 * </ul>
 * </p>
 *
 * R(-15,-5),R(-3,3)
 *
 */
public class Bukin6 implements ContinuousFunction {

    private static final long serialVersionUID = -5557883529972004157L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Bukin 6 function is only defined for 2 dimensions");

        double x1 = input.doubleValueOf(0);
        double x2 = input.doubleValueOf(1);

        return 100 * Math.sqrt(Math.abs(x2 - 0.01 * x1 * x1)) + 0.01 * Math.abs(x1 + 10);
    }
}
