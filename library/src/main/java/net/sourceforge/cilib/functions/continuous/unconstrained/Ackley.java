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
 * <p><b>The Generalized Ackley.</b></p>
 *
 * <p><b>Reference:</b> T.Back, <i>Evolutionary Algorithms in Theory and Practice</i>,
 * Oxford University Press, 1996</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-32.768,32.768]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-32.768, 32.768)^30
 *
 *
 */
public class Ackley implements ContinuousFunction {

    private static final long serialVersionUID = -7803711986955989075L;

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
     */
    @Override
    public Double apply(Vector input) {
        final int size = input.size();
        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < size; ++i) {
            sumsq += input.doubleValueOf(i) * input.doubleValueOf(i);
            sumcos += Math.cos(2 * Math.PI * input.doubleValueOf(i));
        }
        return -20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / size)) - Math.exp(sumcos / size) + 20 + Math.E;
    }
}
