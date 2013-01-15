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
 * <p><b>Bohachevsky 1</b></p>.
 *
 * <p><b>Reference:</b> Global Optimization Meta-Heuristics Website,
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/go.htm</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0 </li>
 * <li> <b>x</b>* = (0, 0)</li>
 * <li> for x<sub>1</sub>, x<sub>2</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-100, 100)^2
 *
 */
public class Bohachevsky1 implements ContinuousFunction {

    private static final long serialVersionUID = 44382638223225638L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Bohachevsky 1 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return x * x + 2 * y * y - 0.3 * Math.cos(3 * Math.PI * x) - 0.4 * Math.cos(4 * Math.PI * y) + 0.7;
    }
}
