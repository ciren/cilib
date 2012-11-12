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
 * <p><b>The Zakharov Function.</b></p>
 *
 * <p><b>Reference:</b> M. Laguna, R. Martı´ <i>Experimental testing of advanced scatter search designs for global optimization of multimodal functions</i>,
 * Journal of Global Optimization, 2005</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-5, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-5, 10)^30
 *
 */
public class Zakharov implements ContinuousFunction {

    private static final long serialVersionUID = -635648546100966058L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0; i < input.size(); ++i){
            sum1 += input.doubleValueOf(i) * input.doubleValueOf(i);
            sum2 += 0.5 * (i + 1) * input.doubleValueOf(i);
        }
        return sum1 + (sum2 * sum2) + (sum2 * sum2 * sum2 * sum2);
    }
}
