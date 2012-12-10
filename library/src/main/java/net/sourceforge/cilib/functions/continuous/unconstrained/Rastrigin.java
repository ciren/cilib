/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import fj.F;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Rastrigin function.</b></p>
 *
 * <p><b>Reference:</b> X. Yao and Y. Liu and G. Liu, <i>Evolutionary Programming Made Faster</i>,
 * IEEE Transactions on Evolutionary Computation, vol 3, number 2, pages 82--102, 1999.</p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Seperable</li>
 * </ul>
 * </p>
 *
 * f(x) = 0; x = (0,0,...,0);
 *
 * x e [-5.12, 5.12];
 *
 * R(-5.12, 5.12)^30
 *
 */
public class Rastrigin implements ContinuousFunction, Differentiable {

    private static final long serialVersionUID = 447701182683968035L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp += input.doubleValueOf(i) * input.doubleValueOf(i) - 10.0 * Math.cos(2 * Math.PI * input.doubleValueOf(i));
        }
        return 10 * input.size() + tmp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector input) {
        return input.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric x) {
                return Real.valueOf((2.0 * x.doubleValue()) + (20 * Math.PI * Math.sin(2.0 * Math.PI * x.doubleValue())));
            }
        });
    }
}
